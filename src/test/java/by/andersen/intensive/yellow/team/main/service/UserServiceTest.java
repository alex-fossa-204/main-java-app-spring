package by.andersen.intensive.yellow.team.main.service;

import by.andersen.intensive.yellow.team.main.model.entity.User;
import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    IUserService userDetailsService;

    @Test
    public void findAllUsersTest() throws ServiceException {
        List<User> users = userDetailsService.getUsers();
        users.forEach(user -> {
            System.out.println(user);
            System.out.println("\t" + user.getRoles());
            user.getRoles().forEach(role -> System.out.println("\t" + role.getPrivileges()));
        });
    }

    @Test
    public void findByUserName() throws ServiceException {
        String username = "alexey-bakulin404";
        User user = userDetailsService.findByUserName(username);
        System.out.println(user);
    }

    @Test
    public void registerUserTest() throws ServiceException {
        String username = "testUser";
        String firstName = "User";
        String lastName = "Userovich";
        String email = "test.user@mail.com";
        String password = "123qwerty321";
        User user = userDetailsService.register(username,firstName, lastName, email, password);
        System.out.println(user);
    }

    @Test
    public void deleteUserTest() throws ServiceException, IOException {
        String username = "testUser";
        userDetailsService.deleteUser(username);
    }

}
