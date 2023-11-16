package com.example.praca.dto.organization;

import com.example.praca.model.Company;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class CompanyInformationDto {
    private Long id;
    private String name;

    public static CompanyInformationDto of (Company company) {
        CompanyInformationDto dto = new CompanyInformationDto();

        dto.setId(company.getId());
        dto.setName(company.getName());

        return dto;
    }
}
