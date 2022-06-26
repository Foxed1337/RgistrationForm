package ru.zenkov.regform.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.zenkov.regform.models.User;
import ru.zenkov.regform.repositories.UserRepositories;


@Controller
public class SignUpController {

    @Autowired
    private UserRepositories userRepositories;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signUp")
    public String signUp() {
        return "signup_page";
    }

    @PostMapping("/signUp")
    public String signUpUser(User user, Model model) {
        User userByUsername = userRepositories.findByUsername(user.getUsername());
        User userByEmail = userRepositories.findUserByEmail(user.getEmail());
        if (userByEmail != null || userByUsername != null) {
            model.addAttribute("message", "Пользоватедь с таким логином иди почтой уже существует");
            return "signup_page";
        }
        user.setHashPassword(passwordEncoder.encode(user.getPassword()));
        userRepositories.save(user);
        return "redirect:/signIn";
    }
}
