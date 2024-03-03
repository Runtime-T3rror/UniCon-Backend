package com.codex.unicon.controller;

import com.codex.unicon.dto.common.CommonResponse;
import com.codex.unicon.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/timetable")
@RequiredArgsConstructor
public class TimeTableController {
    private final TimeTableService timeTableService;

    @GetMapping
    public ResponseEntity<?> getTimeTable(
        Authentication authentication
    ) {
        CommonResponse<?> commonResponse = timeTableService.getTimeTable(authentication.getName());
        if (commonResponse.getHasException()) {
            return ResponseEntity.internalServerError().body(commonResponse.getErrorResponse());
        }
        if (!commonResponse.getIsSuccess()) {
            return ResponseEntity.badRequest().body(commonResponse.getErrorResponse());
        }
        return ResponseEntity.ok().body(commonResponse.getResult());
    }
}
