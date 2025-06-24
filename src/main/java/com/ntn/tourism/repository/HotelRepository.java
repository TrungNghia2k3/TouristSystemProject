/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.repository;

import com.ntn.tourism.model.Hotel;
import com.ntn.tourism.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    @Query("SELECT h FROM Hotel h ORDER BY h.stars DESC")
    List<Hotel> findTop5ByOrderByStarsDesc(Pageable pageable);

    @Query("SELECT h FROM Hotel h JOIN h.ward w JOIN w.district d JOIN d.city c " +
            "WHERE" +
            "(:cityId IS NULL OR c.id = :cityId) " +
            "AND (:minPrice IS NULL OR h.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR h.price <= :maxPrice)" +
            "AND (:stars IS NULL OR h.stars IN :stars)")
    Page<Hotel> filterHotels(
            @Param("cityId") Integer cityId,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("stars") List<Integer> stars,
            Pageable pageable);

    Hotel findByUser(User user);
}
