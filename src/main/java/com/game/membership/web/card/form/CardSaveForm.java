package com.game.membership.web.card.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardSaveForm {

    @NotNull
    private Long memberId;

    private Long gameId;

    @NotBlank(message = "카드 이름을 입력해주세요.")
    @Size(min = 1, max = 100, message = "카드 이름은 1자 이상 100글자 이하여야합니다.")
    private String title;

    @NotBlank(message = "가격을 입력해주세요.")
    private String price;
}
