package com.example.praca.controller;

import com.example.praca.dto.eventtype.EventTypeInformationDto;
import com.example.praca.dto.filter.EventFilterParam;
import com.example.praca.dto.event.CreateEventDto;
import com.example.praca.dto.event.UpdateEventDto;
import com.example.praca.service.EventService;
import com.example.praca.service.EventTypeService;
import com.example.praca.service.ReturnService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/event")
@Slf4j
public class EventController {
    private final EventService EVENT_SERVICE;
    private final EventTypeService EVENT_TYPE_SERVICE;
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping()
    public ReturnService createEvent(@RequestBody CreateEventDto dto) {
        log.info("createDTo: " + dto);
        return EVENT_SERVICE.createEvent(dto);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PutMapping
    public ReturnService updateEvent(@RequestBody UpdateEventDto dto) {
        return EVENT_SERVICE.updateEvent(dto);
    }
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @DeleteMapping("")
    public ReturnService deleteEventById(@RequestParam Long eventId) {
        return EVENT_SERVICE.deleteEventById(eventId);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/my-events")
    public ReturnService getUserEvents() {
        return EVENT_SERVICE.getUserEvents();
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/my-events-history")
    public ReturnService getUserHistoryEvents() {
        return EVENT_SERVICE.getUserHistoryEvents();
    }

    @GetMapping("")
    public ReturnService getEventById(@RequestParam Long eventId) {
        return EVENT_SERVICE.getEventById(eventId);
    }

    @GetMapping("/all-events")
    public ReturnService getAllEvents(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                      @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                      @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir
                                      ) {
        return EVENT_SERVICE.getAllEvents(pageNo, pageSize, sortBy, sortDir, EventFilterParam.A, new String[] {""});
    }

    @GetMapping("/city")
    public ReturnService getEventByCity(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                        @RequestParam(value = "city", defaultValue = "", required = true) String city) {
        return EVENT_SERVICE.getAllEvents(pageNo, pageSize, sortBy, sortDir, EventFilterParam.C, new String[] {city});
    }

    @GetMapping("/date")
    public ReturnService getEventsByDate(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                         @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                         @RequestParam(value = "date", defaultValue = "", required = true) String date) {
        return EVENT_SERVICE.getAllEvents(pageNo, pageSize, sortBy, sortDir, EventFilterParam.D, new String[] {date});
    }

    @GetMapping("/owner")
    public ReturnService getEventsByOwner(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                         @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                         @RequestParam(value = "owner", defaultValue = "", required = true) String owner) {
        return EVENT_SERVICE.getAllEvents(pageNo, pageSize, sortBy, sortDir, EventFilterParam.O, new String[] {owner});
    }

    @GetMapping("/date-range")
    public ReturnService getEventsByOwner(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                          @RequestParam(value = "dateStart", defaultValue = "", required = true) String dateStart,
                                          @RequestParam(value = "dateEnd", defaultValue = "", required = true) String dateEnd) {
        return EVENT_SERVICE.getAllEvents(pageNo, pageSize, sortBy, sortDir, EventFilterParam.DD, new String[] {dateStart, dateEnd});
    }

    @GetMapping("/types")
    public ReturnService getEventsByTypes(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                          @RequestParam(value = "type", defaultValue = "", required = true) String types) {
        return EVENT_SERVICE.getAllEvents(pageNo, pageSize, sortBy, sortDir, EventFilterParam.T, new String[] {types});
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/member-history")
    public ReturnService getEventsByTypes(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir) {
        return EVENT_SERVICE.getAllEvents(pageNo, pageSize, sortBy, sortDir, EventFilterParam.MH, new String[] {});
    }

    @Transactional
    @GetMapping("/range")
    public ReturnService getEventsByRange(@RequestParam(value = "range", defaultValue = "", required = true) String range,
                                          @RequestParam(value = "userLat", defaultValue = "", required = true) String userLat,
                                          @RequestParam(value = "userLng", defaultValue = "", required = true) String userLng) {
        Double userLatD = Double.valueOf(userLat);
        Double userLngD = Double.valueOf(userLng);
        Double rangeD = Double.valueOf(range);
        return EVENT_SERVICE.getAllEventsInRange(userLatD, userLngD, rangeD);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/participation")
    public ReturnService eventParticipation(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return EVENT_SERVICE.getAllEvents(pageNo, pageSize, sortBy, sortDir, EventFilterParam.P, new String[] {email});
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping(value = "/qr-code", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getQrCodeEvent(@RequestParam Long eventId) {
       return EVENT_SERVICE.getQrCodeEvent(eventId);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping(value = "/get-event-types")
    public ReturnService getAllEventTypes() {
        return EVENT_TYPE_SERVICE.getAllEventTypesInformation();
    }

}
