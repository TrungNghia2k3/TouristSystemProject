/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Duration;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nameTour;

    @Temporal(TemporalType.DATE)
    private Date dateStart;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private Duration duration;

    private int days;

    private int nights;

    private int quantity;

    private int stars;

    @Column(columnDefinition = "BIT")
    private byte isEmpty;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private long price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String tourSchedule;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @OneToMany(mappedBy = "tour")
    private Set<TourImage> tourImages;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Mỗi khách sạn thuộc về một người dùng
}
