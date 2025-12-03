package com.ordep.syncotable.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    @Column(name = "can_view")
    private boolean canView;
    @Column(name = "can_create")
    private boolean canCreate;
    @Column(name = "can_edit")
    private boolean canEdit;
    @Column(name = "can_delete")
    private boolean canDelete;

}
