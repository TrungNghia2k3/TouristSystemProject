package com.ntn.tourism.service.impl;

import com.ntn.tourism.dto.user.DistrictCityDTO;
import com.ntn.tourism.repository.WardRepository;
import com.ntn.tourism.service.WardService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WardServiceImpl implements WardService {

    WardRepository wardRepository;

    @Override
    public DistrictCityDTO findDistrictAndCityByWardId(int wardId) {
        return wardRepository.findDistrictAndCityByWardId(wardId);
    }
}
