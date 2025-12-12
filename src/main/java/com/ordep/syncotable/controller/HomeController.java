package com.ordep.syncotable.controller;

import com.ordep.syncotable.service.card.CardService;
import com.ordep.syncotable.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    private final CardService cardService;
    private final UserService userService;

    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/home";
        }
        return "redirect:auth/login";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("title", "Home");
        model.addAttribute("cards", cardService.list());
        model.addAttribute("userId", userService.findUserByEmail(authentication.getName()).getId());
        return "home";
    }

}
