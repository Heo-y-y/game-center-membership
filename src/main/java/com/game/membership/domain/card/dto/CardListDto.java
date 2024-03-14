package com.game.membership.domain.card.dto;

import com.game.membership.domain.game.entity.Game;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CardListDto {

    private Long id;
    private Game game;
    private String title;
    private int serialNumber;
    private BigDecimal price;
}
