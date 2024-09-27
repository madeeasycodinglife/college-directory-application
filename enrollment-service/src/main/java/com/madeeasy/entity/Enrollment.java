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
public class Enrollment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enrollment_sequence_generator")
    @SequenceGenerator(
            name = "enrollment_sequence_generator",
            sequenceName = "enrollment_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;


}

