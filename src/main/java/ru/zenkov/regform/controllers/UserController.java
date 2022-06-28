package ru.zenkov.regform.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.zenkov.regform.models.User;


@Controller
public class UserController {
    @GetMapping("/user-info")
    public String getUserInfo(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "user_page";
    }
}
