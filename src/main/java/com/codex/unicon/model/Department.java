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
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String departmentName;

    @ManyToOne
    @JoinColumn(name = "hod_id", referencedColumnName = "id") // Adjust the column names as per your database schema
    private Faculty hod;
}
