package com.paymybuddy.app.controller;

import com.paymybuddy.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller()
public class RegistrationController {

    @Autowired
    UserService userService;

    @GetMapping("/signin")
    public String signIn() {
        return "signin";
    }

    @GetMapping("/login")
    public String signIn(@RequestParam String email, String password) {
        if (userService.getLoginInfo(email, password)) {
            return "signup";
        } else {
            return "404";
        }
    }
}
