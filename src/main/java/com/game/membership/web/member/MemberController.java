package com.game.membership.web.member;

import com.game.membership.domain.member.dto.*;
import com.game.membership.domain.member.service.MemberService;
import com.game.membership.global.response.ResultResponse;
import com.game.membership.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.game.membership.global.response.ResultCode.*;

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
            return ResponseEntity.ok(new ResultResponse(MEMBER_SAVE_SUCCESS, "/member/list"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }

    @GetMapping("/view/{id}")
    public String viewMember(@PathVariable(name = "id") Long id, Model model) {
        MemberDto member = memberService.getMember(id);
        model.addAttribute("member", member);
        return "member/member_update";
    }

    @PatchMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<ResultResponse> updateMember(MemberFormDto dto, @PathVariable(name = "id") Long id) {
        try {
            memberService.updateMember(dto, id);
            return ResponseEntity.ok(new ResultResponse(MEMBER_UPDATE_SUCCESS, "/member/" + id));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }

    @GetMapping("/list")
    public String getMembers(Model model, MemberListConditionDto condition) {
        List<MemberListDto> members = memberService.searchAllMembers(condition);
        model.addAttribute("members", members);
        model.addAttribute("condition", condition);
        return "member/member_list";
    }

    @GetMapping("/{id}")
    public String getMember(@PathVariable Long id, Model model) {
        MemberDto member = memberService.getMember(id);
        model.addAttribute("member", member);
        return "member/member_detail";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok(new ResultResponse(MEMBER_DELETE_SUCCESS, "/member/list"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }
}
