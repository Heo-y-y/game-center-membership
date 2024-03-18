package com.game.membership.web.member;

import com.game.membership.domain.card.dto.CardListDto;
import com.game.membership.domain.card.service.CardService;
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
    private final CardService cardService;

    /**
     * 회원 등록 UI
     *
     * @return 회원 등록 폼
     */
    @GetMapping("/save")
    public String saveMemberForm() {
        return "member/member_save";
    }

    /**
     * 회원 등록
     *
     * @return 회원 목록
     */
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

    /**
     * 회원 수정 UI
     *
     * @return 회원 수정 폼
     */
    @GetMapping("/update/{id}")
    public String viewMember(@PathVariable(name = "id") Long id, Model model) {
        MemberDto member = memberService.getMember(id);
        model.addAttribute("member", member);
        return "member/member_update";
    }

    /**
     * 회원 수정
     *
     * @return 회원 정보 조회
     */
    @PatchMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResultResponse> updateMember(MemberFormDto dto, @PathVariable(name = "id") Long id) {
        try {
            memberService.updateMember(dto, id);
            return ResponseEntity.ok(new ResultResponse(MEMBER_UPDATE_SUCCESS, "/member/" + id));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }

    /**
     * 회원 목록 UI
     *
     * @return 회원 목록
     */
    @GetMapping("/list")
    public String getMembers(Model model, MemberListConditionDto condition) {
        List<MemberListDto> members = memberService.searchAllMembers(condition);
        model.addAttribute("members", members);
        model.addAttribute("condition", condition);
        return "member/member_list";
    }

    /**
     * 회원 정보 UI
     *
     * @return 회원 정보
     */
    @GetMapping("/{id}")
    public String getMember(@PathVariable Long id, Model model) {
        MemberDto member = memberService.getMember(id);
        List<CardListDto> cards = cardService.getCards(id);
        model.addAttribute("member", member);
        model.addAttribute("cards", cards);
        return "member/member_detail";
    }

    /**
     * 회원 삭제
     *
     * @return 회원 목록
     */
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
