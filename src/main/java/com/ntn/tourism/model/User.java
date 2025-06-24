/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "[user]")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;

    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
