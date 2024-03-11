package com.game.membership.web.member;

import com.game.membership.domain.member.dto.MemberFormDto;
import com.game.membership.domain.member.service.MemberService;
import com.game.membership.global.response.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.game.membership.global.response.ResultCode.MEMBER_SAVE_SUCCESS;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/save")
    public String saveMemberForm() {
        return "member/member_save";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<ResultResponse> saveMember(MemberFormDto dto) {
        try {
            memberService.saveMember(dto);
            return ResponseEntity.ok(new ResultResponse(MEMBER_SAVE_SUCCESS, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }
}
