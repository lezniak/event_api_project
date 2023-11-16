package com.example.praca.controller;

import com.example.praca.dto.eventpost.CreateEventPostDto;
import com.example.praca.dto.eventpost.EventPostInformationDto;
import com.example.praca.dto.eventpost.UpdatePostDto;
import com.example.praca.dto.filter.EventPostFilterParam;
import com.example.praca.service.EventPostService;
import com.example.praca.service.ReturnService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/event-post")
@Slf4j
public class EventPostController {
    private final EventPostService EVENT_POST_SERVICE;
    @ApiResponse(code = 200, response = EventPostInformationDto.class, message = "No Content")
    @PostMapping("")
    public ReturnService addPost(@RequestBody CreateEventPostDto dto) {
        return EVENT_POST_SERVICE.createEventPost(dto);
    }

    @GetMapping("/posts")
    public ReturnService eventPosts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                            @RequestParam(value = "eventId") Long eventId) {
        return EVENT_POST_SERVICE.getAllEventsPosts(pageNo, pageSize, sortBy, sortDir, EventPostFilterParam.AP,eventId);
    }

    @GetMapping("/post-creator")
    public ReturnService creatorPost(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                            @RequestParam(value = "creatorId") Long eventId) {
        return EVENT_POST_SERVICE.getAllEventsPosts(pageNo, pageSize, sortBy, sortDir, EventPostFilterParam.AU,eventId);
    }

    @DeleteMapping("")
    public ReturnService deletePost(@RequestParam("eventId") Long eventId, @RequestParam("postId") Long postId) {
        return EVENT_POST_SERVICE.deletePost(eventId, postId);
    }

    @PutMapping("")
    public ReturnService updatePost(@RequestBody UpdatePostDto dto) {
        return EVENT_POST_SERVICE.updatePost(dto);
    }
}
