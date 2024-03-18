package com.game.membership.domain.card.repository;

import com.game.membership.domain.card.entity.Card;
import com.game.membership.domain.game.entity.Game;
import com.game.membership.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * 게임 중복 확인
     */
    @Query("SELECT count(DISTINCT c.game) FROM Card c WHERE c.member = :member")
    Long countDistinctGamesByMember(Member member);

    List<Card> findByMemberOrderByIdDesc(Member member);

    List<Card> findAllByMember(Member member);

    boolean existsByGameAndSerialNumber(Game game, int serialNumber);
}
