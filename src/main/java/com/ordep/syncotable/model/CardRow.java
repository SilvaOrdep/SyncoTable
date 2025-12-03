package com.ordep.syncotable.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "card_rows")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;
    @Type(JsonBinaryType.class)
    @Column(name = "values_json", columnDefinition = "jsonb")
    private Map<String, Object> valuesJson = new HashMap<>();
    @Version
    private Long version;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @lombok.Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt;
    private String status;

}
