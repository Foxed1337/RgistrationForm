package ru.zenkov.regform.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.zenkov.regform.models.User;
import ru.zenkov.regform.services.RegistrationService;
import ru.zenkov.regform.services.UserDetailsServiceImpl;

import java.util.concurrent.TimeoutException;


@Controller
public class SignUpController {

    private final UserDetailsServiceImpl userDetailsService;

    private final RegistrationService registrationService;

    public SignUpController(UserDetailsServiceImpl userDetailsService, RegistrationService registrationService) {
        this.userDetailsService = userDetailsService;
        this.registrationService = registrationService;
    }
    @GetMapping("/signUp")
    public String signUp() {
        return "signup_page";
    }

    @PostMapping("/signUp")
    public String signUpUser(User user, Model model) {

        if (userDetailsService.checkForSameUsernameAndEmail(user)) {
            model.addAttribute("message", "Пользователь с таким логином или почтой уже существует");
            return "signup_page";
        }

        try {
            if (registrationService.registrationUser(user)) {
                userDetailsService.addUser(user);
                System.out.println("Добавили пользователя");
            } else {
                System.out.println("Не добавили пользователя");
            }
        } catch (TimeoutException e) {
            model.addAttribute("message",
                    "Сервис регистрации сейчас не доступен, повторите попытку позже");
            return "signup_page";
        }
        model.addAttribute("message",
                "Ваша заявка на регистрацию рассматривается, сообщение с результатом регистрации придет на почту");
        return "signup_page";

    }
}
