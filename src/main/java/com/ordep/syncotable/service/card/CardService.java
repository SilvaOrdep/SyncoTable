package com.ordep.syncotable.service.card;

import com.ordep.syncotable.dto.card.response.CardResponse;
import com.ordep.syncotable.dto.column.response.CardColumnResponse;
import com.ordep.syncotable.dto.row.request.CreateRowRequest;
import com.ordep.syncotable.dto.row.request.UpdateRowRequest;
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
import com.ordep.syncotable.sheets.Spreadsheet;
import com.ordep.syncotable.sheets.SpreadsheetFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final SpreadsheetFactory spreadsheetFactory;


    public List<CardResponse> list() {
        return cards.findAll().stream().map(cardMapper::toResponse).toList();
    }

    public CardResponse findCard(Long id) {
        return cardMapper.toResponse(findCardById(id));
    }

    public List<CardColumnResponse> findCardColumnsByCardId(Long id) {
        return columns.findByCard(findCardById(id)).stream().map(columnMapper::toResponse).toList();
    }

    public List<RowResponse> findCardRowsByCardId(Long id, String columnKey, String direction) {
        List<CardRow> cardRowsResponse = new ArrayList<>();

        if (columnKey == null || columnKey.isEmpty()) {
            cardRowsResponse = rows.findByCard(findCardById(id));
        } else {
            CardColumn cardColumn = columns.findByCardAndKey(findCardById(id), columnKey).get();

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
        return cardMapper.toResponse(cards.save(card));
    }

    //Problema no createColumn
//    @Transactional
//    public CardResponse createColumn(Card card, CardRow cardRow){
//        CardColumn column = new CardColumn();
//        column.setCard(card);
//
//        column.setKey();
//        columns.save(column);
//        return null;
//    }

    @Transactional
    public CardResponse importSpreadsheet(MultipartFile multipartFile, Long userId) {
        //ver se é melhor criar as colunas direto daqui ou se seria uma boa fazer cada coluna dentro do handler
        String filename = multipartFile.getOriginalFilename();
        Spreadsheet sheet = spreadsheetFactory.getHandler(filename);
        try (InputStream is = multipartFile.getInputStream()) {
            List<CardRow> cardRows = sheet.read(is);
            CardResponse card = createCard(filename, "", userId);
            cardRows.forEach(row -> row.setCard(findCardById(card.id())));
            rows.saveAll(cardRows);
            return card;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read spreadsheet", e);
        }
    }

    public RowResponse createRow(CreateRowRequest createRowRequest) {
        Card card = findCardById(createRowRequest.cardId());
        User user = users.findById(createRowRequest.userId()).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        CardRow row = CardRow.builder().status("ACTIVE").card(card).createdBy(user).valuesJson(createRowRequest.values()).version(0L).updatedAt(Instant.now()).build();

        return rowMapper.toResponse(rows.save(row));
    }

    public RowResponse updateRow(UpdateRowRequest updateRowRequest) {
        CardRow cardRow = rows.findById(updateRowRequest.rowId()).orElseThrow(() -> new EntityNotFoundException("Linha não encontrada"));

        if (!cardRow.getVersion().equals(updateRowRequest.version()))
            throw new RuntimeException("Erro de incompatibilidade de versões da linha atual");

        validateAndUpdateValues(cardRow, updateRowRequest.values());

        cardRow.setUpdatedAt(Instant.now());
        cardRow.setVersion(cardRow.getVersion() + 1);
        return rowMapper.toResponse(rows.save(cardRow));
    }

    public void deleteCardById(Long id) {
        cards.deleteById(id);
    }

    public void deleteRow(Long rowId) {
        rows.deleteById(rowId);
    }

    public void deleteRowsInBatch(List<Long> rowsId) {
        rows.deleteAllById(rowsId);
    }

    private Card findCardById(Long id) {
        return cards.findById(id).orElseThrow(() -> new EntityNotFoundException("Card not found"));
    }

    private void validateAndUpdateValues(CardRow row, Map<String, Object> newValues) {
        var cardColumnMap = columns.findByCard(row.getCard()).stream().collect(Collectors.toMap(CardColumn::getKey, col -> col));

        for (String key : row.getValuesJson().keySet()) {

            Object newValue = newValues.get(key);

            if (newValue == null || newValue.toString().isEmpty()) {
                continue;
            }

            CardColumn col = cardColumnMap.get(key);
            if (col != null) {
                try {
                    Object validatedValue = validateType(newValue, col.getType());
                    row.getValuesJson().put(key, validatedValue);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(String.format("Campo '%s' inválido: %s", col.getLabel(), e.getMessage()));
                }
            }

        }
    }

    private Object validateType(Object value, String type) {
        try {
            return switch (type) {
                case "NUMERIC" -> value = Double.parseDouble(value.toString());
                case "DATE" -> value = LocalDate.parse(value.toString());
                case "TEXT" -> value = value.toString();
                default -> value;
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("Valor inválido para tipo: " + type);
        }

    }

}