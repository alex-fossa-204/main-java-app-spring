package by.andersen.intensive.yellow.team.main.repository;

import by.andersen.intensive.yellow.team.main.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserByEmailAddress(String emailAddress);

}
