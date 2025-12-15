package com.ordep.syncotable.controller;

import com.ordep.syncotable.dto.user.UserDto;
import com.ordep.syncotable.dto.user.request.UpdateUserRequest;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public String userPage(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("users", userService.findAllUsers());

        return "admin/users";
    }

    @PostMapping("/users")
    public String register(@Valid @RequestBody UserDto dto, BindingResult result, Model model) {

        User user = userService.findUserByEmail(dto.getEmail());

        if (user != null) {
            result.rejectValue("email", null, "Já existe uma conta com este email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", dto);
            return "/admin/users";
        }

        userService.saveUser(dto);
        return "redirect:/admin/users?success";
    }

    @PostMapping("/users/{id}")
    public String update(@Valid @RequestBody UpdateUserRequest dto, BindingResult result, Model model, @PathVariable Long id) {

        userService.updateUser(id, dto);
        return "redirect:/admin/users?success";
    }

    @PostMapping("/users/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users?success";
    }
}
