package com.ordep.syncotable.controller;

import com.ordep.syncotable.dto.permission.request.PermissionUpdateRequest;
import com.ordep.syncotable.dto.permission.response.PermissionMatrixResponse;
import com.ordep.syncotable.service.PermissionService;
import com.ordep.syncotable.service.card.CardService;
import com.ordep.syncotable.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/permissions")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class PermissionAdminController {

    private final PermissionService permissionService;
    private final UserService userService;
    private final CardService cardService;

    @GetMapping
    public String permissionsPage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("cards", cardService.findAllCards());
        return "admin/permissions";
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public List<PermissionMatrixResponse> getUserPermissions(@PathVariable Long userId) {
        return permissionService.getPermissionsByUser(userId);
    }

    @PostMapping("/user/{userId}/card/{cardId}")
    @ResponseBody
    public PermissionMatrixResponse setPermission(
            @PathVariable Long userId,
            @PathVariable Long cardId,
            @RequestBody PermissionUpdateRequest request
    ) {
        return permissionService.createPermission(userId, cardId, request);
    }

    @PutMapping("/user/{userId}/card/{cardId}")
    @ResponseBody
    public PermissionMatrixResponse updatePermission(
            @PathVariable Long userId,
            @PathVariable Long cardId,
            @RequestBody PermissionUpdateRequest request
    ) {
        return permissionService.updatePermission(userId, cardId, request);
    }

    @DeleteMapping("/user/{userId}/card/{cardId}")
    @ResponseBody
    public void deletePermission(@PathVariable Long userId, @PathVariable Long cardId) {
        permissionService.deletePermission(userId, cardId);
    }
}