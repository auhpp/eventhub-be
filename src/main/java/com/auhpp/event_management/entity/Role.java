package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType type;

    @OneToMany(mappedBy = "role")
    private List<AppUser> appUsers;

    @OneToMany(mappedBy = "role")
    private List<EventStaff> eventStaffs;
}
