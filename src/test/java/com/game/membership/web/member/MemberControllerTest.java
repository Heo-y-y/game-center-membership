package com.game.membership.web.member;

import com.game.membership.domain.member.dto.MemberDto;
import com.game.membership.domain.member.dto.MemberFormDto;
import com.game.membership.domain.member.service.MemberService;
import com.game.membership.global.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.game.membership.global.error.ErrorCode.EMAIL_ALREADY_EXIST;
import static com.game.membership.global.error.ErrorCode.MEMBER_NOT_FOUND;
import static com.game.membership.global.response.ResultCode.MEMBER_SAVE_SUCCESS;
import static com.game.membership.global.response.ResultCode.MEMBER_UPDATE_SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private WebApplicationContext ctx;

    @Nested
    class SaveMember  {
        @Test
        @DisplayName("saveMember 성공")
        void saveMember () throws Exception{
            // given
            String json = "{\"name\": \"testName\", \"email\": \"test@gmail.com\"}";

            // when, then
            mvc.perform(post("/member/save")
                            .contentType(APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(MEMBER_SAVE_SUCCESS.getStatus()))
                    .andExpect(jsonPath("$.message").value(MEMBER_SAVE_SUCCESS.getMessage()));

        }

        @Test
        @DisplayName("saveMember 에러")
        void saveMemberFail () throws Exception{
            // given
            String json = "{\"name\": \"testName\", \"email\": \"test@gmail.com\"}";
            BusinessException expectedException = new BusinessException(EMAIL_ALREADY_EXIST);
            doThrow(expectedException).when(memberService).saveMember(any(MemberFormDto.class));

            // when, then
            mvc.perform(post("/member/save")
                            .contentType(APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(500))
                    .andExpect(jsonPath("$.message").value(expectedException.getMessage()));
        }

        @Nested
        class UpdateMember {
            @Test
            @DisplayName("updateMember")
            void updateMember () throws Exception{
                // given
                String json = "{\"name\": \"testName\", \"email\": \"test@gmail.com\"}";


                // when, then
                mvc.perform(patch("/member/view/{id}", 1L)
                                .contentType(APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value(MEMBER_UPDATE_SUCCESS.getStatus()))
                        .andExpect(jsonPath("$.message").value(MEMBER_UPDATE_SUCCESS.getMessage()));
            }

            @Test
            @DisplayName("")
            void checkUpdateMemberFail () throws Exception{
                // given
                String json = "{\"name\": \"testName\", \"email\": \"test@gmail.com\"}";
                BusinessException expectedException = new BusinessException(MEMBER_NOT_FOUND);
                doThrow(expectedException).when(memberService).updateMember(any(MemberFormDto.class), any(Long.class));
                // when, then
                mvc.perform(patch("/member/view/{id}", 1L)
                                .contentType(APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value(500))
                        .andExpect(jsonPath("$.message").value(expectedException.getMessage()));

            }
        }

        @Nested
        class GetMember {
            @Test
            @DisplayName("getMember")
            void getMember () throws Exception{
                // given
                Long memberId = 1L;
                MemberDto memberDto = new MemberDto();
                memberDto.setId(memberId);
                memberDto.setName("John Doe");
                memberDto.setEmail("john.doe@example.com");

                // when
                doReturn(memberDto).when(memberService).getMember(anyLong());

                // then
                mvc.perform(get("/member/{id}", memberId))
                        .andExpect(status().isOk())
                        .andExpect(view().name("member/member_detail"))
                        .andExpect(model().attributeExists("member"))
                        .andExpect(model().attribute("member", memberDto));
            }
        }
    }
}