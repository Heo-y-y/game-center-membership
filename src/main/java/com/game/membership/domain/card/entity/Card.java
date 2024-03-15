package com.game.membership.domain.card.entity;

import com.game.membership.domain.game.entity.Game;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@Table(name = "card_tb")
@NoArgsConstructor
@AllArgsConstructor
public class Card extends BaseTimeEntity {

    @Id
    @Column(name = "card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "serial_number")
    private int serialNumber;

    @Column(precision = 8, scale = 2)
    private BigDecimal price;

}
