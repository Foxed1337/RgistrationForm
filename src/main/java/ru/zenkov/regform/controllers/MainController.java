package ru.zenkov.regform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping()
    public String main() {
        System.out.println("Main test");
        return "main";
    }
}
