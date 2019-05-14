package de.mpg.mpdl.mpadmanager.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameAlreadyBoundException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.mpg.mpdl.mpadmanager.dto.UserDTO;
import de.mpg.mpdl.mpadmanager.model.LdapUser;
import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.model.VerificationToken;
import de.mpg.mpdl.mpadmanager.repository.LdapGroupRepository;
import de.mpg.mpdl.mpadmanager.repository.LdapUserRepository;
import de.mpg.mpdl.mpadmanager.repository.UserRepository;
import de.mpg.mpdl.mpadmanager.repository.VerificationTokenRepository;
import de.mpg.mpdl.mpadmanager.web.error.UserAlreadyExistException;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LdapGroupRepository ldapGroupRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;
    
    @Autowired
    private LdapUserRepository ldapUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private SessionRegistry sessionRegistry;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";
    public static final String TOKEN_EXIST = "userExist";

    public static String APP_NAME = "SpringRegistration";

    // API

    @Override
    public User registerNewUserAccount(final UserDTO accountDto) {
        if (emailExist(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
        }
        final User user = new User();

        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setEmail(accountDto.getEmail());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setOrganization(accountDto.getOrganization());
        user.setDepartment(accountDto.getDepartment());
        return userRepository.save(user);
    }

    @Override
    public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            String email = token.getUserEmail();
            return userRepository.findByEmail(email);
        }
        return null;
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(final User user) {
        final VerificationToken verificationToken = tokenRepository.findByUserEmail(user.getEmail());

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }
        
        userRepository.delete(user);
    }

    @Override
    public void createVerificationTokenForUser(final String email, final String token) {
        final VerificationToken myToken = new VerificationToken(token, email);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
            .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = userRepository.findByEmail(verificationToken.getUserEmail());
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
            .getTime()
            - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }
        user.setEnabled(true);
        tokenRepository.delete(verificationToken);
        userRepository.save(user);
        
        try {
            LdapUser ldapUser = new LdapUser(user.getFirstName(), user.getLastName(), user.getPassword(), user.getOrganization(), user.getEmail(), "0049-89-38602", "asesome", user.getDepartment());
            ldapUserRepository.create(ldapUser);
        } catch (Exception e) {
            if (e instanceof NameAlreadyBoundException) return TOKEN_EXIST;
            else {
                System.out.print(e);
                return TOKEN_INVALID;
            }
        }
        return TOKEN_VALID;
    }

    private boolean emailExist(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public List<String> getUsersFromSessionRegistry() {
        return sessionRegistry.getAllPrincipals()
            .stream()
            .filter((u) -> !sessionRegistry.getAllSessions(u, false)
                .isEmpty())
            .map(o -> {
                if (o instanceof User) {
                    return ((User) o).getEmail();
                } else {
                    return o.toString();
                }
            })
            .collect(Collectors.toList());
    }

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}
	
    @Override
    public List<VerificationToken> findExpiredTokens(Date now) {
    	List<VerificationToken> tokens = tokenRepository.findExpiredTokens(now);
    	return tokens;
    }

	@Override
	public void deleteVerificationToken(String token) {
		VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }
	}
}
