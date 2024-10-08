package com.madeeasy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "`user`")
public class User implements Serializable {

    @Id
    private Long id;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
}
