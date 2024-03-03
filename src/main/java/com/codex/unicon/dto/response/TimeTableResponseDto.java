package com.codex.unicon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableResponseDto {
    private String subjectName;
    private String facultyShortName;
    private String roomNo;
    private Time startTime;
    private Time endTime;
}
