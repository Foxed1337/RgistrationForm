package ru.zenkov.regform.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.zenkov.regform.models.User;
import ru.zenkov.regform.repositories.UserRepositories;


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

    /**
     * Добавляем бользователя в базу данных, шифруя его пароль.
     * @param user - пользователь, которого собираемся добавить в БД.
     */
    public void addUser(User user) {
        user.setHashPassword(passwordEncoder.encode(user.getDecodedPassword()));
        userRepositories.save(user);
    }

    /**
     * Проверяем нет ли уже зарегестрированных пользователей с таким же логином или почтой.
     * @param user - пользователь, которого хотим зарегистрировать в системе.
     * @return true - пользователя с токой же почтой или таким же логином нет в системе,
     * false - такой пользователь уже зарегестрирован.
     */
    public boolean checkForSameUsernameAndEmail(User user) {
        User userByUsername = userRepositories.findByUsername(user.getUsername());
        User userByEmail = userRepositories.findUserByEmail(user.getEmail());

        return  userByEmail != null || userByUsername != null;
    }
}
