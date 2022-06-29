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
import java.util.logging.Logger;


@Controller
public class SignUpController {

    private final UserDetailsServiceImpl userDetailsService;

    private final RegistrationService registrationService;

    private final Logger log = Logger.getLogger(this.getClass().getName());

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
        //Проверяем уникальность пользователья
        if (userDetailsService.checkForSameUsernameAndEmail(user)) {
            model.addAttribute("message", "Пользователь с таким логином или почтой уже существует");
            log.info(String
                    .format(
                            "Пользователь %s c почтой %s, не зарегистрирован, т.к. уже есть пользователь с таким логином или почтой уже существует"
                            , user.getUsername(), user.getEmail()));
            return "signup_page";
        }

        try {
            //Пытаемся зарегистрировать пользователья,
            // если внешняя система одобряет регистрацию, то добавляем пользователья в БД
            if (registrationService.registrationUser(user)) {
                userDetailsService.addUser(user);
                log.info(String.format("Пользователь %s, зарегистрирован и добавлен в БД", user.getUsername()));
            } else {
                log.info(String.format("Пользователь %s, не зарегистрирован", user.getUsername()));
            }
        } catch (TimeoutException e) {
            //Обрабатываем случаей, если сервис регистрации или отправки почты не доступны
            model.addAttribute("message",
                    "Сервис регистрации сейчас не доступен, повторите попытку позже");
            return "signup_page";
        }
        model.addAttribute("message",
                "Ваша заявка на регистрацию рассматривается, сообщение с результатом регистрации придет на почту");
        return "signup_page";

    }
}
