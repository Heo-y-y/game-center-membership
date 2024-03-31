package com.game.membership.web.card;

import com.game.membership.domain.card.service.CardService;
import com.game.membership.domain.game.dto.GameListDto;
import com.game.membership.domain.game.service.GameService;
import com.game.membership.global.response.ResultResponse;
import com.game.membership.web.card.form.CardSaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.game.membership.global.response.ResultCode.CARD_DELETE_SUCCESS;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    private final GameService gameService;

    /**
     * 카드 등록 폼
     *
     * @param memberId
     * @param model
     * @return "test/card_save";
     */
    @GetMapping("/{memberId}/save")
    public String saveForm(@PathVariable @ModelAttribute Long memberId, Model model) {
        List<GameListDto> games = gameService.getGameList();
        model.addAttribute("games", games);
        model.addAttribute("card", new CardSaveForm());

        return "card/save";
    }

    /**
     * 카드 등록
     *
     * @param memberId
     * @param form
     * @param bindingResult
     * @param model
     * @return "redirect:/members/" + memberId
     */
    @PostMapping("/{memberId}/save")
    public String saveForm(@PathVariable Long memberId, @Validated @ModelAttribute("card") CardSaveForm form, BindingResult bindingResult, Model model) {

        cardService.cardSaveFormValid(form, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            List<GameListDto> games = gameService.getGameList();
            model.addAttribute("games", games);
            return "card/save";
        }

        cardService.saveCard(form);

        return "redirect:/members/" + memberId;
    }

    /**
     * 카드 삭제
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<ResultResponse> delete(@PathVariable Long cardId) {
        try {
            cardService.deleteCard(cardId);
            return ResponseEntity.ok(new ResultResponse(CARD_DELETE_SUCCESS, null));

        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }
}
