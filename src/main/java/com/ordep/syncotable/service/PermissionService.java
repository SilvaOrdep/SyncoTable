package com.ordep.syncotable.service;

import com.ordep.syncotable.dto.permission.request.ColumnPermissionEntry;
import com.ordep.syncotable.dto.permission.request.PermissionUpdateRequest;
import com.ordep.syncotable.dto.permission.response.ColumnPermissionEntryResponse;
import com.ordep.syncotable.dto.permission.response.PermissionMatrixResponse;
import com.ordep.syncotable.mapper.PermissionColumnMapper;
import com.ordep.syncotable.mapper.PermissionMapper;
import com.ordep.syncotable.model.*;
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
    private final PermissionColumnMapper permissionColumnMapper;
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

        Permission saved = permissions.save(permission);

        for (CardColumn column : columns.findByCard(card)) {
            permissionColumns.save(PermissionColumn.builder()
                    .canView(true)
                    .canEdit(true)
                    .permission(saved)
                    .cardColumn(column)
                    .build()
            );
        }

        return permissionMapper.toMatrixResponse(saved, findPermissionColumnsByPermission(permission));
    }

    public PermissionMatrixResponse findPermissionByUserIdAndCardId(Long userId, Long cardId) {
        User user = users.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Card card = cards.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Card não encontrado"));

        Permission permission = permissions.findByUserAndCard(user, card).orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada"));

        return permissionMapper.toMatrixResponse(permission, findPermissionColumnsByPermission(permission));
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

        if (request.columnOverrides() != null) {
            for (ColumnPermissionEntry entry : request.columnOverrides()) {
                CardColumn column = columns.findById(entry.columnId()).orElseThrow(() -> new EntityNotFoundException("cardColumn not found"));

                PermissionColumn permissionColumn = permissionColumns.findByCardColumnAndPermission(column, updated).orElseThrow(() -> new EntityNotFoundException("permissionColumn not found"));

                permissionColumnMapper.updateEntity(entry, permissionColumn);

                permissionColumns.save(permissionColumn);
            }
        }

        return permissionMapper.toMatrixResponse(updated, findPermissionColumnsByPermission(updated));
    }

    @Transactional
    public void deletePermission(Long userId, Long cardId) {
        User user = users.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Card card = cards.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card não encontrado"));

        Permission permission = permissions.findByUserAndCard(user, card)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada"));

        permissionColumns.deleteByPermission(permission);

        permissions.delete(permission);
    }

    public List<PermissionMatrixResponse> getPermissionsByUser(Long userId) {
        User user = users.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return permissions.findByUser(user).stream()
                .map(p -> permissionMapper.toMatrixResponse(p, findPermissionColumnsByPermission(p)))
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

    private List<ColumnPermissionEntryResponse> findPermissionColumnsByPermission(Permission permission) {
        return permissionColumns.findByPermission(permission).stream().map(permissionColumnMapper::toResponse).toList();
    }

}
