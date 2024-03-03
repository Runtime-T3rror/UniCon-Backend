package com.codex.unicon.repository;

import com.codex.unicon.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepo extends JpaRepository<TimeTable, Integer> {
}
