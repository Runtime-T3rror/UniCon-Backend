package com.codex.unicon.repository;

import com.codex.unicon.model.Batch;
import com.codex.unicon.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeTableRepo extends JpaRepository<TimeTable, Integer> {
    List<TimeTable> findByBatId(Batch batId);
}
