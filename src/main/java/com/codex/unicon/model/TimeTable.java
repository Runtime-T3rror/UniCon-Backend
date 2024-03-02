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
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private TimeSlot timeId;
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Batch batId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Faculty facId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Room roomId;
}
