package com.ordep.syncotable.repository;

import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.CardRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRowRepository extends JpaRepository<CardRow, Long> {

    List<CardRow> findByCard(Card card);

}
