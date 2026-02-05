package com.ordep.syncotable.controller;

import com.ordep.syncotable.dto.card.request.UpdateCardRequest;
import com.ordep.syncotable.dto.card.response.CardResponse;
import com.ordep.syncotable.dto.lock.response.LockStatusResponse;
import com.ordep.syncotable.dto.permission.response.PermissionMatrixResponse;
import com.ordep.syncotable.dto.row.request.*;
import com.ordep.syncotable.dto.row.response.RowResponse;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.service.PermissionService;
import com.ordep.syncotable.service.card.CardLockService;
import com.ordep.syncotable.service.card.CardService;
import com.ordep.syncotable.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/card")
@AllArgsConstructor
public class CardController {

    private final CardService cardService;
    private final UserService userService;
    private final PermissionService permissionService;
    private final CardLockService lockService;

    @GetMapping("/{id}")
    public String card(Authentication authentication, Model model, @PathVariable Long id,
            @RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortDirection) {
        long count = cardService.getAccessibleCardsByUser(userService.findUserByEmail(authentication.getName()))
                .stream().filter(c -> c.id().equals(id)).count();

        if (count == 0)
            return "redirect:/home";

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
    public String deleteCard(@PathVariable Long id, @RequestParam(required = true) Long userId) {
        cardService.deleteCardById(id, userId);

        return "redirect:/home";
    }

    @PostMapping("/{id}/row")
    @ResponseBody
    public ResponseEntity<?> createRow(@PathVariable Long id, @RequestBody CreateRowRequest createRowRequest,
            Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());

        if (!lockService.canUserEdit(id, user.getId())) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Card está sendo editado por outro usuário");
        }

        RowResponse row = cardService.createRow(createRowRequest);
        return ResponseEntity.ok(row);
    }

    @PostMapping("/{id}/row/update")
    @ResponseBody
    public ResponseEntity<?> updateRow(@PathVariable Long id, @RequestBody UpdateRowRequest updateRowRequest,
            Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());

        if (!lockService.canUserEdit(id, user.getId())) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Card está sendo editado por outro usuário");
        }

        cardService.updateRow(updateRowRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/row/batch-edit")
    @ResponseBody
    public ResponseEntity<?> updateRowsInBatch(@PathVariable Long id,
            @RequestBody List<RowUnitUpdate> rowUnitUpdateList, Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());

        if (!lockService.canUserEdit(id, user.getId())) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Card está sendo editado por outro usuário");
        }

        cardService.updateInBatch(rowUnitUpdateList);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/row/{rowId}")
    @ResponseBody
    public ResponseEntity<?> deleteRow(@PathVariable Long id, @PathVariable Long rowId, Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());

        if (!lockService.canUserEdit(id, user.getId())) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Card está sendo editado por outro usuário");
        }

        cardService.deleteRow(rowId, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/row/batch-delete")
    @ResponseBody
    public ResponseEntity<?> deleteRowsInBatch(@PathVariable Long id,
            @RequestBody BatchDeleteRowsRequest batchDeleteRowsRequest, Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());

        if (!lockService.canUserEdit(id, user.getId())) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Card está sendo editado por outro usuário");
        }

        cardService.deleteRowsInBatch(batchDeleteRowsRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String importCard(@RequestParam("file") MultipartFile multipartFile,
            @RequestParam(required = true) Long userId) {
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

    @PostMapping("/{id}/lock/acquire")
    @ResponseBody
    public LockStatusResponse acquireLock(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());
        lockService.acquireLock(id, user.getId());
        return lockService.getLockStatus(id, user.getId());
    }

    @PostMapping("/{id}/lock/release")
    @ResponseBody
    public ResponseEntity<Void> releaseLock(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());
        lockService.releaseLock(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/lock/heartbeat")
    @ResponseBody
    public ResponseEntity<Void> heartbeat(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());
        lockService.heartbeat(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/lock/status")
    @ResponseBody
    public LockStatusResponse getLockStatus(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());
        return lockService.getLockStatus(id, user.getId());
    }

}
