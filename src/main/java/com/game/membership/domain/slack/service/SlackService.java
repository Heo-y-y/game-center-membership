package com.game.membership.domain.slack.service;

import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.slack.enumset.MessageTemplate;
import com.game.membership.global.error.BusinessException;
import com.game.membership.global.error.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SlackService {
    @Value("${webhook.slack.url}")
    private String SLACK_WEBHOOK_URL;

    /**
     * 슬랙 메시지 전송
     **/
    public ResponseEntity<String> sendMessage(String message) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> request = new HashMap<>();
        request.put("username", "지원자 허윤영");
        request.put("text", message);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request);
        return restTemplate.exchange(SLACK_WEBHOOK_URL, HttpMethod.POST, entity, String.class);

    }

    /**
     * 메시지 치환
     **/
    public String createSlackMessage(Member member, MessageTemplate messageTemplate) {
        String message = messageTemplate.getMessage();

        if (member == null) throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);

        message = message.replace("{member.id}", String.valueOf(member.getId()));
        message = message.replace("{member.name}", member.getName());
        message = message.replace("{member.level}", member.getLevel().name());
        return message;
    }
}
