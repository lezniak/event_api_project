package com.example.praca.service;




import com.example.praca.dto.common.PageableDto;
import com.example.praca.dto.filter.EventPostFilterParam;
import com.example.praca.dto.eventpost.CreateEventPostDto;
import com.example.praca.dto.eventpost.EventPostInformationDto;
import com.example.praca.dto.eventpost.UpdatePostDto;

import com.example.praca.dto.filter.EventPostFilterParam;
import com.example.praca.exception.EventNotFoundException;
import com.example.praca.exception.InvalidEventOwnerException;
import com.example.praca.model.Event;
import com.example.praca.model.EventPost;
import com.example.praca.model.User;
import com.example.praca.repository.EventPostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
@Slf4j
public class EventPostService {
    private final UserService USER_SERVICE;
    private final EventService EVENT_SERVICE;
    private final EventPostRepository EVENT_POST_REPOSITORY;
    public ReturnService createEventPost(CreateEventPostDto dto) {
        Event event = EVENT_SERVICE.findEventById(dto.getEventId());
        if (!EVENT_SERVICE.isEventOwner(event.getUser().getId()))
            throw new InvalidEventOwnerException();


        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());

        try {
            EventPost eventPost = EventPost.of(dto,event, user);
            EventPostInformationDto eventPostInformationDto = EventPostInformationDto.of(EVENT_POST_REPOSITORY.save(eventPost));
            return ReturnService.returnInformation("Succ. create post", eventPostInformationDto, 1);
        } catch (Exception ex) {
            log.error("Err: create post event %s user %s", event.getId(), user.getId());
            return ReturnService.returnError("Err. create post: " + ex.getMessage(),-1);
        }
    }

    public ReturnService getAllEventsPosts(int pageNo, int pageSize, String sortBy, String sortDir, EventPostFilterParam eventPostFilterParam, Long param) {
        Sort sort = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<EventPost> eventPostPage = null;


        switch (eventPostFilterParam) {
            case AP:
                eventPostPage = EVENT_POST_REPOSITORY.findALlByEvent(param, pageable);
                break;
            case AU:
                eventPostPage = EVENT_POST_REPOSITORY.findAllByCreator(param, pageable);
                break;
        }
        if (eventPostPage == null)
            throw new NotFoundException("Post not found");
        List<EventPostInformationDto> dto = eventPostPage.stream()
                .map(x -> EventPostInformationDto.of(x))
                .collect(Collectors.toList());

        return ReturnService.returnInformation("Event posts", PageableDto.of(dto, eventPostPage), 1);
    }

    public ReturnService deletePost(Long eventId, Long postId) {
        EventPost eventPost = findByPostId(postId);
        if (!isPostOwner(eventPost.getUser().getId()))
            throw new InvalidEventOwnerException();
        try {
            EVENT_POST_REPOSITORY.delete(eventPost);
            return ReturnService.returnInformation("Succ. delete post", 1);
        } catch (Exception ex) {
            log.error("Delte post err. %s", postId);
            return ReturnService.returnError("err. delete post: " + ex.getMessage(), -1);
        }
    }

    private EventPost findByPostId(Long postId) {
        return EVENT_POST_REPOSITORY.findById(postId).orElseThrow(() -> new EventNotFoundException());
    }

    public ReturnService updatePost(UpdatePostDto dto) {
        EventPost eventPost = findByPostId(dto.getPostId());
        if (!isPostOwner(eventPost.getUser().getId()))
            throw new InvalidEventOwnerException();

        try {
            EventPost eventPostUpdated = EventPost.update(dto, eventPost);
            EVENT_POST_REPOSITORY.save(eventPostUpdated);
            return ReturnService.returnInformation("Succ. update post", EventPostInformationDto.of(eventPostUpdated),1);
        } catch (Exception ex) {
            log.error("Delte post err. %s", dto.getPostId());
            return ReturnService.returnError("err. update post: " + ex.getMessage(), -1);
        }
    }

    public boolean isPostOwner(Long postOwnerId) {
        Long userId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
        return postOwnerId == userId;
    }

}
