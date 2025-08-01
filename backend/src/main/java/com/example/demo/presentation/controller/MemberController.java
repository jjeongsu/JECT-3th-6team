package com.example.demo.presentation.controller;

import com.example.demo.application.dto.member.MeResponse;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberPort memberPort;

    @GetMapping("/me")
    public ApiResponse<MeResponse> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("인증된 사용자 정보를 가져올 수 없습니다.");
        }
        Member member = memberPort.findById(principal.getId())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + principal.getId()));
        return new ApiResponse<>("사용자 정보 조회 성공", MeResponse.from(member));
    }
} 