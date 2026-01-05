package com.ordep.syncotable.controller;

import com.ordep.syncotable.dto.card.request.UpdateCardRequest;
import com.ordep.syncotable.dto.card.response.CardResponse;
import com.ordep.syncotable.dto.permission.response.PermissionMatrixResponse;
import com.ordep.syncotable.dto.row.request.BatchDeleteRowsRequest;
import com.ordep.syncotable.dto.row.request.CreateRowRequest;
import com.ordep.syncotable.dto.row.request.UpdateRowRequest;
import com.ordep.syncotable.dto.row.response.RowResponse;
import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.service.PermissionService;
import com.ordep.syncotable.service.card.CardService;
import com.ordep.syncotable.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping("/card")
@AllArgsConstructor
public class CardController {

    private final CardService cardService;
    private final UserService userService;
    private final PermissionService permissionService;

    @GetMapping("/{id}")
    public String card(Authentication authentication, Model model, @PathVariable Long id, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortDirection) {
        long count = cardService.getAccessibleCardsByUser(userService.findUserByEmail(authentication.getName())).stream().filter(c -> c.id().equals(id)).count();
        if (count == 0) return "redirect:/home";
        model.addAttribute("card", cardService.findCard(id));
        model.addAttribute("columns", cardService.findCardColumnsByCardId(id));
        model.addAttribute("rows", cardService.findCardRowsByCardId(id, sortBy, sortDirection));
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("userId", userService.findUserByEmail(authentication.getName()).getId());

        return "card/card";
    }

    @PutMapping("{id}")
    public String updateCard(@PathVariable Long id, UpdateCardRequest updateCardRequest) {
        cardService.updateCard(id, updateCardRequest);

        return "redirect:/home";
    }

    @DeleteMapping("/{id}")
    public String deleteCard(@PathVariable Long id) {
        cardService.deleteCardById(id);

        return "redirect:/home";
    }

    @PostMapping("/{id}/row")
    public String createRow(@PathVariable Long id, @RequestBody CreateRowRequest createRowRequest) {
        RowResponse row = cardService.createRow(createRowRequest);

        return "redirect:/card/" + id;
    }

    @PostMapping("/{id}/row/update")
    public String updateRow(@PathVariable Long id, @RequestBody UpdateRowRequest updateRowRequest) {
        cardService.updateRow(updateRowRequest);

        return "redirect:/card/" + id;
    }

    @DeleteMapping("/{id}/row/{rowId}")
    public String deleteRow(@PathVariable Long id, @PathVariable Long rowId) {
        cardService.deleteRow(rowId);

        return "redirect:/card/" + id;
    }

    @PostMapping("/{id}/row/batch-delete")
    public String deleteRowsInBatch(@PathVariable Long id, @RequestBody BatchDeleteRowsRequest batchDeleteRowsRequest) {
        cardService.deleteRowsInBatch(batchDeleteRowsRequest.rowIds());

        return "redirect:/card/" + id;
    }


    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String importCard(@RequestParam("file") MultipartFile
                                     multipartFile, @RequestParam(required = true) Long userId) {
        CardResponse card = cardService.importSpreadsheet(multipartFile, userId);

        return "redirect:/card/" + card.id();
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportCard(@PathVariable Long id) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + cardService.findCard(id).title() + ".xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(cardService.exportSpreadsheet(id));
    }


    @GetMapping("/{id}/permissions")
    @ResponseBody
    public PermissionMatrixResponse getMyCardPermissions(Authentication authentication, @PathVariable Long id) {
        User currentUser = userService.findUserByEmail(authentication.getName());
        return permissionService.findPermissionByUserIdAndCardId(currentUser.getId(), id);
    }

}



