package com.example.praca.service;

import com.example.praca.dto.eventaddress.EventAddressDto;
import com.example.praca.dto.event.CreateEventDto;
import com.example.praca.dto.event.UpdateEventDto;
import com.example.praca.dto.user.CreateUserDto;
import com.example.praca.dto.user.UpdateUserDto;
import com.example.praca.dto.user.UpdateUserPasswordDto;
import com.example.praca.dto.hobby.AddHobbyToUserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
public class  ValidationService {
    private static Map<String, String> errList = new HashMap<>();
    private final static int MIN_LENGTH = 3;

    public static Map<String, String> createUserValidator(CreateUserDto dto) {

        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");

        if (!ServiceFunctions.isNull(dto.getEmail())) {
            if (!ServiceFunctions.validEmail(dto.getEmail()))
                errList.put("email", "Please provide correct email address");
        } else {
            errList.put("email", "Email cannot be null");
        }
        if (!ServiceFunctions.isNull(dto.getPhoneNumber())) {
            if (!ServiceFunctions.validPhoneNumber(dto.getPhoneNumber()))
                errList.put("phoneNumber", "Please provide correct phone number");
        } else {
            errList.put("phoneNumber", "Phone number cannot be null");
        }
        if (ServiceFunctions.isNull(dto.getName())) {
            errList.put("name", "Name cannot be null");
        } else if (dto.getName().length() < MIN_LENGTH) {
            errList.put("name", "Name should have at least 3 characters");
        }
        if (ServiceFunctions.isNull(dto.getPassword()))
            errList.put("password", "Password should not be empty");
        if (ServiceFunctions.isNull(dto.getMatchingPassword()))
            errList.put("matchingPassword", "Matching password should not be empty");

        if (!ServiceFunctions.isNull(dto.getPassword()) && !ServiceFunctions.isNull(dto.getMatchingPassword())) {
            if (!dto.getPassword().toLowerCase(Locale.ROOT).equals(dto.getMatchingPassword().toLowerCase()))
                errList.put("password", "Passwords should match");
        }
        if (!ServiceFunctions.isNull(dto.getBirthdate())) {
            Date birthDate = ServiceFunctions.stringToDate(dto.getBirthdate());
            LocalDateTime now = LocalDateTime.now().minusYears(18L);
            if (!birthDate.before(ServiceFunctions.dateTimeToDate(now))) {
                    errList.put("birthdate", "U are too young");
            }
        }

//        if (ServiceFunctions.isNull(dto.getCity()) || dto.getCity().length() < 2) {
//            errList.put("city", "City cannot be null");
//        }

        return errList;
    }

    public static Map<String, String> updateUserValidator(UpdateUserDto dto) {

        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");

        if (!ServiceFunctions.isNull(dto.getEmail())) {
            if (!ServiceFunctions.validEmail(dto.getEmail()))
                errList.put("email", "Please provide correct email address");
        } else {
            errList.put("email", "Email cannot be null");
        }

        if (!ServiceFunctions.isNull(dto.getPhoneNumber())) {
            if (!ServiceFunctions.validPhoneNumber(dto.getPhoneNumber()))
                errList.put("phoneNumber", "Please provide correct phone number");
        } else {
            errList.put("phoneNumber", "Phone number cannot be null");
        }

        if (ServiceFunctions.isNull(dto.getName())) {
            errList.put("name", "Name cannot be null");
        } else if (dto.getName().length() < MIN_LENGTH) {
            errList.put("name", "Name should have at least 3 characters");
        }

        return errList;
    }

    public static Map<String, String> updateUserPasswordValidator(UpdateUserPasswordDto dto) {

        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");

        if (!ServiceFunctions.isNull(dto.getEmail())) {
            if (!ServiceFunctions.validEmail(dto.getEmail()))
                errList.put("email", "Please provide correct email address");
        } else {
            errList.put("email", "Email cannot be null");
        }
        if (ServiceFunctions.isNull(dto.getPassword()))
            errList.put("password", "Password should not be empty");

        if (ServiceFunctions.isNull(dto.getMatchingPassword()))
            errList.put("matchingPassword", "Matching password should not be empty");

        if (!ServiceFunctions.isNull(dto.getPassword()) && !ServiceFunctions.isNull(dto.getMatchingPassword())) {
            if (!dto.getPassword().toLowerCase(Locale.ROOT).equals(dto.getMatchingPassword().toLowerCase()))
                errList.put("password", "Passwords should match");
        }
        return errList;
    }

    public static Map<String, String> addHobbyToUserValidator(AddHobbyToUserDto dto) {
        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");
        if (dto.getUserId() == null || dto.getUserId() < 0)
             errList.put("userId","User id cannot be null or less than 0");

        if (dto.getHobbies() == null) {
            errList.put("hobbies","Hobbies cannot be null");
        } else if (dto.getHobbies().size() == 0) {
            errList.put("hobbies", "Please select more hobbies");
        }
        return errList;

    }

    public static Map<String, String> createEventValidator(CreateEventDto dto) {
        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");

        if (ServiceFunctions.isNull(dto.getName())) {
            errList.put("name", "Name cannot be null");
        } else if (dto.getName().length() < MIN_LENGTH) {
            errList.put("name", "Name should have at least 3 characters");
        }

        if (ServiceFunctions.isNull(dto.getEventDescription())) {
            errList.put("eventDescription", "Event description cannot be null");
        } else if (dto.getEventDescription().length() < MIN_LENGTH) {
            errList.put("eventDescription", "Event description should have at least 3 characters");
        }

        if (ServiceFunctions.isNull(dto.getEventType())) {
            errList.put("eventTypes", "Event types cannot be null");
        }

        if (ServiceFunctions.isNull(dto.getMaxMembers())) {
            errList.put("maxMembers", "Max members description cannot be null");
        } else if (dto.getMaxMembers() <= 0) {
            errList.put("maxMembers", "Max members should be greater than 0");
        }

        if (ServiceFunctions.isNull(dto.getStartDate())) {
            errList.put("startDate", "Start date description cannot be null");
        }

        try {
            if (new SimpleDateFormat("yyyy-MM-dd").parse(dto.getStartDate()).before(new Date()))
                errList.put("startDate", "Wrong start date");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return errList;
    }

    public static Map<String, String> validateEventAddress(EventAddressDto dto) {
        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");

        if (ServiceFunctions.isNull(dto.getLat()))
            return Collections.singletonMap("lat", "Lat cannot be null");

        if (ServiceFunctions.isNull(dto.getLng()))
            return Collections.singletonMap("lng", "Lng cannot be null");

        if (ServiceFunctions.isNull(dto.getAddress())) {
            return Collections.singletonMap("address", "Address cannot be null");
        } else if (dto.getAddress().isEmpty() || dto.getAddress().length() < 1) {
            return Collections.singletonMap("address", "Invalid address");
        }

        if (ServiceFunctions.isNull(dto.getCity())) {
            return Collections.singletonMap("city", "City cannot be null");
        } else if (dto.getCity().isEmpty() || dto.getCity().length() < 1) {
            return Collections.singletonMap("city", "Invalid City");
        }

        return errList;
    }

    public static Map<String, String> updateEventValidator(UpdateEventDto dto) {
        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");

        if (ServiceFunctions.isNull(dto.getCreateEventDto().getName())) {
            errList.put("name", "Name cannot be null");
        } else if (dto.getCreateEventDto().getName().length() < MIN_LENGTH) {
            errList.put("name", "Name should have at least 3 characters");
        }

        if (ServiceFunctions.isNull(dto.getCreateEventDto().getEventDescription())) {
            errList.put("eventDescription", "Event description cannot be null");
        } else if (dto.getCreateEventDto().getEventDescription().length() < MIN_LENGTH) {
            errList.put("eventDescription", "Event description should have at least 3 characters");
        }

        if (ServiceFunctions.isNull(dto.getCreateEventDto().getEventType())) {
            errList.put("eventTypes", "Event types cannot be null");
        }

        if (ServiceFunctions.isNull(dto.getCreateEventDto().getMaxMembers())) {
            errList.put("maxMembers", "Max members description cannot be null");
        } else if (dto.getCreateEventDto().getMaxMembers() <= 0) {
            errList.put("maxMembers", "Max members should be greater than 0");
        }

        if (ServiceFunctions.isNull(dto.getCreateEventDto().getStartDate())) {
            errList.put("startDate", "Start date description cannot be null");
        }

        try {
            if (new SimpleDateFormat("yyyy-MM-dd").parse(dto.getCreateEventDto().getStartDate()).before(new Date()))
                errList.put("startDate", "Wrong start date");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (ServiceFunctions.isNull(dto.getEventId()))
            errList.put("event", "Event id cannot be null");

        return errList;
    }

}
