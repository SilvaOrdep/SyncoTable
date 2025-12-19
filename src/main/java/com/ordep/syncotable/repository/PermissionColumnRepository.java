package com.ordep.syncotable.repository;

import com.ordep.syncotable.model.CardColumn;
import com.ordep.syncotable.model.Permission;
import com.ordep.syncotable.model.PermissionColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionColumnRepository extends JpaRepository<PermissionColumn, Long> {
    List<PermissionColumn> findByPermission(Permission permission);

    Optional<PermissionColumn> findByCardColumnAndPermission(CardColumn cardColumn, Permission permission);

    void deleteByPermission(Permission permission);
}
