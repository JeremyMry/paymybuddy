package com.paymybuddy.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class TransactionController {

    @GetMapping("/transactions")
    public String getAllTransactions(@RequestParam String id) {
        return "transaction";
    }

    @PostMapping("transactions/add")
    public String createTransaction() {
        return "add transaction";
    }
}
