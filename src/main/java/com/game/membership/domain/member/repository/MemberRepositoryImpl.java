package com.game.membership.domain.member.repository;

import com.game.membership.domain.member.dto.MemberListConditionDto;
import com.game.membership.domain.member.dto.MemberListDto;
import com.game.membership.domain.member.enumset.Level;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.game.membership.domain.card.entity.QCard.card;
import static com.game.membership.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberListDto> searchAllMembers (MemberListConditionDto condition){
        return queryFactory.select(Projections.fields(
                        MemberListDto.class,
                        member.id,
                        member.name,
                        member.email,
                        member.createdAt,
                        member.level,
                        ExpressionUtils.as(JPAExpressions
                                        .select(card.id.count())
                                        .from(card)
                                        .where(card.member.eq(member)),
                                "cardCount"
                        )
                ))
                .from(member)
                .where(condNameLike(condition.getName()), condLevelLike(condition.getLevel()))
                .orderBy(member.createdAt.desc())
                .fetch();
    }

    private static Predicate condNameLike(String name) {
        return StringUtils.hasText(name) ? member.name.contains(name) : null;
    }

    private static Predicate condLevelLike(Level level) {
        return level != null ? member.level.eq(level) : null;
    }
}