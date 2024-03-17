package com.game.membership.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.game.membership.domain.member.enumset.Level;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_tb")
public class Member{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private Level level;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
