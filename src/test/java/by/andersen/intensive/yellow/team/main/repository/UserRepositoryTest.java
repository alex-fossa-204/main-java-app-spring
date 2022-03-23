package by.andersen.intensive.yellow.team.main.repository;

import by.andersen.intensive.yellow.team.main.model.entity.Privilege;
import by.andersen.intensive.yellow.team.main.model.entity.Report;
import by.andersen.intensive.yellow.team.main.model.entity.Role;
import by.andersen.intensive.yellow.team.main.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void findAllUsersTest() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            Set<Role> roles = user.getRoles();
            roles.forEach(role -> {
                Set<Privilege> privileges = role.getPrivileges();
                privileges.forEach(privilege -> System.out.println(privilege));
            });
            System.out.println(user.getUsername() + " " + roles);
            Set<Report> reports = user.getReports();
            reports.forEach(report -> {
                System.out.println( "\t" + report);
            });
            System.out.println("\n");
        });
    }

    @Test
    public void findUserByUserNameTest() {
        User user = userRepository.findUserByUsername("alexey-bakulin404");
        System.out.println(user);
    }

    @Test
    public void findUserByEmailAddressTest() {
        User user = userRepository.findUserByEmailAddress("alexey.bakulin@mail.com");
        System.out.println(user);
    }

}
