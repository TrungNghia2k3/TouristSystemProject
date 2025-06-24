package com.ntn.tourism.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roomType;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private long pricePerNight;

    private int capacity;

    private boolean available;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
}
