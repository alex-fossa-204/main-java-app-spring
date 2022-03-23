package by.andersen.intensive.yellow.team.main.repository;

import by.andersen.intensive.yellow.team.main.model.entity.Privilege;
import by.andersen.intensive.yellow.team.main.model.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void findAllRolesTest() {
        List<Role> roles = roleRepository.findAll();
        roles.forEach(role -> {
            System.out.println(role);
        });
    }

    @Test
    public void findRoleByRoleNameTest() {
        Role role = roleRepository.findRoleByRoleName("ADMIN");
        Set<Privilege> privileges = role.getPrivileges();
        System.out.println(role);
        System.out.println(privileges);
    }
}
