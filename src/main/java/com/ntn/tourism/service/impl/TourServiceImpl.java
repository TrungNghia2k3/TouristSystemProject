package com.ntn.tourism.service.impl;

import com.ntn.tourism.model.Tour;
import com.ntn.tourism.repository.TourRepository;
import com.ntn.tourism.service.TourService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourServiceImpl implements TourService {
    TourRepository tourRepository;

    @Override
    public List<Tour> findTop5ByOrderByStarsDesc(Pageable pageable) {
        return tourRepository.findTop5ByOrderByStarsDesc(pageable);
    }

    @Override
    public List<Tour> filterTours(Integer cityId, Date dateFrom, Date dateTo, Long minPrice, Long maxPrice, List<Integer> stars) {
        return tourRepository.filterTours(cityId, dateFrom, dateTo, minPrice, maxPrice, stars);
    }

    @Override
    public Page<Tour> filterTours(Integer cityId, Date dateFrom, Date dateTo, Long minPrice, Long maxPrice, List<Integer> stars, Pageable pageable) {
        return tourRepository.filterTours(cityId, dateFrom, dateTo, minPrice, maxPrice, stars, pageable);
    }

    @Override
    public Tour findById(int id) {
        return tourRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Tour> findAll(Pageable pageable) {
        return tourRepository.findAll(pageable);
    }
}
