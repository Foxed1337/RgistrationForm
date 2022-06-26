package ru.zenkov.regform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import ru.zenkov.regform.models.User;
import ru.zenkov.regform.security.UserDetailsImpl;

@Controller
public class UserController {

    @GetMapping("/user-info")
    public String getUserInfo(Model model, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        model.addAttribute("user",  user);
        return "user_page";
    }
}
