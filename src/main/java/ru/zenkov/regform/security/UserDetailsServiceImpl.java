package ru.zenkov.regform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.zenkov.regform.models.User;
import ru.zenkov.regform.repositories.UserRepositories;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepositories userRepositories;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositories.findByUsername(username);
        if (user != null) {
            return new UserDetailsImpl(user);
        } else throw new UsernameNotFoundException("Пользователя с таким логином нет!");
    }
}
