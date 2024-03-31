package com.game.membership.domain.game.service;

import com.game.membership.domain.game.dto.GameListDto;
import com.game.membership.domain.game.entity.Game;
import com.game.membership.domain.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    /**
     * 게임 목록
     */
    public List<GameListDto> getGameList() {
        List<Game> games = gameRepository.findAll(Sort.by("id"));

        return games.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private GameListDto convertToDto(Game game) {
        GameListDto dto = new GameListDto();
        dto.setId(game.getId());
        dto.setName(game.getName());

        return dto;
    }
}
