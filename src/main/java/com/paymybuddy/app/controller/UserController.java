package com.paymybuddy.app.controller;

import com.paymybuddy.app.data.config.DBConfig;
import org.springframework.web.bind.annotation.*;

@RestController()
public class UserController {

    public DBConfig dbConfig = new DBConfig();

    @GetMapping("/profile")
    public void getProfile(@RequestParam String lastName, String email) {
    }

    @PostMapping("profile/add")
    public String addUser() {
        return "add User";
    }

    @PutMapping("profile/put")
    public String updateUser() {
        return "update user";
    }

    @DeleteMapping("profile/delete")
    public void deleteUser() {}
}
