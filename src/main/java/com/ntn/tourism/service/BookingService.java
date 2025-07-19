package com.ntn.tourism.service;

import com.ntn.tourism.model.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> findByHotelId(int hotelId);

    void save (Booking booking);
}
