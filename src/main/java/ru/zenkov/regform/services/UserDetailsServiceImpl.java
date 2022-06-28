package ru.zenkov.regform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.zenkov.regform.models.Message;
import ru.zenkov.regform.models.User;
import ru.zenkov.regform.repositories.UserRepositories;

import java.util.concurrent.TimeoutException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositories userRepositories;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder, UserRepositories userRepositories) {
        this.passwordEncoder = passwordEncoder;
        this.userRepositories = userRepositories;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositories.findByUsername(username);
        if (user != null) {
            return user;
        } else throw new UsernameNotFoundException("Пользователя с таким логином нет!");
    }

    public void addUser(User user) {
        user.setHashPassword(passwordEncoder.encode(user.getDecodedPassword()));
        userRepositories.save(user);
    }

    public boolean checkForSameUsernameAndEmail(User user) {
        User userByUsername = userRepositories.findByUsername(user.getUsername());
        User userByEmail = userRepositories.findUserByEmail(user.getEmail());

        return  userByEmail != null || userByUsername != null;
    }

    public void deleteUser(User user) {
        userRepositories.delete(user);
    }
}
