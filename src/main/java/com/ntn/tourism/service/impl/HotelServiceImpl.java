package com.ntn.tourism.service.impl;

import com.ntn.tourism.model.Hotel;
import com.ntn.tourism.model.User;
import com.ntn.tourism.repository.HotelRepository;
import com.ntn.tourism.service.HotelService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelServiceImpl implements HotelService {
    HotelRepository hotelRepository;


    @Override
    public Hotel findByUser(User user) {
        return hotelRepository.findByUser(user);
    }

    @Override
    public List<Hotel> findTop5ByOrderByStarsDesc(Pageable pageable) {
        return hotelRepository.findTop5ByOrderByStarsDesc(pageable);
    }

    @Override
    public Page<Hotel> filterHotels(Integer cityId, Long minPrice, Long maxPrice, List<Integer> stars, Pageable pageable) {
        return hotelRepository.filterHotels(cityId, minPrice, maxPrice, stars, pageable);
    }

    @Override
    public Hotel findById(int id) {
        return hotelRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Hotel> findAll(Pageable pageable) {
        return hotelRepository.findAll(pageable);
    }
}
