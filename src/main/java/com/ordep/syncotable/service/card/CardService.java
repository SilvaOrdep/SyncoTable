package com.ordep.syncotable.service.card;

import com.ordep.syncotable.dto.card.response.CardResponse;
import com.ordep.syncotable.dto.column.response.CardColumnResponse;
import com.ordep.syncotable.dto.row.response.RowResponse;
import com.ordep.syncotable.mapper.CardColumnMapper;
import com.ordep.syncotable.mapper.CardMapper;
import com.ordep.syncotable.mapper.CardRowMapper;
import com.ordep.syncotable.repository.CardColumnRepository;
import com.ordep.syncotable.repository.CardRepository;
import com.ordep.syncotable.repository.CardRowRepository;
import com.ordep.syncotable.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository cards;
    private final CardMapper cardMapper;
    private final UserRepository users;
    private final CardColumnRepository columns;
    private final CardColumnMapper columnMapper;
    private final CardRowRepository rows;
    private final CardRowMapper rowMapper;


    public List<CardResponse> list() {
        return cards.findAll().stream().map(cardMapper::toResponse).toList();
    }

    public CardResponse findCardById(Long id) {
        return cards.findById(id).map(cardMapper::toResponse).orElseThrow(() -> new EntityNotFoundException("Card não encontrado"));
    }

    public List<CardColumnResponse> findCardColumnsByCardId(Long id) {
        return columns.findByCard(cards.findById(id).orElse(null)).stream().map(columnMapper::toResponse).toList();
    }

    public List<RowResponse> findCardRowsByCardId(Long id) {
        return rows.findByCard(cards.findById(id).orElse(null)).stream().map(rowMapper::toResponse).toList();
    }

    @Transactional
    public void deleteCardById(Long id) {
        cards.deleteById(id);
    }

}
