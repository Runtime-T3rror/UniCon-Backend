package com.codex.unicon.controller;

import com.codex.unicon.dto.common.CommonResponse;
import com.codex.unicon.dto.request.EmailReqDto;
import com.codex.unicon.repository.TokenRepo;
import com.codex.unicon.repository.UserRepo;
import com.codex.unicon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;

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
}
