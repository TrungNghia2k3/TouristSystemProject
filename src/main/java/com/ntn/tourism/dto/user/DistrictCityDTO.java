package com.ntn.tourism.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DistrictCityDTO {
    private String districtName;
    private String cityName;

    public DistrictCityDTO(String districtName, String cityName) {
        this.districtName = districtName;
        this.cityName = cityName;
    }

}
