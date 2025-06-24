/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.repository;

import com.ntn.tourism.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DestinationRepository extends JpaRepository<Destination, Integer> {
}
