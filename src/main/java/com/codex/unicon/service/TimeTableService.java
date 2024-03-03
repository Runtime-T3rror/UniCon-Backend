package com.codex.unicon.service;

import com.codex.unicon.dto.common.CommonErrorResponse;
import com.codex.unicon.dto.common.CommonResponse;
import com.codex.unicon.dto.response.TimeTableResponseDto;
import com.codex.unicon.model.BasicStudentDetails;
import com.codex.unicon.model.DynamicStudentDetails;
import com.codex.unicon.model.TimeTable;
import com.codex.unicon.model.User_;
import com.codex.unicon.repository.BasicStudentDetailsRepo;
import com.codex.unicon.repository.DynamicStudentDetailsRepo;
import com.codex.unicon.repository.TimeTableRepo;
import com.codex.unicon.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TimeTableService {
    private final UserRepo userRepo;
    private final BasicStudentDetailsRepo basicStudentDetailsRepo;
    private final DynamicStudentDetailsRepo dynamicStudentDetailsRepo;
    private final TimeTableRepo timeTableRepo;

    @Transactional
    public CommonResponse<?> getTimeTable(String email) {
        Optional<User_> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "invalid_verification_token",
                    "invalid_verification_token",
                    "invalid_verification_token"
                )
            );
        }
        BigInteger enrollmentNo = new BigInteger(email.split("@")[0]);
        Optional<BasicStudentDetails> basicStudentDetails = basicStudentDetailsRepo.findByEnrollmentNo(enrollmentNo);
        if (basicStudentDetails.isEmpty()) {
            return new CommonResponse<>(
                new Exception()
            );
        }
        Optional<DynamicStudentDetails> dynamicStudentDetails = dynamicStudentDetailsRepo.findByStudentId(basicStudentDetails.get());
        if (dynamicStudentDetails.isEmpty()) {
            return new CommonResponse<>(
                new Exception()
            );
        }
        List<TimeTable> timeTables = timeTableRepo.findByBatId(dynamicStudentDetails.get().getBatchId());
        Map<Integer, List<TimeTableResponseDto>> timeTableMap = new HashMap<>();
        for (TimeTable timeTable : timeTables) {
            TimeTableResponseDto timeTableResponseDto = TimeTableResponseDto.builder()
                .subjectName(timeTable.getFacId().getSubject().getShortname())
                .facultyShortName(timeTable.getFacId().getShortname())
                .roomNo(timeTable.getRoomId().getRoomNo())
                .startTime(timeTable.getTimeId().getEndTime())
                .endTime(timeTable.getTimeId().getStartTime())
                .build();
            if (!timeTableMap.containsKey(timeTable.getDay().ordinal())) {
                timeTableMap.put(timeTable.getDay().ordinal(), new ArrayList<>());
            }
            timeTableMap.get(timeTable.getDay().ordinal()).add(timeTableResponseDto);
        }

        return new CommonResponse<>(
            timeTableMap
        );
    }
}
