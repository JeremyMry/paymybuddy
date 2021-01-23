package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.User;
import com.paymybuddy.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller()
public class RegistrationController {

    @Autowired
    UserService userService;

    //@GetMapping("/signUp")
    //public String signUp() {return "login";}

    @GetMapping("/signin")
    public String signIn() {
        return "signin";
    }

    @PostMapping("/signin")
    public String signIn(@RequestParam String email, @RequestParam String password) {
        if (userService.getLoginInfo(email, password)) {
            return "true";
        } else {
            return "false";
        }
    }
}
