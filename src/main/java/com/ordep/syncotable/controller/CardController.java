package com.ordep.syncotable.controller;

import com.ordep.syncotable.service.card.CardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/card")
@AllArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/{id}")
    public String card(Model model, @PathVariable Long id) {
        model.addAttribute("card", cardService.findCardById(id));
        model.addAttribute("columns", cardService.findCardColumnsByCardId(id));
        model.addAttribute("rows", cardService.findCardRowsByCardId(id));

        return "card/card";
    }

    @PostMapping("/{id}")
    public String card(@PathVariable Long id) {
        cardService.deleteCardById(id);

        return "redirect:/home";
    }

}
