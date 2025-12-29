package com.ordep.syncotable.service.card;

import com.ordep.syncotable.dto.card.request.UpdateCardRequest;
import com.ordep.syncotable.dto.card.response.CardResponse;
import com.ordep.syncotable.dto.column.response.CardColumnResponse;
import com.ordep.syncotable.dto.row.request.CreateRowRequest;
import com.ordep.syncotable.dto.row.request.UpdateRowRequest;
import com.ordep.syncotable.dto.row.response.RowResponse;
import com.ordep.syncotable.mapper.CardColumnMapper;
import com.ordep.syncotable.mapper.CardMapper;
import com.ordep.syncotable.mapper.CardRowMapper;
import com.ordep.syncotable.model.*;
import com.ordep.syncotable.repository.*;
import com.ordep.syncotable.service.PermissionService;
import com.ordep.syncotable.sheets.SpreadsheetReader;
import com.ordep.syncotable.sheets.SpreadsheetReaderFactory;
import com.ordep.syncotable.sheets.impl.writer.XlsxWriter;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    private final SpreadsheetReaderFactory spreadsheetReaderFactory;
    private final CardColumnMapper cardColumnMapper;
    private final PermissionRepository permissions;
    private final PermissionService permissionService;
    private final RoleRepository roles;

    public List<CardResponse> findAllCards() {
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

    @Transactional
    public void updateCard(Long cardId, UpdateCardRequest updateCardRequest) {
        Card card = findCardById(cardId);

        cardMapper.updateEntity(updateCardRequest, card);

        cards.save(card);
    }

    @Transactional
    public void createColumn(Card card, String key){
        CardColumn column = new CardColumn();
        column.setCard(card);
        column.setLabel(key.toUpperCase(Locale.ROOT));
        column.setKey(key);
        column.setType("TEXT");
        columns.save(column);
    }

    @Transactional
    public CardResponse importSpreadsheet(MultipartFile multipartFile, Long userId) {
        String filename = multipartFile.getOriginalFilename();
        SpreadsheetReader sheet = spreadsheetReaderFactory.getReader(filename);
        try (InputStream is = multipartFile.getInputStream()) {
            List<CardRow> cardRows = sheet.read(is);
            CardResponse cardResponse = createCard(filename, "Adicione uma descrição", userId);
            Card card = findCardById(cardResponse.id());

            permissionService.createPermission(userId, cardResponse.id(), null);

            Map<String,Object> headers = cardRows.get(0).getValuesJson();
            for (String key : headers.keySet()) {
                createColumn(card, key);
            }
            cardRows.forEach(row -> row.setCard(card));
            rows.saveAll(cardRows);
            return cardResponse;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read spreadsheet", e);
        }
    }

    public byte[] exportSpreadsheet(Long cardId) {
        Card card = cards.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Card not found"));
        XlsxWriter xlsxWriter = new XlsxWriter();
        return xlsxWriter.write(card);
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

    @Transactional
    public void deleteCardById(Long id) {
        permissions.deleteByCard_Id(id);

        cards.deleteById(id);
    }

    public void deleteRow(Long rowId) {
        rows.deleteById(rowId);
    }

    public void deleteRowsInBatch(List<Long> rowsId) {
        rows.deleteAllById(rowsId);
    }

    public List<CardResponse> getAccessibleCardsByUser(User user) {
        Role role = roles.findByName("ROLE_ADMIN").orElseThrow(() -> new EntityNotFoundException("Role não encontrado"));
        if (user.getRoles().contains(role)) {
            return cards.findAll().stream().map(cardMapper::toResponse).collect(Collectors.toList());
        }

        return permissions.findByUser(user).stream()
                .filter(Permission::isCanView)
                .map(Permission::getCard)
                .map(cardMapper::toResponse)
                .collect(Collectors.toList());
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