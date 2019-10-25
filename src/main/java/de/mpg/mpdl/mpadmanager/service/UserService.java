package de.mpg.mpdl.mpadmanager.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameAlreadyBoundException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.mpg.mpdl.mpadmanager.dto.UserDTO;
import de.mpg.mpdl.mpadmanager.model.CoordinateTeam;
import de.mpg.mpdl.mpadmanager.model.LdapGroup;
import de.mpg.mpdl.mpadmanager.model.LdapUser;
import de.mpg.mpdl.mpadmanager.model.Organization;
import de.mpg.mpdl.mpadmanager.model.PasswordResetToken;
import de.mpg.mpdl.mpadmanager.model.ResearchField;
import de.mpg.mpdl.mpadmanager.model.ResearchMethod;
import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.model.VerificationToken;
import de.mpg.mpdl.mpadmanager.repository.CoordinateTeamRepository;
import de.mpg.mpdl.mpadmanager.repository.LdapGroupRepository;
import de.mpg.mpdl.mpadmanager.repository.LdapUserRepository;
import de.mpg.mpdl.mpadmanager.repository.OrganizationRepository;
import de.mpg.mpdl.mpadmanager.repository.PasswordResetTokenRepository;
import de.mpg.mpdl.mpadmanager.repository.ResearchFieldRepository;
import de.mpg.mpdl.mpadmanager.repository.ResearchMethodRepository;
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
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private CoordinateTeamRepository coordinateTeamRepository;

    @Autowired
    private ResearchFieldRepository researchFieldRepository;

    @Autowired
    private ResearchMethodRepository researchMethodRepository;
    
    @Autowired
    private LdapUserRepository ldapUserRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private SessionRegistry sessionRegistry;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";
    public static final String TOKEN_EXIST = "userExist";

    public static String APP_NAME = "SpringRegistration";
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    // API

    @Override
    public User registerNewUserAccount(final UserDTO accountDto) {
        if (emailExist(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + accountDto.getEmail());
        }
        final User user = new User();

        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setEmail(accountDto.getEmail());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setOrganization(accountDto.getOrganization());
        user.setDepartment(accountDto.getDepartment());
        user.setTelephone(accountDto.getTelephone());
        user.setAddress(accountDto.getAddress() + " " + accountDto.getCity() + " " + accountDto.getCountry());
        user.setZip(accountDto.getZip());
        if (null != accountDto.getCoordinateTeams() && accountDto.getCoordinateTeams().size() > 0) {
            List<CoordinateTeam> coordinateTeams = user.getCoordinateTeams();
            for (String coordinateTeamName : accountDto.getCoordinateTeams()) {
                CoordinateTeam coordinateTeam = coordinateTeamRepository.findByName(coordinateTeamName);
                if (coordinateTeam != null) {
                    coordinateTeams.add(coordinateTeam);
                    coordinateTeam.getUsers().add(user);
                } else {
                    CoordinateTeam newTag =createCoordinateTeamIfNotFound(coordinateTeamName);
                    coordinateTeams.add(newTag);
                    newTag.getUsers().add(user);
                }
            }
        }

        if (null != accountDto.getResearchFields() && accountDto.getResearchFields().size() > 0) {
            List<ResearchField> researchFields = user.getResearchFields();
            for (String researchFieldName : accountDto.getResearchFields()) {
                ResearchField researchField = researchFieldRepository.findByName(researchFieldName);
                if (researchField != null) {
                    researchFields.add(researchField);
                    researchField.getUsers().add(user);
                } else {
                    ResearchField newTag = createResearchFieldIfNotFound(researchFieldName);
                    researchFields.add(newTag);
                    newTag.getUsers().add(user);
                }
            } 
        }

        if (null != accountDto.getResearchMethods() && accountDto.getResearchMethods().size() > 0) {
            List<ResearchMethod> researchMethods = user.getResearchMethods();
            for (String researchMethodName : accountDto.getResearchMethods()) {
                ResearchMethod researchMethod = researchMethodRepository.findByName(researchMethodName);
                if (researchMethod != null) {
                    researchMethods.add(researchMethod);
                    researchMethod.getUsers().add(user);
                } else {
                    ResearchMethod newTag =createResearchMethodIfNotFound(researchMethodName);
                    researchMethods.add(newTag);
                    newTag.getUsers().add(user);
                }
            }
        }

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
        LOGGER.info("validateVerificationToken");
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
        userRepository.save(user);
        
        try {
            String organization = user.getOrganization();
            if (null == organizationRepository.findByName(organization)) {
                Organization org = new Organization(organization);
                organizationRepository.save(org);
                ldapGroupRepository.create(new LdapGroup(organization));
                LOGGER.info("Create organization: " + organization);
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
            if (e instanceof NameAlreadyBoundException) {
                LOGGER.info("Organization already exists on LDAP");
            } else {
                LOGGER.error("Create organization failed");
                return TOKEN_EXIST;
            }
        }

        try {
            LdapUser ldapUser = new LdapUser(user.getFirstName(), user.getLastName(), user.getPassword(), user.getOrganization(), user.getEmail(), user.getTelephone(), "no description yet", user.getDepartment(), user.getZip(), user.getAddress());
            LdapUser ldap = ldapUserRepository.create(ldapUser);
            LOGGER.info("User created on LDAP");
            LOGGER.info(ldap.toString());
        } catch (Exception e) {
            if (e instanceof NameAlreadyBoundException) {
                LOGGER.info("User already on LDAP");
                return TOKEN_EXIST;
            }
            else {
                userRepository.delete(user);
                LOGGER.error("other ldap exceptions: " + user.getEmail(), e.toString());
                LOGGER.error(e.toString());
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

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public User getUserByPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token).getUser();
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        ldapUserRepository.updatePassword(user, passwordEncoder.encode(password));
    }
    
    private final CoordinateTeam createCoordinateTeamIfNotFound(final String name) {
		CoordinateTeam coordinateTeam = coordinateTeamRepository.findByName(name);
		if (coordinateTeam == null) {
			coordinateTeam = new CoordinateTeam(name);
		}
		coordinateTeam = coordinateTeamRepository.save(coordinateTeam);
		return coordinateTeam;
    }
    
    private final ResearchField createResearchFieldIfNotFound(final String name) {
		ResearchField researchField = researchFieldRepository.findByName(name);
		if (researchField == null) {
			researchField = new ResearchField(name);
		}
		researchField = researchFieldRepository.save(researchField);
		return researchField;
    }
    
    private final ResearchMethod createResearchMethodIfNotFound(final String name) {
		ResearchMethod researchMethod = researchMethodRepository.findByName(name);
		if (researchMethod == null) {
			researchMethod = new ResearchMethod(name);
		}
		researchMethod = researchMethodRepository.save(researchMethod);
		return researchMethod;
	}
}
