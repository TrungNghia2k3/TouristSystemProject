package com.ntn.tourism.service;

import com.ntn.tourism.model.Room;

import java.util.List;

public interface RoomService {
    List<Room> findByHotelId(int hotelId);
}
