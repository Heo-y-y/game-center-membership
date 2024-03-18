package com.game.membership.domain.card.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardFormDto {

    private Long memberId;

    private Long gameId;

    private String title;

    private String price;
}
