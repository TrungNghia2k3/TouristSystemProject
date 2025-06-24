/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String address;

    private int stars;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "BIT")
    private byte isEmpty;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private long price;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @OneToMany(mappedBy = "hotel")
    private Set<HotelImage> Images;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Mỗi khách sạn thuộc về một người dùng
}
