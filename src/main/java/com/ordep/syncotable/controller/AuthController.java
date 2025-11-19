package com.ordep.syncotable.controller;

import com.ordep.syncotable.dto.UserDto;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") UserDto dto, BindingResult result, Model model) {

        User user = userService.findUserByEmail(dto.getEmail());

        if (user != null) {
            result.rejectValue("email", null, "Já existe uma conta com este email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", dto);
            return "register";
        }

        userService.saveUser(dto);
        return "redirect:/login?success";
    }

}
