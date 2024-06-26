package com.game.membership.domain.member.repository;

import com.game.membership.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>,
        QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
