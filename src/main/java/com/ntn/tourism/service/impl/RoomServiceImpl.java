package com.ntn.tourism.service.impl;

import com.ntn.tourism.model.Room;
import com.ntn.tourism.repository.RoomRepository;
import com.ntn.tourism.service.RoomService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomServiceImpl implements RoomService {

    RoomRepository roomRepository;

    @Override
    public List<Room> findByHotelId(int hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }
}
