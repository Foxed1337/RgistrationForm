package ru.zenkov.regform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zenkov.regform.models.User;

@Repository
public interface UserRepositories extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findUserByEmail(String email);
}
