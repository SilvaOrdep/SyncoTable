package com.ordep.syncotable.repository;

import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.Permission;
import com.ordep.syncotable.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByUserAndCard(User user, Card card);

    List<Permission> findByUser(User user);

    void deleteByCard_Id(Long cardId);
}
