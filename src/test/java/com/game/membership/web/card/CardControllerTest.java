package com.game.membership.web.card;

import com.game.membership.domain.card.service.CardService;
import com.game.membership.domain.game.dto.GameListDto;
import com.game.membership.domain.game.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.game.membership.global.response.ResultCode.CARD_DELETE_SUCCESS;
import static com.game.membership.global.response.ResultCode.CARD_SAVE_SUCCESS;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private GameService gameService;

    @Test
    @DisplayName("saveCardForm")
    void saveCardForm() throws Exception {

        // given
        Long memberId = 123L;
        List<GameListDto> games = new ArrayList<>();

        // when
        when(gameService.getGameList()).thenReturn(games);

        // then
        mvc.perform(get("/card/save/{memberId}", memberId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("games"))
                .andExpect(view().name("card/card_save"));
    }

    @Test
    @DisplayName("카드 저장")
    void saveCard() throws Exception {

        // given
        String json = "{\"memberId\": \"1L\", \"gameId\": \"1L\", \"title\": \"게임\", \"price\": \"12\"}";

        // when, then
        mvc.perform(post("/card/save")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())  // HTTP 상태코드 200 확인
                .andExpect(jsonPath("$.status").value(CARD_SAVE_SUCCESS.getStatus()))
                .andExpect(jsonPath("$.message").value(CARD_SAVE_SUCCESS.getMessage()));
    }

    @Test
    @DisplayName("카드 삭제")
    void deleteCard() throws Exception {
        // when, then
        mvc.perform(delete("/card/{cardId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(CARD_DELETE_SUCCESS.getStatus()))
                .andExpect(jsonPath("$.message").value(CARD_DELETE_SUCCESS.getMessage()));
    }
}