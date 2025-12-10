package com.ordep.syncotable.controller;

import com.ordep.syncotable.dto.row.request.BatchDeleteRowsRequest;
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
    public String card(Model model, @PathVariable Long id, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortDirection) {
        model.addAttribute("card", cardService.findCardById(id));
        model.addAttribute("columns", cardService.findCardColumnsByCardId(id));
        model.addAttribute("rows", cardService.findCardRowsByCardId(id, sortBy, sortDirection));
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);

        return "card/card";
    }

    @PostMapping("/{id}")
    public String deleteCard(@PathVariable Long id) {
        cardService.deleteCardById(id);

        return "redirect:/home";
    }

    @PostMapping("/{id}/row/{rowId}")
    public String deleteRow(@PathVariable Long id, @PathVariable Long rowId) {
        cardService.deleteRow(rowId);

        return "redirect:/card/" + id;
    }

    @PostMapping("/{id}/row/batch-delete")
    public String deleteRowsInBatch(@PathVariable Long id, @RequestBody BatchDeleteRowsRequest batchDeleteRowsRequest) {
        cardService.deleteRowsInBatch(batchDeleteRowsRequest.rowIds());

        return "redirect:/card/" + id;
    }

}
