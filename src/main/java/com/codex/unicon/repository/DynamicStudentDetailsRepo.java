package com.codex.unicon.repository;


import com.codex.unicon.model.BasicStudentDetails;
import com.codex.unicon.model.DynamicStudentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DynamicStudentDetailsRepo extends JpaRepository<DynamicStudentDetails, Integer> {
    Optional<DynamicStudentDetails> findByStudentId(BasicStudentDetails studentId);
}
