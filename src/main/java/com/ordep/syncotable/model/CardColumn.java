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
    private String key;
    private String label;
    private String type;
    @Column(name = "order_index")
    private Integer orderIndex;
    private boolean required = false;
    private boolean visible = true;
    private boolean editable = true;

}
