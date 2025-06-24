/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.repository;

import com.ntn.tourism.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {

    Page<Tour> findAll(Pageable pageable);

    @Query("SELECT t FROM Tour t ORDER BY t.stars DESC")
    List<Tour> findTop5ByOrderByStarsDesc(Pageable pageable);

    @Query("SELECT t FROM Tour t JOIN t.ward w JOIN w.district d JOIN d.city c " +
            "WHERE " +
            "(:cityId IS NULL OR c.id = :cityId) " +
            "AND (:minPrice IS NULL OR t.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR t.price <= :maxPrice) " +
            "AND (:dateFrom IS NULL OR t.dateStart >= :dateFrom) " +
            "AND (:dateTo IS NULL OR t.dateStart <= :dateTo) " +
            "AND (:stars IS NULL OR t.stars IN :stars)")
    List<Tour> filterTours(@Param("cityId") Integer cityId,
                           @Param("dateFrom") Date dateFrom,
                           @Param("dateTo") Date dateTo,
                           @Param("minPrice") Long minPrice,
                           @Param("maxPrice") Long maxPrice,
                           @Param("stars") List<Integer> stars);

    @Query("SELECT t FROM Tour t JOIN t.ward w JOIN w.district d JOIN d.city c " +
            "WHERE " +
            "(:cityId IS NULL OR c.id = :cityId) " +
            "AND (:minPrice IS NULL OR t.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR t.price <= :maxPrice) " +
            "AND (:dateFrom IS NULL OR t.dateStart >= :dateFrom) " +
            "AND (:dateTo IS NULL OR t.dateStart <= :dateTo) " +
            "AND (:stars IS NULL OR t.stars IN :stars)")
    Page<Tour> filterTours(
            @Param("cityId") Integer cityId,
            @Param("dateFrom") Date dateFrom,
            @Param("dateTo") Date dateTo,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("stars") List<Integer> stars,
            Pageable pageable);
}
