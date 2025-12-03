package com.ordep.syncotable.repository;

import com.ordep.syncotable.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
