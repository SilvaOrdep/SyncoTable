package com.ordep.syncotable.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permission_columns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_column_id")
    private CardColumn cardColumn;
    @Column(name = "can_view")
    private boolean canView;
    @Column(name = "can_edit")
    private boolean canEdit;

}
