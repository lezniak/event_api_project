package com.example.praca.service;

import com.example.praca.dto.PlaceFilterParam;
import com.example.praca.dto.common.PageableDto;
import com.example.praca.dto.event.InformationEventDto;
import com.example.praca.dto.places.PlaceInformationDto;
import com.example.praca.exception.EventNotFoundException;
import com.example.praca.model.Event;
import com.example.praca.model.EventType;
import com.example.praca.model.Places;
import com.example.praca.model.User;
import com.example.praca.repository.PlacesRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
public class PlacesService {
    private final PlacesRepository PLACES_REPOSITORY;

    public ReturnService getAll(int pageNo, int pageSize, String sortBy, String sortDir, PlaceFilterParam filterParam, String... value) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Places> placesPage = null;

        switch (filterParam) {
            case C:
                placesPage = PLACES_REPOSITORY.findAllByCapacity(Integer.parseInt(value[0]), pageable);
                break;
        }


        if (placesPage == null)
            throw new EventNotFoundException();
        List<Places> placesList = placesPage.getContent();

        List<PlaceInformationDto> placeInformationDtos = placesList.stream()
                .map(x -> PlaceInformationDto.of(x))
                .collect(Collectors.toList());


        return ReturnService.returnInformation("", PageableDto.of(placeInformationDtos, placesPage),1);
    }


//    public ReturnService getAllInRange(Double userLatD, Double userLngD, Double rangeD) {
//
//    }
}
