package com.game.membership.domain.card.service;

import com.game.membership.domain.card.dto.CardFormDto;
import com.game.membership.domain.card.dto.CardListDto;
import com.game.membership.domain.card.entity.Card;
import com.game.membership.domain.card.repository.CardRepository;
import com.game.membership.domain.game.entity.Game;
import com.game.membership.domain.game.repository.GameRepository;
import com.game.membership.domain.member.dto.MemberFormDto;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.member.enumset.Level;
import com.game.membership.domain.member.repository.MemberRepository;
import com.game.membership.domain.member.service.MemberService;
import com.game.membership.global.error.BusinessException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CardServiceTest {

    @Autowired
    private CardService cardService;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GameRepository gameRepository;

    private Optional<Member> savedMember;

    @BeforeEach
    public void beforeEach() {
        MemberFormDto member = new MemberFormDto();
        member.setName("testName");
        member.setEmail("test@gmail.com");
        memberService.saveMember(member);
        savedMember = memberRepository.findByEmail(member.getEmail());
    }

    @AfterEach
    public void afterEach() {
        cardRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Nested
    class SaveCard {

        @Test
        @DisplayName("카드 등록")
        void saveCardSuccess() {
            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(savedMember.get().getId());
            dto.setPrice(String.valueOf(new BigDecimal(500)));
            dto.setTitle("티모");

            // when
            cardService.saveCard(dto);
        }

        @Test
        @DisplayName("저장 후 골드 확인")
        void checkSetLevelGold() {
            // given
            CardFormDto dto1 = new CardFormDto();
            dto1.setGameId(2L);
            dto1.setMemberId(savedMember.get().getId());
            dto1.setPrice(String.valueOf(new BigDecimal(500)));
            dto1.setTitle("티모");

            CardFormDto dto2 = new CardFormDto();
            dto2.setGameId(3L);
            dto2.setMemberId(savedMember.get().getId());
            dto2.setPrice(String.valueOf(new BigDecimal(500)));
            dto2.setTitle("리신");

            // when
            cardService.saveCard(dto1);
            cardService.saveCard(dto2);

            // then
            Optional<Member> member = memberRepository.findByEmail(savedMember.get().getEmail());

            assertThat(member.get().getLevel()).isEqualTo(Level.GOLD);
        }

        @Test
        @DisplayName("저장 후 실버 확인")
        void checkSetLevelSilver() {
            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(savedMember.get().getId());
            dto.setPrice(String.valueOf(new BigDecimal(500)));
            dto.setTitle("티모");

            // when
            cardService.saveCard(dto);

            // then
            Optional<Member> member = memberRepository.findByEmail(savedMember.get().getEmail());

            assertThat(member.get().getLevel()).isEqualTo(Level.SILVER);
        }

        @Test
        @DisplayName("저장 후 브론즈 확인")
        void checkSetLevelBronze() {
            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(savedMember.get().getId());
            dto.setPrice(String.valueOf(new BigDecimal(0)));
            dto.setTitle("티모");

            // when
            cardService.saveCard(dto);

            // then
            Optional<Member> member = memberRepository.findByEmail(savedMember.get().getEmail());

            assertThat(member.get().getLevel()).isEqualTo(Level.BRONZE);
        }

        @Test
        @DisplayName("유저 찾기 실패")
        void memberNotFound() {

            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(2L);
            dto.setPrice("500");
            dto.setTitle("리신");

            // when, then
            BusinessException exception = assertThrows(BusinessException.class, () -> cardService.saveCard(dto));
            assertEquals(exception.getMessage(), "가입된 사용자가 아닙니다.");
        }

        @Test
        @DisplayName("게임 찾기 실패")
        void gameNotFound() {

            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(5L);
            dto.setMemberId(savedMember.get().getId());
            dto.setPrice("500");
            dto.setTitle("리신");

            // when, then
            BusinessException exception = assertThrows(BusinessException.class, () -> cardService.saveCard(dto));
            assertEquals(exception.getMessage(), "해당 게임이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카드 이름 공백")
        void cardNameBlank() {

            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(1L);
            dto.setPrice("500");
            dto.setTitle("");

            // when, then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> cardService.saveCard(dto));
            assertEquals("카드 이름을 입력해주세요.", exception.getMessage());
        }

        @Test
        @DisplayName("카드 이름 공백")
        void cardNameFormat() {

            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(1L);
            dto.setPrice("500");
            dto.setTitle("ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ" +
                    "ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ" +
                    "ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ");

            // when, then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> cardService.saveCard(dto));
            assertEquals("카드 이름은 1자 이상 100글자 이하여야합니다.", exception.getMessage());
        }

        @Test
        @DisplayName("가격 0부터 확인")
        void checkCardPrice() {

            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(1L);
            dto.setPrice("1000000");
            dto.setTitle("리신");

            // when, then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> cardService.saveCard(dto));
            assertEquals("가격은 0 이상 100,000 이하여야 합니다.", exception.getMessage());
        }

        @Test
        @DisplayName("숫자 확인")
        void checkCardPriceNumber() {

            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(1L);
            dto.setPrice("숫자");
            dto.setTitle("리신");

            // when, then
            IllegalArgumentException exception = assertThrows(NumberFormatException.class,
                    () -> cardService.saveCard(dto));
            assertEquals("숫자만 입력 가능합니다.", exception.getMessage());
        }
    }

    @Nested
    class GetCards {

        @Test
        @DisplayName("소유 게임카드 목록")
        void getCardsSuccess() {

            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(savedMember.get().getId());
            dto.setPrice(String.valueOf(new BigDecimal(500)));
            dto.setTitle("티모");
            cardService.saveCard(dto);

            // when
            List<CardListDto> cards = cardService.getCards(savedMember.get().getId());

            // then
            assertThat(cards.get(0).getTitle()).isEqualTo("티모");
            assertThat(cards.get(0).getPrice()).isEqualTo("500.00");
        }

        @Test
        @DisplayName("유저 찾기 실패")
        void memberNotFound() {

            // when, then
            BusinessException exception = assertThrows(BusinessException.class, () -> cardService.getCards(1L));
            assertEquals(exception.getMessage(), "가입된 사용자가 아닙니다.");
        }
    }

    @Nested
    class DeleteCard {

        @Test
        @DisplayName("카드 삭제")
        void deleteCardSuccess() {

            // given
            Member member = new Member();
            member.setName("test");
            member.setLevel(Level.BRONZE);
            member.setEmail("test@gmail");
            memberRepository.save(member);

            Game game = new Game();
            game.setName("Test Game");
            gameRepository.save(game);

            Card card = new Card();
            card.setGame(game);
            card.setMember(member);
            card.setTitle("티모");
            card.setPrice(BigDecimal.valueOf(400));
            card.setSerialNumber(4);
            cardRepository.save(card);

            // when
            cardService.deleteCard(card.getId());

            // then
            Optional<Card> deletedCard = cardRepository.findById(card.getId());
            assertThrows(NoSuchElementException.class, deletedCard::get);
        }

        @Test
        @DisplayName("삭제 후 골드 확인")
        void checkSetLevelGold() {
            // given
            CardFormDto dto1 = new CardFormDto();
            dto1.setGameId(2L);
            dto1.setMemberId(savedMember.get().getId());
            dto1.setPrice(String.valueOf(new BigDecimal(100)));
            dto1.setTitle("티모");

            CardFormDto dto2 = new CardFormDto();
            dto2.setGameId(1L);
            dto2.setMemberId(savedMember.get().getId());
            dto2.setPrice(String.valueOf(new BigDecimal(100)));
            dto2.setTitle("리신");

            CardFormDto dto3 = new CardFormDto();
            dto3.setGameId(1L);
            dto3.setMemberId(savedMember.get().getId());
            dto3.setPrice(String.valueOf(new BigDecimal(100)));
            dto3.setTitle("야스오");

            cardService.saveCard(dto1);
            cardService.saveCard(dto2);
            cardService.saveCard(dto3);

            // when
            List<Card> cards = cardRepository.findAllByMember(savedMember.get());
            cardService.deleteCard(cards.get(1).getId());

            // then
            Optional<Member> member = memberRepository.findByEmail(savedMember.get().getEmail());
            assertThat(member.get().getLevel()).isEqualTo(Level.GOLD);
        }

        @Test
        @DisplayName("삭제 후 실버 확인")
        void checkSetLevelSilver() {
            // given
            CardFormDto dto1 = new CardFormDto();
            dto1.setGameId(2L);
            dto1.setMemberId(savedMember.get().getId());
            dto1.setPrice(String.valueOf(new BigDecimal(100)));
            dto1.setTitle("티모");

            CardFormDto dto2 = new CardFormDto();
            dto2.setGameId(1L);
            dto2.setMemberId(savedMember.get().getId());
            dto2.setPrice(String.valueOf(new BigDecimal(100)));
            dto2.setTitle("리신");

            cardService.saveCard(dto1);
            cardService.saveCard(dto2);

            // when
            List<Card> cards = cardRepository.findAllByMember(savedMember.get());
            cardService.deleteCard(cards.get(0).getId());

            // then
            Optional<Member> member = memberRepository.findByEmail(savedMember.get().getEmail());
            assertThat(member.get().getLevel()).isEqualTo(Level.SILVER);
        }

        @Test
        @DisplayName("삭제 후 브론즈 확인")
        void checkSetLevelBronze() {
            // given
            CardFormDto dto = new CardFormDto();
            dto.setGameId(2L);
            dto.setMemberId(savedMember.get().getId());
            dto.setPrice(String.valueOf(new BigDecimal(100)));
            dto.setTitle("티모");
            cardService.saveCard(dto);

            // when
            List<Card> cards = cardRepository.findAllByMember(savedMember.get());
            cardService.deleteCard(cards.get(0).getId());

            // then
            Optional<Member> member = memberRepository.findByEmail(savedMember.get().getEmail());
            assertThat(member.get().getLevel()).isEqualTo(Level.BRONZE);
        }

        @Test
        @DisplayName("카드 찾기 실패")
        void cardNotFound() {
            // When / Then
            assertThrows(BusinessException.class, () -> cardService.deleteCard(1L));
        }
    }
}