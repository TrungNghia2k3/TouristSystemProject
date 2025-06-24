/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.model;

import javax.persistence.*;
import java.util.Set;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Ward")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String wardName;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @OneToMany(mappedBy = "ward")
    private Set<Destination> destinations;

    @OneToMany(mappedBy = "ward")
    private Set<Tour> tours;
}
