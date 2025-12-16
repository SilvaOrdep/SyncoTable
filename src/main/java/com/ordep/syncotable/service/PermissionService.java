package com.ordep.syncotable.service;

import com.ordep.syncotable.dto.permission.request.PermissionUpdateRequest;
import com.ordep.syncotable.dto.permission.response.PermissionMatrixResponse;
import com.ordep.syncotable.mapper.PermissionMapper;
import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.Permission;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PermissionService {

    private final PermissionRepository permissions;
    private final PermissionColumnRepository permissionColumns;
    private final UserRepository users;
    private final CardRepository cards;
    private final CardColumnRepository columns;
    private final PermissionMapper permissionMapper;
    private final RoleRepository roles;

    public PermissionMatrixResponse createPermission(Long userId, Long cardId, PermissionUpdateRequest permissionUpdateRequest) {
        User user = users.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Card card = cards.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Card não encontrado"));
        Permission permission;

        if(permissionUpdateRequest != null) {
            permission = permissionMapper.toEntity(permissionUpdateRequest);
            permission.setUser(user);
            permission.setCard(card);
        } else {
            permission = createPermission(user, card);
        }

        return permissionMapper.toMatrixResponse(permissions.save(permission));
    }

    public PermissionMatrixResponse findPermissionByUserIdAndCardId(Long userId, Long cardId) {
        User user = users.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Card card = cards.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Card não encontrado"));

        Permission permission = permissions.findByUserAndCard(user, card).orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada"));

        return permissionMapper.toMatrixResponse(permission);
    }

    @Transactional
    public PermissionMatrixResponse updatePermission(Long userId, Long cardId, PermissionUpdateRequest request) {
        User user = users.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Card card = cards.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card não encontrado"));

        Permission permission = permissions.findByUserAndCard(user, card).orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada"));

        permission.setCanView(request.canView());
        permission.setCanCreate(request.canCreate());
        permission.setCanEdit(request.canEdit());
        permission.setCanDelete(request.canDelete());

        Permission updated = permissions.save(permission);

        return permissionMapper.toMatrixResponse(updated);
    }

    @Transactional
    public void deletePermission(Long userId, Long cardId) {
        User user = users.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Card card = cards.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card não encontrado"));

        Permission permission = permissions.findByUserAndCard(user, card)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada"));

        permissions.delete(permission);
    }

    public List<PermissionMatrixResponse> getPermissionsByUser(Long userId) {
        User user = users.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return permissions.findByUser(user).stream()
                .map(permissionMapper::toMatrixResponse)
                .collect(Collectors.toList());
    }

    private Permission createPermission(User user, Card card) {

        return Permission.builder()
                .canView(true)
                .canEdit(true)
                .canCreate(true)
                .canDelete(true)
                .card(card)
                .user(user)
                .build();

    }

}
