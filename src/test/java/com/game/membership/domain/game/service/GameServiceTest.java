package com.game.membership.domain.game.service;

import com.game.membership.domain.game.dto.GameListDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    @DisplayName("getGameListSuccess")
    void getGameList() {

        List<GameListDto> gameList = gameService.getGameList();

        Assertions.assertThat(gameList.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(gameList.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(gameList.get(2).getId()).isEqualTo(3L);
        Assertions.assertThat(gameList.get(0).getName()).isEqualTo("매직 더 게더링");
        Assertions.assertThat(gameList.get(1).getName()).isEqualTo("유희왕");
        Assertions.assertThat(gameList.get(2).getName()).isEqualTo("포켓몬");
    }
}