package com.ordep.syncotable.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @lombok.Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    @ManyToOne
    @JoinColumn(name = "locked_by")
    private User lockedBy;
    @Column(name = "locked_at")
    private Instant lockedAt;

}
