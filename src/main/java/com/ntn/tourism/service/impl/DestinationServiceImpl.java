package com.ntn.tourism.service.impl;

import com.ntn.tourism.model.Destination;
import com.ntn.tourism.repository.DestinationRepository;
import com.ntn.tourism.service.DestinationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DestinationServiceImpl implements DestinationService {

    DestinationRepository destinationRepository;

    @Override
    public List<Destination> findAll() {
        return destinationRepository.findAll();
    }
}
