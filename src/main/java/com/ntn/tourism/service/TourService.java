package com.ntn.tourism.service;

import com.ntn.tourism.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TourService {
    List<Tour> findTop5ByOrderByStarsDesc(Pageable pageable);

    List<Tour> filterTours(Integer cityId, Date dateFrom, Date dateTo, Long minPrice, Long maxPrice, List<Integer> stars);

    Page<Tour> filterTours(Integer cityId, Date dateFrom, Date dateTo, Long minPrice, Long maxPrice, List<Integer> stars, Pageable pageable);

    Tour findById(int id);

    Page<Tour> findAll(Pageable pageable);
}
