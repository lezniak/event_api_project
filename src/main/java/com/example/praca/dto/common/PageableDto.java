package com.example.praca.dto.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
public class PageableDto  {
    private List<?> objectList;
    public int pageNo;
    public int pageSize;
    public int totalElements;
    public int totalPages;
    public boolean last;

    public static <T> PageableDto of(List<?> objectList, Page<?> objectPage) {
        PageableDto dto = new PageableDto();
        dto.setObjectList(objectList);
        dto.setPageNo(objectPage.getNumber());
        dto.setPageSize(objectPage.getSize());
        dto.setTotalElements(objectPage.getTotalPages());
        dto.setLast(objectPage.isLast());

        return dto;

    }

}
