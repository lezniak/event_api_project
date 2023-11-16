package com.example.praca.controller;

import com.example.praca.dto.common.IdDto;
import com.example.praca.dto.ticket.TicketContentDto;
import com.example.praca.dto.ticket.TicketFilterParam;
import com.example.praca.service.ReturnService;
import com.example.praca.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService TICKET_SERVICE;
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping(value = "", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] createTicket(@RequestBody IdDto idDto) {
        return TICKET_SERVICE.createTicket(idDto);
    }
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("/check")
    public ReturnService checkTicket(@RequestBody TicketContentDto dto) {
        return TICKET_SERVICE.checkTicket(dto.getContent());
    }
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/my-tickets")
    public ReturnService getMyTickets(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                      @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                      @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                      @RequestParam(value = "used", defaultValue = "dsc", required = false) String used
    ) {
        return TICKET_SERVICE.getAllTickets(pageNo, pageSize, sortBy, sortDir, TicketFilterParam.AU, new String[] {used});
    }
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/my-event-ticket")
    public ReturnService getMyTicketInEvent(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                      @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                      @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                      @RequestParam(value = "used", defaultValue = "dsc", required = false) String used,
                                      @RequestParam(value = "eventId", defaultValue = "", required = true) String eventId
    ) {
        return TICKET_SERVICE.getAllTickets(pageNo, pageSize, sortBy, sortDir, TicketFilterParam.AUE, new String[] {used,eventId});
    }
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/tickets")
    public ReturnService getAllTicketInEvent(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                            @RequestParam(value = "eventId", defaultValue = "", required = true) String eventId,
                                             @RequestParam(value = "used", defaultValue = "dsc", required = false) String used
    ) {
        return TICKET_SERVICE.getAllTickets(pageNo, pageSize, sortBy, sortDir, TicketFilterParam.AUE, new String[] {used,eventId});
    }
}
