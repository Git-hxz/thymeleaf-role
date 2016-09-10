package com.example.thymeleaf.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hxz
 * @since 2016/08/30 10:04
 */
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String role;
}
