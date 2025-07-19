package com.ntn.tourism.controller.user;

import com.ntn.tourism.model.City;
import com.ntn.tourism.model.Destination;
import com.ntn.tourism.model.Hotel;
import com.ntn.tourism.repository.*;
import com.ntn.tourism.dto.user.DistrictCityDTO;
import com.ntn.tourism.model.Tour;
import com.ntn.tourism.service.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/")
public class HomeController {

    DestinationService destinationService;
    WardService wardService;
    TourService tourService;
    HotelService HotelService;
    CityService cityService;

    @GetMapping
    public String displayHomepage(Model model) {

        // Get all city
        List<City> cities = cityService.findAll();
        cities.sort(Comparator.comparing(City::getCityName));
        model.addAttribute("cities", cities);

        // Get all destination
        List<Destination> destinations = destinationService.findAll();
        model.addAttribute("destinations", destinations);

        // Get list of tour (top 5 by stars)
        List<Tour> tours = tourService.findTop5ByOrderByStarsDesc(PageRequest.of(0, 5));
        model.addAttribute("tours", tours);

        // Get list of hotel (top 5 by stars)
        List<Hotel> hotels = HotelService.findTop5ByOrderByStarsDesc(PageRequest.of(0, 5));
        model.addAttribute("hotels", hotels);

        // Use Map to store district and city information
        Map<String, String> districtMap = new HashMap<>();
        Map<String, String> cityMap = new HashMap<>();

        // Lấy thông tin District và City từ Ward dựa trên wardId của mỗi Tour
        for (Tour tour : tours) {
            int wardId = tour.getWard().getId();
            DistrictCityDTO districtCity = wardService.findDistrictAndCityByWardId(wardId);
            if (districtCity != null) {
                districtMap.put("tour_districtName_" + tour.getId(), districtCity.getDistrictName());
                cityMap.put("tour_cityName_" + tour.getId(), districtCity.getCityName());
            }
        }

        for (Hotel hotel : hotels) {
            int wardId = hotel.getWard().getId();
            DistrictCityDTO districtCity = wardService.findDistrictAndCityByWardId(wardId);
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
