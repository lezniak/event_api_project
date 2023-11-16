package com.example.praca.controller;


import com.example.praca.dto.EventOrganizationFilter;
import com.example.praca.dto.organization.AddCompanyDto;
import com.example.praca.dto.organization.CreateOrganizationDto;
import com.example.praca.dto.organization.UpdateOrganizationDto;
import com.example.praca.dto.organization.UserInOrganizationDto;
import com.example.praca.service.OrganizationService;
import com.example.praca.service.ReturnService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/organization")
public class OrganizationController {
    private final OrganizationService ORGANIZATION_SERVICE;

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("")
    public ReturnService createOrganization(@RequestBody CreateOrganizationDto dto) {
        return ORGANIZATION_SERVICE.createOrganization(dto);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PutMapping("")
    public ReturnService updateOrganization(@RequestBody UpdateOrganizationDto dto) {
        return ORGANIZATION_SERVICE.updateOrganization(dto);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @DeleteMapping("")
    public ReturnService deleteOrganization(@RequestParam Long eventId, @RequestParam Long organizationId) {
        return ORGANIZATION_SERVICE.deleteOrganization(eventId, organizationId);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/all-organizations")
    public ReturnService getAllEvents(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                      @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                      @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                      @RequestParam(value = "eventId", defaultValue = "", required = false) String eventId
    ) {
        if (eventId.equals(""))
            return ORGANIZATION_SERVICE.getAllOrganization(pageNo, pageSize, sortBy, sortDir, EventOrganizationFilter.A, new String[] {""});
        return ORGANIZATION_SERVICE.getAllOrganization(pageNo, pageSize, sortBy, sortDir, EventOrganizationFilter.E, new String[]{eventId});
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("/get-company")
    public ReturnService getAllCompany() {
        return ORGANIZATION_SERVICE.getAllCompany();
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("/add-company")
    public ReturnService addCompanyToOrganization(AddCompanyDto dto) {
        return ORGANIZATION_SERVICE.addCompanyToOrganization(dto);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("/check-user")
    public ReturnService userInOrganization(@RequestBody UserInOrganizationDto dto) {
        return ORGANIZATION_SERVICE.userInOrganization(dto);
    }


}
