package com.madeeasy.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_sequence_generator")
    @SequenceGenerator(
            name = "department_sequence_generator",
            sequenceName = "department_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
}
