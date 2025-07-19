package com.ntn.tourism.service;

import com.ntn.tourism.model.Hotel;
import com.ntn.tourism.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelService {
    Hotel findByUser(User user);

    List<Hotel> findTop5ByOrderByStarsDesc(Pageable pageable);

    Page<Hotel> filterHotels(Integer cityId, Long minPrice, Long maxPrice, List<Integer> stars, Pageable pageable);

    Hotel findById(int id);

    Page<Hotel> findAll(Pageable pageable);
}
