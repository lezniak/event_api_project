package com.example.praca.service;

import com.example.praca.dto.hobby.HobbyInformationDto;
import com.example.praca.dto.hobby.AddHobbyToUserDto;
import com.example.praca.dto.hobby.DeleteHobbyUserDto;
import com.example.praca.exception.HobbyNotFoundException;
import com.example.praca.model.Hobby;
import com.example.praca.model.User;
import com.example.praca.repository.HobbyRepository;
import com.example.praca.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
public class HobbyService {
    private final HobbyRepository HOBBY_REPOSITORY;
    private final UserRepository USER_REPOSITORY;
    private final UserService USER_SERVICE;
    private Map<String, String> validationError;

    public ReturnService addHobbyToUser(AddHobbyToUserDto dto) {
        List<Hobby> hobbyList = new ArrayList<>();
        List<Hobby> userHobbiesList = new ArrayList<>();

        validationError = ValidationService.addHobbyToUserValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);

        USER_SERVICE.findUserById(dto.getUserId());
        for (Long id : dto.getHobbies()) {
            if (!hobbyExist(id)) {
                validationError.put("hobby", "Can't find hobby with given id");
                return ReturnService.returnError("error", validationError, dto, 0);
            }
        }
        User user = USER_REPOSITORY.findById(dto.getUserId()).get();
        userHobbiesList = user.getHobbies();

        hobbyList = HOBBY_REPOSITORY.findAllById(dto.getHobbies());

        hobbyList.removeAll(new HashSet(userHobbiesList));

        //ADd new hobby for user
        userHobbiesList.addAll(hobbyList);

        try {
            user.setHobbies(userHobbiesList);
            USER_REPOSITORY.save(user);
            return ReturnService.returnInformation("Succ. hobby added", dto, 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. add hobby to user exception: " + ex.getMessage(), -1);
        }

    }
    @Transactional
    public boolean hobbyExist(Long id) {
        return HOBBY_REPOSITORY.findById(id).isPresent();
    }

    public ReturnService deleteHobbyUser(DeleteHobbyUserDto dto) {
        Optional<Hobby> hobbyOptional = HOBBY_REPOSITORY.findById(dto.getHobbyId());
        if (hobbyOptional.isEmpty())
            return ReturnService.returnError("Can't find hobby with given id", 0);

        try {
            HOBBY_REPOSITORY.deleteById(dto.getHobbyId());
            return ReturnService.returnInformation("Succ. delete hobby: ", 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. delete hobby: " + ex.getMessage(), -1);
        }

    }

    public List<HobbyInformationDto> getAllHobbies() {
        return HOBBY_REPOSITORY.getAllHobbies().orElseThrow(() -> new HobbyNotFoundException())
                .stream()
                .map(x -> HobbyInformationDto.of(x))
                .collect(Collectors.toList());
    }

    public Long getHobbyIdByName(String name) {
        return HOBBY_REPOSITORY.getHobbyIdByName(name).orElseThrow(() -> new HobbyNotFoundException());
    }
}
