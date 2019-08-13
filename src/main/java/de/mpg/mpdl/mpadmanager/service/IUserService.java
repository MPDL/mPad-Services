package de.mpg.mpdl.mpadmanager.service;

import java.util.Date;
import java.util.List;

import de.mpg.mpdl.mpadmanager.dto.UserDTO;
import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.model.VerificationToken;
import de.mpg.mpdl.mpadmanager.web.error.UserAlreadyExistException;


public interface IUserService {

    User registerNewUserAccount(UserDTO accountDto) throws UserAlreadyExistException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(String email, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    User findUserByEmail(String email);
    
    List<User> findAllUsers();

    String validateVerificationToken(String token);

    List<String> getUsersFromSessionRegistry();
    
    List<VerificationToken> findExpiredTokens(Date now);

    void deleteVerificationToken(String token);

}

