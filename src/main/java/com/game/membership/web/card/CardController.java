package com.game.membership.web.card;

import com.game.membership.domain.card.dto.CardFormDto;
import com.game.membership.domain.card.service.CardService;
import com.game.membership.domain.game.dto.GameListDto;
import com.game.membership.domain.game.service.GameService;
import com.game.membership.global.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.game.membership.global.response.ResultCode.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    private final GameService gameService;

    /**
     * 카드 등록 폼 UI
     *
     * @return 카드 등록 폼
     */
    @GetMapping("/save/{memberId}")
    public String saveCardForm(@PathVariable @ModelAttribute Long memberId, Model model) {
        List<GameListDto> games = gameService.getGameList();
        model.addAttribute("games", games);

        return "card/card_save";
    }

    /**
     * 카드 등록
     *
     * @return 회원 정보 조회
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<ResultResponse> saveCard(CardFormDto dto) {
        try {
            cardService.saveCard(dto);
            return ResponseEntity.ok(new ResultResponse(CARD_SAVE_SUCCESS, "/member/" + dto.getMemberId()));

        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }

    /**
     * 카드 삭제
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<ResultResponse> deleteCard(@PathVariable Long cardId) {
        try {
            cardService.deleteCard(cardId);
            return ResponseEntity.ok(new ResultResponse(CARD_DELETE_SUCCESS, null));

        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }
}
