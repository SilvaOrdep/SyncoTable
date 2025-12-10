package com.ordep.syncotable.repository;

import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.CardRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRowRepository extends JpaRepository<CardRow, Long> {

    List<CardRow> findByCard(Card card);

    @Query(value = """
        SELECT * FROM card_rows cr
        WHERE cr.card_id = :cardId
        ORDER BY 
            CASE WHEN :direction = 'ASC' THEN 
                CAST(cr.values_json->>:columnKey AS TEXT)
            END ASC,
            CASE WHEN :direction = 'DESC' THEN 
                CAST(cr.values_json->>:columnKey AS TEXT)
            END DESC
        """, nativeQuery = true)
    List<CardRow> findByCardIdOrderByJsonField(
            @Param("cardId") Long cardId,
            @Param("columnKey") String columnKey,
            @Param("direction") String direction
    );

    @Query(value = """
        SELECT * FROM card_rows cr
        WHERE cr.card_id = :cardId
        ORDER BY 
            CASE WHEN :direction = 'ASC' THEN 
                CAST(cr.values_json->>:columnKey AS NUMERIC)
            END ASC,
            CASE WHEN :direction = 'DESC' THEN 
                CAST(cr.values_json->>:columnKey AS NUMERIC)
            END DESC
        """, nativeQuery = true)
    List<CardRow> findByCardIdOrderByJsonFieldNumeric(
            @Param("cardId") Long cardId,
            @Param("columnKey") String columnKey,
            @Param("direction") String direction
    );

    @Query(value = """
        SELECT * FROM card_rows cr
        WHERE cr.card_id = :cardId
        ORDER BY 
            CASE WHEN :direction = 'ASC' THEN 
                CAST(cr.values_json->>:columnKey AS DATE)
            END ASC,
            CASE WHEN :direction = 'DESC' THEN 
                CAST(cr.values_json->>:columnKey AS DATE)
            END DESC
        """, nativeQuery = true)
    List<CardRow> findByCardIdOrderByJsonFieldDate(
            @Param("cardId") Long cardId,
            @Param("columnKey") String columnKey,
            @Param("direction") String direction
    );

}