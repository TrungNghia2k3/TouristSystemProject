package com.ntn.tourism.controller.user;

import com.ntn.tourism.model.City;
import com.ntn.tourism.model.Destination;
import com.ntn.tourism.model.Hotel;
import com.ntn.tourism.repository.*;
import com.ntn.tourism.dto.user.DistrictCityDTO;
import com.ntn.tourism.model.Tour;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class HomeController {

    private final DestinationRepository destinationRepository;
    private final WardRepository wardRepository;
    private final TourRepository tourRepository;
    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;

    @GetMapping
    public String displayHomepage(Model model) {

        // Get all city
        List<City> cities = cityRepository.findAll();
        cities.sort(Comparator.comparing(City::getCityName));
        model.addAttribute("cities", cities);

        // Get all destination
        List<Destination> destinations = destinationRepository.findAll();
        model.addAttribute("destinations", destinations);

        // Get list of tour (top 5 by stars)
        List<Tour> tours = tourRepository.findTop5ByOrderByStarsDesc(PageRequest.of(0, 5));
        model.addAttribute("tours", tours);

        // Get list of hotel (top 5 by stars)
        List<Hotel> hotels = hotelRepository.findTop5ByOrderByStarsDesc(PageRequest.of(0, 5));
        model.addAttribute("hotels", hotels);

        // Use Map to store district and city information
        Map<String, String> districtMap = new HashMap<>();
        Map<String, String> cityMap = new HashMap<>();

        // Lấy thông tin District và City từ Ward dựa trên wardId của mỗi Tour
        for (Tour tour : tours) {
            Integer wardId = tour.getWard().getId();
            DistrictCityDTO districtCity = wardRepository.findDistrictAndCityByWardId(wardId);
            if (districtCity != null) {
                districtMap.put("tour_districtName_" + tour.getId(), districtCity.getDistrictName());
                cityMap.put("tour_cityName_" + tour.getId(), districtCity.getCityName());
            }
        }

        for (Hotel hotel : hotels) {
            Integer wardId = hotel.getWard().getId();
            DistrictCityDTO districtCity = wardRepository.findDistrictAndCityByWardId(wardId);
            if (districtCity != null) {
                districtMap.put("hotel_districtName_" + hotel.getId(), districtCity.getDistrictName());
                cityMap.put("hotel_cityName_" + hotel.getId(), districtCity.getCityName());
            }
        }

        // Add information to model
        model.addAttribute("districtMap", districtMap);
        model.addAttribute("cityMap", cityMap);

        return "user/index";
    }

}
