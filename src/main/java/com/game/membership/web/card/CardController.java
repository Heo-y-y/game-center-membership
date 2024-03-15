package com.game.membership.web.card;

import com.game.membership.domain.card.dto.CardFormDto;
import com.game.membership.domain.card.dto.MemberCardFormDto;
import com.game.membership.domain.card.dto.MemberCardListDto;
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

    @GetMapping("/save")
    public String saveCardForm(Model model) {
        List<GameListDto> games = gameService.getGames();
        model.addAttribute("games", games);

        return "card/card_save";
    }

    @GetMapping("/save/member/{memberId}")
    public String saveMemberCardForm(@PathVariable @ModelAttribute Long memberId, Model model) {
        List<GameListDto> games = gameService.getGames();
        model.addAttribute("games", games);

        return "card/card_member_save";
    }

    @GetMapping("/cards/{gameId}")
    @ResponseBody
    public List<MemberCardListDto> getCardsByGameId(@PathVariable Long gameId) {
        return cardService.getGameCards(gameId);
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<ResultResponse> saveCard(CardFormDto dto) {
        try {
            cardService.saveCard(dto);
            return ResponseEntity.ok(new ResultResponse(CARD_SAVE_SUCCESS, "/member/list"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }

    @PostMapping("/save/member")
    @ResponseBody
    public ResponseEntity<ResultResponse> saveMemberCard(MemberCardFormDto dto) {
        try {
            cardService.saveMemberCard(dto);
            return ResponseEntity.ok(new ResultResponse(CARD_SAVE_SUCCESS, "/member/" + dto.getMemberId()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }

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
