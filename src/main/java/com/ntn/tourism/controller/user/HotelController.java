/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.controller.user;

import com.ntn.tourism.dto.user.HotelBookingDTO;
import com.ntn.tourism.dto.user.DistrictCityDTO;
import com.ntn.tourism.model.*;
import com.ntn.tourism.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/hotels")
public class HotelController {

    private final HotelRepository hotelRepository;
    private final WardRepository wardRepository;
    private final CityRepository cityRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @GetMapping
    public String showHotelPage(
            @RequestParam(defaultValue = "0") int page,  // Page number (default: 0)
            @RequestParam(defaultValue = "6") int size,  // Page size (default: 6 hotels)
            Model model) {

        // Get all cities
        List<City> cities = cityRepository.findAll();
        cities.sort(Comparator.comparing(City::getCityName));
        model.addAttribute("cities", cities);

        // Fetch paginated hotels
        Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, size));

        // Add hotesl to the model
        model.addAttribute("hotels", hotelPage.getContent()); //List of hotel
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hotelPage.getTotalPages());

        // Retrieve district and city info efficiently
        Map<Integer, DistrictCityDTO> districtCityMap = getDistrictCityMap(hotelPage.getContent());
        Map<String, String> districtMap = new HashMap<>();
        Map<String, String> cityMap = new HashMap<>();

        for (Hotel hotel : hotelPage.getContent()) {
            DistrictCityDTO districtCity = districtCityMap.get(hotel.getWard().getId());
            if (districtCity != null) {
                districtMap.put("hotel_districtName_" + hotel.getId(), districtCity.getDistrictName());
                cityMap.put("hotel_cityName_" + hotel.getId(), districtCity.getCityName());
            }
        }

        model.addAttribute("districtMap", districtMap);
        model.addAttribute("cityMap", cityMap);

        return "user/hotel";
    }

    @GetMapping("/hotel-filter")
    public String filterHotels(
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) List<Integer> stars,
            @RequestParam(defaultValue = "0") int page,  // Default page 0
            @RequestParam(defaultValue = "6") int size,  // Default size 6 tours per page
            Model model) {

        // Get all cities
        List<City> cities = cityRepository.findAll();
        cities.sort(Comparator.comparing(City::getCityName));
        model.addAttribute("cities", cities);

        // Use Pageable to apply pagination
        Page<Hotel> hotelPage = hotelRepository.filterHotels(cityId, minPrice, maxPrice, stars, PageRequest.of(page, size));
        model.addAttribute("hotels", hotelPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hotelPage.getTotalPages());

        // Retrieve district and city info efficiently
        Map<Integer, DistrictCityDTO> districtCityMap = getDistrictCityMap(hotelPage.getContent());
        Map<String, String> districtMap = new HashMap<>();
        Map<String, String> cityMap = new HashMap<>();

        for (Hotel hotel : hotelPage.getContent()) {
            DistrictCityDTO districtCity = districtCityMap.get(hotel.getWard().getId());
            if (districtCity != null) {
                districtMap.put("hotel_districtName_" + hotel.getId(), districtCity.getDistrictName());
                cityMap.put("hotel_cityName_" + hotel.getId(), districtCity.getCityName());
            }
        }

        model.addAttribute("districtMap", districtMap);
        model.addAttribute("cityMap", cityMap);

        return "user/hotel";
    }

    @GetMapping("/hotel-details")
    public String showHotelDetailPage(
            @RequestParam int id,
            Model model
    ) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);

        if (hotel == null) return "redirect:/hotels";

        DistrictCityDTO districtCity = wardRepository.findDistrictAndCityByWardId(hotel.getWard().getId());

        model.addAttribute("wardName", hotel.getWard().getWardName());
        model.addAttribute("districtName", districtCity.getDistrictName());
        model.addAttribute("cityName", districtCity.getCityName());

        // Get all rooms by hotel id
        List<Room> rooms = roomRepository.findByHotelId(id);
        model.addAttribute("rooms", rooms);

        model.addAttribute("hotel", hotel);

        model.addAttribute("hotelBookingDTO", new HotelBookingDTO()); // Support for form

        return "user/hotel-details";
    }

    @PostMapping(value = "/booking")
    public String hotelBooking(@Valid @ModelAttribute HotelBookingDTO hotelBookingDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            Hotel hotel = hotelRepository.findById(hotelBookingDTO.getHotelId()).orElse(null);

            DistrictCityDTO districtCity = wardRepository.findDistrictAndCityByWardId(hotel.getWard().getId());

            model.addAttribute("wardName", hotel.getWard().getWardName());
            model.addAttribute("districtName", districtCity.getDistrictName());
            model.addAttribute("cityName", districtCity.getCityName());

            // Get all rooms by hotel id
            List<Room> rooms = roomRepository.findByHotelId(hotelBookingDTO.getHotelId());
            model.addAttribute("rooms", rooms);
            model.addAttribute("hotel", hotel);

            model.addAttribute("hotelBookingDTO", hotelBookingDTO);
            return "user/hotel-details";
        }

        // Tìm Hotel theo hotelId
        Hotel hotel = hotelRepository.findById(hotelBookingDTO.getHotelId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid hotel ID"));

        // Chuyển DTO thành Entity
        Booking booking = new Booking();
        booking.setPhone(hotelBookingDTO.getPhone());
        booking.setEmail(hotelBookingDTO.getEmail());
        booking.setName(hotelBookingDTO.getName());
        booking.setDateFrom(hotelBookingDTO.getDateFrom());
        booking.setDateTo(hotelBookingDTO.getDateTo());
        booking.setGuest(hotelBookingDTO.getGuest());
        booking.setChildren(hotelBookingDTO.getChildren() != null ? hotelBookingDTO.getChildren() : 0);
        booking.setNotes(hotelBookingDTO.getNotes());
        booking.setBookingType(BookingType.HOTEL_BOOKING);
        booking.setHotel(hotel);

        // Lưu booking vào database
        bookingRepository.save(booking);

        return "redirect:/hotels/hotel-details?id=" + hotelBookingDTO.getHotelId();
    }

    private Map<Integer, DistrictCityDTO> getDistrictCityMap(List<Hotel> hotels) {
        Map<Integer, DistrictCityDTO> districtCityMap = new HashMap<>();

        for (Hotel hotel : hotels) {
            if (hotel.getWard() != null) {
                Integer wardId = hotel.getWard().getId();
                if (!districtCityMap.containsKey(wardId)) {
                    DistrictCityDTO districtCity = wardRepository.findDistrictAndCityByWardId(wardId);
                    if (districtCity != null) {
                        districtCityMap.put(wardId, districtCity);
                    }
                }
            }
        }

        return districtCityMap;
    }

}
