package com.ordep.syncotable.service.card;

import com.ordep.syncotable.dto.card.response.CardResponse;
import com.ordep.syncotable.dto.column.response.CardColumnResponse;
import com.ordep.syncotable.dto.row.response.RowResponse;
import com.ordep.syncotable.mapper.CardColumnMapper;
import com.ordep.syncotable.mapper.CardMapper;
import com.ordep.syncotable.mapper.CardRowMapper;
import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.CardColumn;
import com.ordep.syncotable.model.CardRow;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.repository.CardColumnRepository;
import com.ordep.syncotable.repository.CardRepository;
import com.ordep.syncotable.repository.CardRowRepository;
import com.ordep.syncotable.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public List<RowResponse> findCardRowsByCardId(Long id, String columnKey, String direction) {
        List<CardRow> cardRowsResponse = new ArrayList<>();

        if (columnKey == null || columnKey.isEmpty()) {
            cardRowsResponse = rows.findByCard(cards.findById(id).orElse(null));
        } else {
            CardColumn cardColumn = columns.findByCardAndKey(cards.findById(id).orElseThrow(() -> new EntityNotFoundException("Card não encontrado")), columnKey).get();

            if (direction == null || direction.isEmpty()) {
                direction = "ASC";
            }

            switch (cardColumn.getType()) {
                case "TEXT":
                    cardRowsResponse = rows.findByCardIdOrderByJsonField(id, columnKey, direction);
                    break;
                case "NUMBER":
                    cardRowsResponse = rows.findByCardIdOrderByJsonFieldNumeric(id, columnKey, direction);
                    break;
                case "DATE":
                    cardRowsResponse = rows.findByCardIdOrderByJsonFieldDate(id, columnKey, direction);
                    break;
            }
        }

        return cardRowsResponse.stream().map(rowMapper::toResponse).toList();
    }

    @Transactional
    public CardResponse createCard(String title, String description, Long userId) {
        User user = users.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não foi encontrado"));
        Card card = new Card();
        card.setTitle(title);
        card.setDescription(description);
        card.setCreatedBy(user);
        return cardMapper.toResponse(card);
    }

    @Transactional
    public void deleteCardById(Long id) {
        cards.deleteById(id);
    }

    public void deleteRow(Long rowId) {
        rows.deleteById(rowId);
    }

    public void deleteRowsInBatch(List<Long> rowsId) {
        rows.deleteAllById(rowsId);
    }

}
