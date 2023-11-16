package com.example.praca.dto.user;

import com.example.praca.model.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Data
public class NewsletterUserDto {
    private Long id;
    private String email;
    private List<Long> hobbyIds;

    public static NewsletterUserDto of(User user) {
        NewsletterUserDto dto = new NewsletterUserDto();

        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        dto.setHobbyIds(user.getHobbies().stream()
                .map(x -> x.getId())
                .collect(Collectors.toList()));

        return dto;
    }
}
