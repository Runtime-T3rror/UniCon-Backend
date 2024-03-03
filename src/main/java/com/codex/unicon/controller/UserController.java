package com.codex.unicon.controller;

import com.codex.unicon.dto.common.CommonResponse;
import com.codex.unicon.dto.request.EmailReqDto;
import com.codex.unicon.dto.request.LoginRequestDto;
import com.codex.unicon.dto.response.TokenResponseDto;
import com.codex.unicon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody EmailReqDto emailReqDto) {
        CommonResponse<?> commonResponse = userService.register(emailReqDto);
        if (commonResponse.getHasException()) {
            return ResponseEntity.internalServerError().body(commonResponse.getErrorResponse());
        }
        if (!commonResponse.getIsSuccess()) {
            return ResponseEntity.badRequest().body(commonResponse.getErrorResponse());
        }
        return ResponseEntity.ok().body(commonResponse.getResult());
    }

    @PostMapping("/set-password")
    public ResponseEntity<?> setPassword(
        @RequestParam String password,
        @RequestParam String token
    ) {
        CommonResponse<?> commonResponse = userService.setPassword(token, password);
        if (commonResponse.getHasException()) {
            return ResponseEntity.internalServerError().body(commonResponse.getErrorResponse());
        }
        if (!commonResponse.getIsSuccess()) {
            return ResponseEntity.badRequest().body(commonResponse.getErrorResponse());
        }
        return ResponseEntity.ok().body(commonResponse.getResult());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody LoginRequestDto request
    ) {
        CommonResponse<TokenResponseDto> commonResponse = userService.login(request);
        if (commonResponse.getHasException()) {
            return ResponseEntity.internalServerError().body(commonResponse.getErrorResponse());
        }
        if (!commonResponse.getIsSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(commonResponse.getErrorResponse());
        }
        return ResponseEntity.ok().body(commonResponse.getResult());
    }
}
