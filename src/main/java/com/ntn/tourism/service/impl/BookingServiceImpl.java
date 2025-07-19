package com.ntn.tourism.service.impl;

import com.ntn.tourism.model.Booking;
import com.ntn.tourism.repository.BookingRepository;
import com.ntn.tourism.service.BookingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    BookingRepository bookingRepository;


    @Override
    public List<Booking> findByHotelId(int hotelId) {
        return bookingRepository.findByHotelId(hotelId);
    }

    @Override
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }
}
