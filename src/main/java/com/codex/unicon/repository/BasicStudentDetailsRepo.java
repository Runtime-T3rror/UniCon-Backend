package com.codex.unicon.repository;

import com.codex.unicon.model.BasicStudentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface BasicStudentDetailsRepo extends JpaRepository<BasicStudentDetails, Integer> {
    Optional<BasicStudentDetails> findByEnrollmentNo(BigInteger enrollmentNo);
}
