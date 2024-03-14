package com.game.membership.domain.card.repository;

import com.game.membership.domain.card.entity.Card;
import com.game.membership.domain.game.entity.Game;
import com.game.membership.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c FROM Card c WHERE c.game = :game ORDER BY c.id desc limit 1")
    Optional<Card> findTopByGameOrderByIdDesc(Game game);

    @Query("SELECT c FROM Card c WHERE c.member = :member ORDER BY c.id desc")
    List<Card> findAllByCards(Member member);
}
