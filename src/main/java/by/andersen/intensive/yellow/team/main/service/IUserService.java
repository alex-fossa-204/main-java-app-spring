package by.andersen.intensive.yellow.team.main.service;

import by.andersen.intensive.yellow.team.main.model.entity.User;
import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface IUserService {

    User register(String username, String firstName, String lastname, String email, String password) throws ServiceException;

    List<User> getUsers() throws ServiceException;

    User findByUserName(String username) throws ServiceException;

    User findUserByEmail(String email) throws ServiceException;

    User addNewUser(String username, String firstName, String lastname, String email, boolean isNonLocked, boolean isActive, String roleName, MultipartFile profileImage) throws ServiceException, MessagingException, IOException;

    User updateUser(String currentUserName,String newFirstName, String newLastName, String newUserName, String newEmail, String roleName, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws ServiceException, IOException;

    void deleteUser(String userName) throws ServiceException, IOException;

    void resetPassword(String email) throws ServiceException, MessagingException;

    User updateProfileImage(String userName, MultipartFile profileImage) throws ServiceException, IOException;
}
