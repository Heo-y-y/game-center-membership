package com.game.membership.web.game;

import com.game.membership.domain.game.dto.GameListDto;
import com.game.membership.domain.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    @GetMapping
    public String getGameList(Model model) {
        List<GameListDto> games = gameService.getGameList();
        model.addAttribute("games", games);

        return "game/game_list";
    }
}
