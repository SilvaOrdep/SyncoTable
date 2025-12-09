package com.ordep.syncotable.repository;

import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.CardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardColumnRepository extends JpaRepository<CardColumn, Long> {
    
    List<CardColumn> findByCard(Card card);
    
}
