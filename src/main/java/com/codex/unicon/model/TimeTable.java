package com.codex.unicon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private TimeSlot timeId;
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
    private Batch batId;
    private Faculty facId;
    private Room roomId;
    //add unique construct
    //UNIQUE(time_id, day, bat_id, fac_id, room_id),
    //    UNIQUE (time_id, day, room_id),
    //    UNIQUE (time_id, day, bat_id),
    //    UNIQUE (time_id, day, fac_id)

}
