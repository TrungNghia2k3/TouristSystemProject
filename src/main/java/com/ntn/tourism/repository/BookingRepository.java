package com.ntn.tourism.repository;

import com.ntn.tourism.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByHotelId(Integer hotelId);
}
