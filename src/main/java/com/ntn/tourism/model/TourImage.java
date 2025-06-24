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
@NoArgsConstructor
@Table(name = "TourImage")
public class TourImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String fileName;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "image_type_id", nullable = false)
    private ImageType imageType;
}
