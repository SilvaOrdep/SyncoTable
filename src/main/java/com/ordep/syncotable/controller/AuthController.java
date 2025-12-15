package com.ordep.syncotable.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
@AllArgsConstructor
public class AuthController {

    @GetMapping
    public String loginForm() {
        return "auth/login";
    }

}
