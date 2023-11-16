package com.example.praca.controller;

import com.example.praca.dto.filter.EventFilterParam;
import com.example.praca.dto.common.IdDto;
import com.example.praca.dto.hobby.AcceptEventMemberDto;
import com.example.praca.service.EventMemberService;
import com.example.praca.service.ReturnService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/event-member")
public class EventMemberController {
    private final EventMemberService EVENT_MEMBER_SERVICE;

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/members")
    public ReturnService eventParticipation(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                            @RequestParam(value = "eventId", defaultValue = "", required = true) Long eventId) {
        return EVENT_MEMBER_SERVICE.getAllEventMembers(pageNo, pageSize, sortBy, sortDir, EventFilterParam.EM, new String[] {String.valueOf(eventId)});
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/members/to-accept")
    public ReturnService eventParticipationToAccept(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                            @RequestParam(value = "eventId", defaultValue = "", required = true) Long eventId) {
        return EVENT_MEMBER_SERVICE.getAllEventMembers(pageNo, pageSize, sortBy, sortDir, EventFilterParam.TA, new String[] {String.valueOf(eventId)});
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/members/accepted")
    public ReturnService eventAcceptedMembers(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                    @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                                    @RequestParam(value = "eventId", defaultValue = "", required = true) Long eventId) {
        return EVENT_MEMBER_SERVICE.getAllEventMembers(pageNo, pageSize, sortBy, sortDir, EventFilterParam.ACCEPTED, new String[] {String.valueOf(eventId)});
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("/join")
    public ReturnService joinEvent(@RequestBody IdDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return EVENT_MEMBER_SERVICE.joinEvent(email, dto.getId());
    }
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("/leave")
    public ReturnService leaveEvent(@RequestParam IdDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return EVENT_MEMBER_SERVICE.leaveEvent(email, dto.getId());
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PutMapping("/accept")
    public ReturnService acceptMember(@RequestBody AcceptEventMemberDto acceptEventMemberDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return EVENT_MEMBER_SERVICE.acceptUser(email, acceptEventMemberDto);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PutMapping("/delete")
    public ReturnService deleteMemebr(@RequestBody AcceptEventMemberDto acceptEventMemberDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return EVENT_MEMBER_SERVICE.deleteMember(email, acceptEventMemberDto);
    }
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/in-event")
    public ReturnService eventParticipation(@RequestParam Long eventId, @RequestParam Long userId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return EVENT_MEMBER_SERVICE.userInformation(eventId, userId);
    }

}
