package com.example.praca.controller;

import com.example.praca.dto.PlaceFilterParam;
import com.example.praca.service.PlacesService;
import com.example.praca.service.ReturnService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/place")
public class PlaceController {
    private final PlacesService PLACES_SERVICE;

    @GetMapping("/capacity")
    public ReturnService getAllPlaces(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                          @RequestParam(value = "capacity", defaultValue = "", required = true) String capacity) {
        return PLACES_SERVICE.getAll(pageNo, pageSize, sortBy, sortDir, PlaceFilterParam.C, new String[] {capacity});
    }

//    @Transactional
//    @GetMapping("/range")
//    public ReturnService getEventsByRange(@RequestParam(value = "range", defaultValue = "", required = true) String range,
//                                          @RequestParam(value = "userLat", defaultValue = "", required = true) String userLat,
//                                          @RequestParam(value = "userLng", defaultValue = "", required = true) String userLng) {
//        Double userLatD = Double.valueOf(userLat);
//        Double userLngD = Double.valueOf(userLng);
//        Double rangeD = Double.valueOf(range);
//        return PLACES_SERVICE.getAllInRange(userLatD, userLngD, rangeD);
//    }
}
