package com.game.membership.domain.slack.service;

import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.member.enumset.Level;
import com.game.membership.domain.slack.enumset.MessageTemplate;
import com.game.membership.global.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlackServiceTest {

    @Mock
    private Member member;

    @Mock
    private MessageTemplate messageTemplate;

    @InjectMocks
    private SlackService slackService;

    @Test
    @DisplayName("메세지 치환")
    void createSlackMessageSuccess() {
        // given
        when(member.getId()).thenReturn(1L);
        when(member.getName()).thenReturn("허윤영");
        when(member.getLevel()).thenReturn(Level.BRONZE);
        when(messageTemplate.getMessage())
                .thenReturn("ID{member.id}의 {member.name}님이 {member.level}등급으로 가입되었습니다.");

        // when
        String message = slackService.createSlackMessage(member, messageTemplate);

        // then
        String expectedMessage = "ID1의 허윤영님이 BRONZE등급으로 가입되었습니다.";
        assertEquals(expectedMessage, message);
    }

    @Test
    @DisplayName("유저 확인")
    void nullMember() {

        // given
        when(messageTemplate.getMessage())
                .thenReturn("ID{member.id}의 {member.name}님이 {member.level}등급으로 가입되었습니다.");

        // when, then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> slackService.createSlackMessage(null, messageTemplate));
        assertEquals(exception.getMessage(), "가입된 사용자가 아닙니다.");
    }
}