package com.game.membership.domain.card.service;

import com.game.membership.domain.card.dto.CardFormDto;
import com.game.membership.domain.card.dto.CardListDto;
import com.game.membership.domain.card.entity.Card;
import com.game.membership.domain.card.repository.CardRepository;
import com.game.membership.domain.game.entity.Game;
import com.game.membership.domain.game.repository.GameRepository;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.member.repository.MemberRepository;
import com.game.membership.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.game.membership.global.error.ErrorCode.GAME_NOT_FOUND;
import static com.game.membership.global.error.ErrorCode.MEMBER_NOT_FOUND;
import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;

    private static final BigDecimal MAX_PRICE = new BigDecimal("100000");
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;


    @Transactional
    public void saveCard(CardFormDto dto) {
        cardFormValidation(dto);

        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        Game game = gameRepository.findById(dto.getGameId()).orElseThrow(() -> new BusinessException(GAME_NOT_FOUND));
        Card lastCard = cardRepository.findTopByGameOrderByIdDesc(game).orElse(null);

        int serialNumber = lastCard != null ? lastCard.getSerialNumber() + 1 : 1;

        Card card = Card.builder()
                .title(dto.getTitle())
                .game(game)
                .member(member)
                .serialNumber(serialNumber)
                .price(new BigDecimal(dto.getPrice()))
                .build();

        cardRepository.save(card);
    }

    public List<CardListDto> getCards(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        List<Card> cards = cardRepository.findAllByCards(member);
        return cards.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private CardListDto convertToDto(Card card) {
        CardListDto dto = new CardListDto();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setPrice(card.getPrice());
        dto.setGame(card.getGame());
        dto.setSerialNumber(card.getSerialNumber());
        return dto;
    }

    private static void cardFormValidation(CardFormDto dto) {
        BigDecimal price;

        try {
            price = new BigDecimal(dto.getPrice());
        }catch (Exception e) {
            throw new NumberFormatException("숫자만 입력 가능합니다.");
        }
        if (!hasText(dto.getTitle())) {
            throw new IllegalArgumentException("카드 이름을 입력해주세요.");
        }
        if (dto.getTitle().length() < 1 || dto.getTitle().length() >= 100) {
            throw new IllegalArgumentException("카드 이름은 1자 이상 100글자 이하여야합니다.");
        }
        if (price.compareTo(MIN_PRICE) < 0 || price.compareTo(MAX_PRICE) > 0) {
            throw new IllegalArgumentException("가격은 0 이상 100,000 이하여야 합니다.");
        }
    }
}
