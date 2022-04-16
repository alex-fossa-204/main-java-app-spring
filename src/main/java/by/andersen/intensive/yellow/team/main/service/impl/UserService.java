package by.andersen.intensive.yellow.team.main.service.impl;

import by.andersen.intensive.yellow.team.main.model.entity.Role;
import by.andersen.intensive.yellow.team.main.model.entity.User;
import by.andersen.intensive.yellow.team.main.repository.RoleRepository;
import by.andersen.intensive.yellow.team.main.repository.UserRepository;
import by.andersen.intensive.yellow.team.main.security.details.UserPrincipal;
import by.andersen.intensive.yellow.team.main.service.IUserService;
import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;

import by.andersen.intensive.yellow.team.main.service.exception.impl.user.EmailExistException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.NotAnImageFileException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.UserNotFoundException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.UsernameExistException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.andersen.intensive.yellow.team.main.service.exception.message.MessageConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.http.MediaType.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserService implements IUserService, UserDetailsService {

    private static final String USER_ROLE = "USER";
    public static final String USER_IMAGE_PATH = "/user/image/";
    public static final String JPG_EXTENSION = "jpg";
    public static final String USER_FOLDER = System.getProperty("user.home") + "/yellow-team/user/";
    public static final String DEFAULT_USER_IMAGE_PATH = "/user/image/profile/";
    public static final String DOT = ".";
    public static final String FORWARD_SLASH = "/";
    public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private EmailService emailService;

    private LoginAttemptService loginAttemptService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService, LoginAttemptService loginAttemptService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.loginAttemptService = loginAttemptService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        UserPrincipal userPrincipal = null;
        boolean isNull = user == null;
        if (isNull) {
            throw new UsernameNotFoundException(username);
        }
        validateLoginAttempt(user);
        userPrincipal = new UserPrincipal(user);
        return userPrincipal;
    }

    @Override
    public User register(String username, String firstName, String lastName, String email, String password) throws UserNotFoundException, UsernameExistException, EmailExistException {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRoleByRoleName(USER_ROLE));

        User registerUser = new User(username, firstName, lastName, email);

        registerUser.setPassword(encodePassword(password));
        registerUser.setRoles(roles);

        userRepository.save(registerUser);
        return registerUser;
    }

    @Override
    public List<User> getUsers() throws ServiceException {
        List<User> users = userRepository.findAll();
        if(users == null) {
            throw new ServiceException(EMPTY_RESULT_SET_GET_ALL_USERS);
        }
        return users;
    }

    @Override
    public User findByUserName(String username) throws UserNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException, EmailExistException {
        validateEmailAddress(EMPTY, email);
        return userRepository.findUserByEmailAddress(email);
    }

    @Override
    public User addNewUser(String username, String firstName, String lastName, String email, boolean isNonLocked, boolean isActive, String roleName, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException, NotAnImageFileException {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRoleByRoleName(roleName));

        User addUser = new User(username, firstName, lastName, email);
        String password = generatePassword();
        addUser.setPassword(encodePassword(password));
        addUser.setRoles(roles);
        addUser.setUserProfileImageUrl(getTemporaryProfileImageUrl(username));

        userRepository.save(addUser);
        emailService.sendNewPasswordEmail(username, password, email);
        return addUser;
    }

    @Override
    public User updateUser(String currentUserName, String newFirstName, String newLastName, String newUsername, String newEmail, String roleName, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRoleByRoleName(roleName));
        User userUpdate = userRepository.findUserByUsername(currentUserName);

        boolean isNotNull = userUpdate != null;
        boolean isUserNamesEquals = currentUserName.equalsIgnoreCase(newUsername);

        if(isNotNull && isUserNamesEquals) {
            userUpdate.setFirstName(newFirstName);
            userUpdate.setLastName(newLastName);
            userUpdate.setEmailAddress(newEmail);
            userUpdate.setRoles(roles);
            userUpdate.setNonLocked(isNonLocked);
            userUpdate.setActive(isActive);
            userRepository.save(userUpdate);
        }
        if(isNotNull && !isUserNamesEquals) {
            userUpdate.setUsername(newUsername);
            userUpdate.setFirstName(newFirstName);
            userUpdate.setLastName(newLastName);
            userUpdate.setEmailAddress(newEmail);
            userUpdate.setRoles(roles);
            userUpdate.setNonLocked(isNonLocked);
            userUpdate.setActive(isActive);
            userRepository.save(userUpdate);
        }
        return userUpdate;
    }

    @Override
    public void deleteUser(String username) throws UserNotFoundException, UsernameExistException {
        User user = userRepository.findUserByUsername(username);
        userRepository.deleteById(user.getId());
    }

    @Override
    public void resetPassword(String email) throws UserNotFoundException, EmailExistException, MessagingException {
        User user = userRepository.findUserByEmailAddress(email);
        if(user != null) {
            String password = generatePassword();
            user.setPassword(encodePassword(password));
            userRepository.save(user);
            emailService.sendNewPasswordEmail(user.getFirstName(), password, email);
        }

    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, NotAnImageFileException, IOException {
        User user = validateUsernameAndEmailAddress(username, EMPTY, EMPTY);
        saveProfileImage(user, profileImage);
        return user;
    }

    private void validateLoginAttempt(User user) {
        if(user.isNonLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNonLocked(false);
            } else {
                user.setNonLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private String getTemporaryProfileImageUrl(String userName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + userName).toUriString();
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException{
        if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + " is not an image, please try again.");
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setUserProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
        }
    }

    private String setProfileImageUrl(String userName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + userName + FORWARD_SLASH + userName + DOT + JPG_EXTENSION).toUriString();
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String encodePassword(String password) {
        return this.bCryptPasswordEncoder.encode(password);
    }

    private User validateUsernameAndEmailAddress(String currentUsername, String newUsername, String newEmailAddress) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User currentUser = userRepository.findUserByUsername(currentUsername);
        User newUser = userRepository.findUserByUsername(newUsername);
        User result = null;
        boolean isNotBlank = StringUtils.isNotBlank(currentUsername);
        if(isNotBlank) {
            if(currentUser == null) {
                throw new UserNotFoundException(String.format("%s. username = %s", USER_NOT_FOUND, currentUsername));
            }
            if(newUser != null && currentUser.getUsername().equals(newUser.getUsername())) {
                throw new UsernameExistException(String.format("%s. username = %s", USERNAME_ALREADY_EXISTS, currentUsername));
            }
            if(newUser != null && currentUser.getEmailAddress().equals(newUser.getEmailAddress())) {
                throw new EmailExistException(String.format("%s. email = %s", EMAIL_ALREADY_EXISTS, newEmailAddress));
            }
            result = currentUser;
        }
        if(!isNotBlank) {
            if(newUser != null) {
                throw new UserNotFoundException(String.format("%s. username = %s", USERNAME_ALREADY_EXISTS, currentUsername));
            }
            if(newUser != null) {
                throw new EmailExistException(String.format("%s. email = %s", EMAIL_ALREADY_EXISTS, newEmailAddress));
            }
        }
        return result;
    }

    private User validateUsername(String currentUsername, String newUsername) throws UserNotFoundException, UsernameExistException {
        User currentUser = userRepository.findUserByUsername(currentUsername);
        User newUser = userRepository.findUserByUsername(newUsername);
        User result = null;
        boolean isNotBlank = StringUtils.isNotBlank(currentUsername);
        if(isNotBlank) {
            if(currentUser == null) {
                throw new UserNotFoundException(String.format("%s. username = %s", USER_NOT_FOUND, currentUsername));
            }
            if(newUser != null && currentUser.getUsername().equals(newUser.getUsername())) {
                throw new UsernameExistException(String.format("%s. username = %s", USERNAME_ALREADY_EXISTS, currentUsername));
            }
            result = currentUser;
        }
        if(!isNotBlank) {
            if(newUser != null) {
                throw new UsernameExistException(String.format("%s. username = %s", USERNAME_ALREADY_EXISTS, currentUsername));
            }
        }
        return result;
    }

    private User validateEmailAddress(String currentEmailAddress, String newEmailAddress) throws UserNotFoundException, EmailExistException {
        User currentUser = userRepository.findUserByEmailAddress(currentEmailAddress);
        User newUser = userRepository.findUserByEmailAddress(newEmailAddress);
        User result = null;
        boolean isNotBlank = StringUtils.isNotBlank(currentEmailAddress);
        if(isNotBlank) {
            if(currentUser == null) {
                throw new UserNotFoundException(String.format("%s. email = %s", USER_NOT_FOUND, currentEmailAddress));
            }
            if(newUser != null && currentUser.getEmailAddress().equals(newUser.getEmailAddress())) {
                throw new EmailExistException(String.format("%s. email = %s", EMAIL_ALREADY_EXISTS, currentEmailAddress));
            }
            result = currentUser;
        }
        if(!isNotBlank) {
            if(newUser != null) {
                throw new EmailExistException(String.format("%s. email = %s", EMAIL_ALREADY_EXISTS, currentEmailAddress));
            }
        }
        return result;
    }

}