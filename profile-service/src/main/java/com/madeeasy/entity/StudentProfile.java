package com.madeeasy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
public class StudentProfile implements Serializable {

    @Id
    private Long id; // This will match User's ID

    @Lob // Indicates that this field can store large objects
    private byte[] photo; // Binary data of the photo

    private String type; // this is image type.

    @Column(nullable = false)
    private Long departmentId;

    @Column(nullable = false)
    private int startYear;

    @Column(nullable = false)
    private int endYear;
}
