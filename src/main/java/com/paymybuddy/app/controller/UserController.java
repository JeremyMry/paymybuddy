package com.paymybuddy.app.controller;

import com.paymybuddy.app.data.config.DBConfig;
import org.springframework.web.bind.annotation.*;

@RestController("/paymybuddy/user")
public class UserController {

    public DBConfig dbConfig = new DBConfig();

    @GetMapping("")
    public void getProfile(@RequestParam String lastName, String email) {
    }

    @PostMapping("/paymybuddy/user/add")
    public String addUser() {
        return "add User";
    }

    @PutMapping("/paymybuddy/user/put")
    public String updateUser() {
        return "update user";
    }

    @DeleteMapping("/paymybuddy/user/delete")
    public void deleteUser() {}
}
