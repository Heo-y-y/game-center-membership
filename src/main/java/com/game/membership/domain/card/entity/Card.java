package com.game.membership.domain.card.entity;

import com.game.membership.domain.game.entity.Game;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "card_tb")
@NoArgsConstructor
@AllArgsConstructor
public class Card extends BaseTimeEntity {

    @Id
    @Column(name = "card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "serial_number", nullable = false)
    private int serialNumber;

    @Column(name = "price")
    private double price;

}
