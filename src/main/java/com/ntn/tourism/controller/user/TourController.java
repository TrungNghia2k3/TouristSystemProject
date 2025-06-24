/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntn.tourism.controller.user;

import com.ntn.tourism.model.Booking;
import com.ntn.tourism.model.BookingType;
import com.ntn.tourism.model.City;
import com.ntn.tourism.repository.BookingRepository;
import com.ntn.tourism.repository.CityRepository;
import com.ntn.tourism.repository.TourRepository;
import com.ntn.tourism.repository.WardRepository;
import com.ntn.tourism.dto.user.DistrictCityDTO;
import com.ntn.tourism.dto.user.TourBookingDTO;
import com.ntn.tourism.model.Tour;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/tour")
public class TourController {

    private final TourRepository tourRepository;
    private final WardRepository wardRepository;
    private final CityRepository cityRepository;
    private final BookingRepository bookingRepository;

    @GetMapping
    public String showTourPage(@RequestParam(defaultValue = "0") int page,  // Page starts at 0
                               @RequestParam(defaultValue = "6") int size,  // 6 tours per page
                               Model model) {

        List<City> cities = cityRepository.findAll();
        cities.sort(Comparator.comparing(City::getCityName));
        model.addAttribute("cities", cities);

        // Fetch paginated tours
        Page<Tour> tourPage = tourRepository.findAll(PageRequest.of(page, size));

        // Add tours to the model
        model.addAttribute("tours", tourPage.getContent()); // Danh sách tour
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tourPage.getTotalPages());

        // Get district and city infor for tours
        Map<Integer, DistrictCityDTO> districtCityMap = getDistrictCityMap(tourPage.getContent());
        Map<String, String> districtMap = new HashMap<>();
        Map<String, String> cityMap = new HashMap<>();

        for (Tour tour : tourPage.getContent()) {
            DistrictCityDTO districtCity = districtCityMap.get(tour.getWard().getId());
            if (districtCity != null) {
                districtMap.put("tour_districtName_" + tour.getId(), districtCity.getDistrictName());
                cityMap.put("tour_cityName_" + tour.getId(), districtCity.getCityName());
            }
        }

        model.addAttribute("districtMap", districtMap);
        model.addAttribute("cityMap", cityMap);

        return "user/tour";
    }

    @GetMapping("/tour-filter")
    public String filterTours(@RequestParam(required = false) Integer cityId, @RequestParam(required = false) @DateTimeFormat(pattern = "MM/dd/yyyy") Date dateFrom, @RequestParam(required = false) @DateTimeFormat(pattern = "MM/dd/yyyy") Date dateTo, @RequestParam(required = false) Long minPrice, @RequestParam(required = false) Long maxPrice, @RequestParam(required = false) List<Integer> stars, @RequestParam(defaultValue = "0") int page,  // Default page 0
                              @RequestParam(defaultValue = "6") int size,  // Default size 6 tours per page
                              Model model) {

        List<City> cities = cityRepository.findAll();
        cities.sort(Comparator.comparing(City::getCityName));
        model.addAttribute("cities", cities);

        // Use Pageable to apply pagination
        Page<Tour> tourPage = tourRepository.filterTours(cityId, dateFrom, dateTo, minPrice, maxPrice, stars, PageRequest.of(page, size));

        model.addAttribute("tours", tourPage.getContent()); // Paginated tour list
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tourPage.getTotalPages());

        // Get district and city info for filtered tours
        Map<Integer, DistrictCityDTO> districtCityMap = getDistrictCityMap(tourPage.getContent());
        Map<String, String> districtMap = new HashMap<>();
        Map<String, String> cityMap = new HashMap<>();

        for (Tour tour : tourPage.getContent()) {
            DistrictCityDTO districtCity = districtCityMap.get(tour.getWard().getId());
            if (districtCity != null) {
                districtMap.put("tour_districtName_" + tour.getId(), districtCity.getDistrictName());
                cityMap.put("tour_cityName_" + tour.getId(), districtCity.getCityName());
            }
        }

        model.addAttribute("districtMap", districtMap);
        model.addAttribute("cityMap", cityMap);

        return "user/tour";
    }


    @GetMapping("/tour-details")
    public String showTourDetailPage(@RequestParam int id, Model model) {
        Tour tour = tourRepository.findById(id).orElse(null);

        if (tour == null) return "redirect:/tour";

        DistrictCityDTO districtCity = wardRepository.findDistrictAndCityByWardId(tour.getWard().getId());
        model.addAttribute("wardName", tour.getWard().getWardName());
        model.addAttribute("districtName", districtCity.getDistrictName());
        model.addAttribute("cityName", districtCity.getCityName());

        model.addAttribute("tour", tour);

        model.addAttribute("tourBookingDTO", new TourBookingDTO());

        return "user/tour-details";
    }

    @PostMapping(value = "/booking")
    public String tourBooking(@Valid @ModelAttribute("tourBookingDTO") TourBookingDTO tourBookingDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            Tour tour = tourRepository.findById(tourBookingDTO.getTourId()).orElse(null);

            DistrictCityDTO districtCity = wardRepository.findDistrictAndCityByWardId(tour.getWard().getId());

            model.addAttribute("wardName", tour.getWard().getWardName());
            model.addAttribute("districtName", districtCity.getDistrictName());
            model.addAttribute("cityName", districtCity.getCityName());

            model.addAttribute("tourBookingDTO", tourBookingDTO);
            return "user/tour-details";
        }

        // Tìm Tour theo tourId
        Tour tour = tourRepository.findById(tourBookingDTO.getTourId()).orElseThrow(() -> new IllegalArgumentException("Invalid tour ID"));

        // Chuyển DTO thành Entity
        Booking booking = new Booking();
        booking.setPhone(tourBookingDTO.getPhone());
        booking.setEmail(tourBookingDTO.getEmail());
        booking.setName(tourBookingDTO.getName());
        booking.setDateFrom(tourBookingDTO.getDateFrom());
        booking.setDateTo(tourBookingDTO.getDateTo());
        booking.setGuest(tourBookingDTO.getGuest());
        booking.setChildren(tourBookingDTO.getChildren() != null ? tourBookingDTO.getChildren() : 0);
        booking.setNotes(tourBookingDTO.getNotes());
        booking.setBookingType(BookingType.TOUR_BOOKING);
        booking.setTour(tour);

        // Lưu booking vào database
        bookingRepository.save(booking);

        return "redirect:/tour/tour-details?id=" + tourBookingDTO.getTourId();
    }

    private Map<Integer, DistrictCityDTO> getDistrictCityMap(List<Tour> tours) {
        Map<Integer, DistrictCityDTO> districtCityMap = new HashMap<>();
        for (Tour tour : tours) {
            Integer wardId = tour.getWard().getId();
            districtCityMap.computeIfAbsent(wardId, wardRepository::findDistrictAndCityByWardId);
        }
        return districtCityMap;
    }
}
