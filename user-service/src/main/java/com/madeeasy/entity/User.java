package com.madeeasy.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "`users`")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    private String id;
    @Column(nullable = false)
    private String fullName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String phone;
    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // This will ensure the individual elements are non-null
    private List<Role> roles = new ArrayList<>();
}
