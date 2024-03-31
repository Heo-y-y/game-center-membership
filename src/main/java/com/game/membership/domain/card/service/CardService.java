package com.game.membership.domain.card.service;

import com.game.membership.domain.card.dto.CardListDto;
import com.game.membership.domain.card.entity.Card;
import com.game.membership.domain.card.repository.CardRepository;
import com.game.membership.domain.game.entity.Game;
import com.game.membership.domain.game.repository.GameRepository;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.member.enumset.Level;
import com.game.membership.domain.member.repository.MemberRepository;
import com.game.membership.domain.slack.enumset.MessageTemplate;
import com.game.membership.domain.slack.service.SlackService;
import com.game.membership.global.error.BusinessException;
import com.game.membership.web.card.form.CardSaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.game.membership.global.error.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;
    private final SlackService slackService;

    private static final BigDecimal MAX_PRICE = new BigDecimal("100000");
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    /**
     * 카드 등록
     *
     * @param form
     */
    @Transactional
    public void saveCard(CardSaveForm form) {

        Member member = memberRepository.findById(form.getMemberId()).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        Game game = gameRepository.findById(form.getGameId()).orElseThrow(() -> new BusinessException(GAME_NOT_FOUND));

        Card card = Card.builder()
                .title(form.getTitle())
                .game(game)
                .member(member)
                .serialNumber(this.genRandomNumber(game))
                .price(new BigDecimal(form.getPrice()))
                .createdAt(LocalDate.now())
                .build();

        cardRepository.save(card);

        this.setLevel(card.getMember(), MessageTemplate.UP);
    }

    /**
     * 카드 목록
     *
     * @param memberId
     */
    public List<CardListDto> getCards(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        List<Card> cards = cardRepository.findByMemberOrderByIdDesc(member);

        return cards.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * 카드 삭제
     *
     * @param id
     */
    @Transactional
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CARD_NOT_FOUND));

        cardRepository.delete(card);

        this.setLevel(card.getMember(), MessageTemplate.DOWN);
    }

    /**
     * 카드 등록 폼 복합 유효성
     *
     * @param form
     * @param bindingResult
     */
    public void cardSaveFormValid(CardSaveForm form, BindingResult bindingResult) {

        if (form.getGameId() == null || !gameRepository.findById(form.getGameId()).isPresent()) {
            bindingResult.rejectValue("gameId", "error.card", "게임을 선택해주세요.");
        }

        BigDecimal price;

        try {
            if (form.getPrice() == null) {
                bindingResult.rejectValue("price", "error.card", "가격을 입력해주세요.");
            }

            price = new BigDecimal(form.getPrice());

            if (price.compareTo(MIN_PRICE) < 0 || price.compareTo(MAX_PRICE) > 0) {
                bindingResult.rejectValue("price", "error.card", "가격은 0 이상 100,000 이하여야 합니다.");
            }

        } catch (Exception e) {
            bindingResult.rejectValue("price", "error.card", "숫자만 입력 가능합니다.");
        }
    }

    /**
     * 레벨 변경
     *
     * @param member
     * @param messageTemplate
     */
    private void setLevel(Member member, MessageTemplate messageTemplate) {
        List<CardListDto> cards = this.getCards(member.getId());
        Long gameCount = cardRepository.countDistinctGamesByMember(member);
        long differentGameCount = gameCount != null ? gameCount : 0;

        List<CardListDto> validGameCard = cards.stream()
                .filter(card -> card.getPrice().compareTo(BigDecimal.ZERO) > 0)
                .toList();

        BigDecimal totalPrice = validGameCard.stream()
                .map(CardListDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Level beforeLevel = member.getLevel();
        Level afterLevel;

        if (differentGameCount >= 2 && (validGameCard.size() >= 4 ||
                validGameCard.size() >= 2 && validGameCard.size() <= 3 &&
                        totalPrice.compareTo(new BigDecimal(100)) > 0)) {
            afterLevel = Level.GOLD;
        } else if (!validGameCard.isEmpty()) {
            afterLevel = Level.SILVER;
        } else {
            afterLevel = Level.BRONZE;
        }

        if (afterLevel != beforeLevel) {
            member.setLevel(afterLevel);
            slackService.sendMessage(slackService.createSlackMessage(member, messageTemplate));
        }
    }

    /**
     * 카드 일련 번호 생성
     *
     * @param game
     * @return serialNumber
     */
    private int genRandomNumber(Game game) {
        int serialNumber = 0;

        while (true) {
            serialNumber = (int) (Math.random() * 1000000);
            boolean cardExists = cardRepository.existsByGameAndSerialNumber(game, serialNumber);

            if (!cardExists) {
                break;
            }
        }
        return serialNumber;
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


}
