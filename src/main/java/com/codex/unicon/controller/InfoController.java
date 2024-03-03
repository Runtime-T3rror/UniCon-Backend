package com.codex.unicon.controller;

import com.codex.unicon.dto.common.CommonResponse;
import com.codex.unicon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class InfoController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUserName(
        Authentication authentication
    ) {
        CommonResponse<?> commonResponse = userService.getUserName(authentication.getName());
        if (commonResponse.getHasException()) {
            return ResponseEntity.internalServerError().body(commonResponse.getErrorResponse());
        }
        if (!commonResponse.getIsSuccess()) {
            return ResponseEntity.badRequest().body(commonResponse.getErrorResponse());
        }
        return ResponseEntity.ok().body(commonResponse.getResult());
    }
}
