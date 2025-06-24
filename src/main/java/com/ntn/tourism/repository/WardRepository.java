/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.repository;

import com.ntn.tourism.dto.user.DistrictCityDTO;
import com.ntn.tourism.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WardRepository extends JpaRepository<Ward, Integer> {

    @Query("SELECT new com.tourismsytem.dto.user.DistrictCityDTO(d.districtName, c.cityName) " +
            "FROM Ward w " +
            "JOIN District d ON w.district.id = d.id " +
            "JOIN City c ON d.city.id = c.id " +
            "WHERE w.id = :wardId")
    DistrictCityDTO findDistrictAndCityByWardId(@Param("wardId") Integer wardId);
}
