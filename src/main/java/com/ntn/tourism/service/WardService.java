package com.ntn.tourism.service;

import com.ntn.tourism.dto.user.DistrictCityDTO;

public interface WardService {
    DistrictCityDTO findDistrictAndCityByWardId(int wardId);
}
