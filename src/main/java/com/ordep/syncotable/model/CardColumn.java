package com.ordep.syncotable.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "card_columns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    @Column(name = "card_key")
    private String key;
    @Column(name = "card_label")
    private String label;
    @Column(name = "card_type")
    private String type;
    @Column(name = "order_index")
    private Integer orderIndex;
    private boolean required = false;
    private boolean visible = true;
    private boolean editable = true;

}
