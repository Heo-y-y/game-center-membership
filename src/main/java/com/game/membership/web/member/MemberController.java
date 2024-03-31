package com.game.membership.web.member;

import com.game.membership.domain.card.dto.CardListDto;
import com.game.membership.domain.card.service.CardService;
import com.game.membership.domain.member.dto.*;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.member.service.MemberService;
import com.game.membership.global.response.ResultResponse;
import com.game.membership.domain.member.dto.MemberDto;
import com.game.membership.web.member.form.MemberSaveForm;
import com.game.membership.web.member.form.MemberUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.game.membership.global.response.ResultCode.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final CardService cardService;

    /**
     * 회원 등록 폼
     *
     * @param model
     * @return "member/save"
     */
    @GetMapping("/save")
    public String saveForm(Model model) {
        model.addAttribute("member", new MemberSaveForm());
        return "member/save";
    }

    /**
     * 회원 등록
     *
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return "redirect:/members"
     */
    @PostMapping("/save")
    public String saveForm(@Validated @ModelAttribute("member") MemberSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        memberService.checkSaveFormValid(form.getCreatedAt(), form.getEmail(), bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "member/save";
        }

         memberService.saveMember(form);

        redirectAttributes.addAttribute("name", "");
        redirectAttributes.addAttribute("level", null);
        return "redirect:/members";
    }

    /**
     * 회원 수정 폼
     *
     * @param id
     * @param model
     * @return "test/update"
     */
    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        Member member = memberService.findMember(id);
        model.addAttribute("member", member);
        return "member/update";
    }

    /**
     * 회원 수정
     *
     * @param id
     * @param form
     * @param bindingResult
     * @return "redirect:/members/{id}"
     */
    @PostMapping("/{id}/update")
    public String updateForm(@PathVariable Long id, @Validated @ModelAttribute("member") MemberUpdateForm form, BindingResult bindingResult) {

        memberService.checkUpdateFormValid(form.getUpdatedAt(), form.getEmail(), id, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "member/update";
        }

        memberService.updateMember(form, id);

        return "redirect:/members/{id}";
    }

    /**
     * 회원 목록
     *
     * @param model
     * @param condition
     * @return "member/members"
     */
    @GetMapping
    public String getMembers(Model model, MemberListConditionDto condition) {
        List<MemberListDto> members = memberService.searchAllMembers(condition);
        model.addAttribute("members", members);
        model.addAttribute("condition", condition);
        return "member/members";
    }

    /**
     * 회원 조회
     *
     * @param id
     * @param model
     * @return "member/member"
     */
    @GetMapping("/{id}")
    public String getMember(@PathVariable Long id, Model model) {
        MemberDto member = memberService.getMember(id);
        List<CardListDto> cards = cardService.getCards(id);
        model.addAttribute("member", member);
        model.addAttribute("cards", cards);
        return "member/member";
    }

    /**
     * 회원 삭제
     *
     * @return 회원 목록
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> delete(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok(new ResultResponse(MEMBER_DELETE_SUCCESS, "/members"));

        } catch (Exception e) {
            return ResponseEntity.ok(new ResultResponse(500, e.getMessage()));
        }
    }
}
