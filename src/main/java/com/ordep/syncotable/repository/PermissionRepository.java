package com.ordep.syncotable.repository;

import com.ordep.syncotable.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
