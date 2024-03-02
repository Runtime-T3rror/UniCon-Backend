package com.codex.unicon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BasicStudentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private BigInteger enrollmentNo;
    private String studentName;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Branch branchId;
}
