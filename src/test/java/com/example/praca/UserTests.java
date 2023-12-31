package com.example.praca;

import com.example.praca.controller.UserController;
import com.example.praca.dto.*;
import com.example.praca.dto.hobby.DeleteHobbyUserDto;
import com.example.praca.dto.user.CreateUserDto;
import com.example.praca.dto.user.InformationUserDto;
import com.example.praca.service.ReturnService;
import com.example.praca.service.UserService;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Lezniak
 */
//@ExtendWith(MockitoExtension.class)
public class UserTests {
//
//    @InjectMocks
//    private UserController userController;
//
//    @Mock
//    private UserService userService;
//
//    @Test
//    void registerUser() {
//        CreateUserDto createUserDto = UserRegisterMock.mockUser();
//        InformationUserDto informationUserDto = UserRegisterMock.mockUserInformation();
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ReturnService rs = new ReturnService();
//        rs.setValue(informationUserDto);
//        rs.setStatus(1);
//        when(userService.createUser(any(CreateUserDto.class))).thenReturn(rs);
//        ReturnService returnService = userController.createUser(createUserDto);
//        assertThat(returnService.getStatus() == 1);
//        assertThat(returnService.getValue() == rs.getValue());
//    }

//    @Autowired
//    UserService userService;
//
//
//    @Test
//    public void validationTest() {
//        CreateUserDto dto = new CreateUserDto();
//        Map<String, String> errList = new HashMap<>();
//        errList.put("password", "Password should match");
//        errList.put("email", "Please provide correct email address");
//        errList.put("password", "Passwords should match");
//        errList.put("name", "Name should have at least 3 characters");
//
//        dto.setEmail("asd@");
//        dto.setName("a");
//        dto.setPassword("test1");
//        dto.setMatchingPassword("asd");
//        dto.setPhoneNumber("asdasd");
//
//        ReturnService ret = userService.createUser(dto);
//        Map<String, String> errListRet = ret.getErrList();
//
//        assertEquals(0, ret.getStatus());
//        assertEquals(errListRet.get("name"),errList.get("name"));
//        assertEquals(errListRet.get("email"),errList.get("email"));
//        assertEquals(errListRet.get("password"),errList.get("password"));
//    }
//
//    @Test
//    public void nullTest() {
//        CreateUserDto dto = null;
//        Map<String, String> errLIst = new HashMap<>();
//        errLIst.put("object", "Object cannot be null");
//        ReturnService ret = userService.createUser(dto);
//        Map<String, String> errListRet = ret.getErrList();
//        assertEquals(0, ret.getStatus());
//        assertEquals(errLIst.get("object"), errListRet.get("object"));
//    }
//
//    @Test
//    public void nullTestField() {
//        CreateUserDto dto = new CreateUserDto();
//        dto.setEmail(null);
//        dto.setPhoneNumber(null);
//        dto.setPassword(null);
//        dto.setMatchingPassword(null);
//        dto.setName(null);
//
//        Map<String, String> errList = new HashMap<>();
//        errList.put("email", "Email cannot be null");
//        errList.put("phoneNumber", "Phone number cannot be null");
//        errList.put("name", "Name cannot be null");
//        errList.put("password", "Password should not be empty");
//
//        ReturnService ret = userService.createUser(dto);
//        Map<String, String> errListRet = ret.getErrList();
//
//        assertEquals(0, ret.getStatus());
//        assertEquals(errList.get("name"),errListRet.get("name"));
//        assertEquals(errList.get("email"),errListRet.get("email"));
//        assertEquals(errList.get("password"),errListRet.get("password"));
//        assertEquals(errList.get("phoneNumber"),errListRet.get("phoneNumber"));
//    }
//
//    @Test
//    public void createUserTest() {
//        CreateUserDto dto = new CreateUserDto();
//        dto.setPassword("test1");
//        dto.setMatchingPassword("test1");
//        dto.setEmail("dlezniak@gmail.com");
//        dto.setPhoneNumber("+48514702363");
//        dto.setName("danieeeel");
//
//        InformationUserDto dto1 = new InformationUserDto();
//        dto1.setEmail("dlezniak@gmail.com");
//        dto1.setPhoneNumber("+48514702363");
//        dto1.setName("danieeeel");
//        ReturnService ret = userService.createUser(dto);
//        assertEquals(1, ret.getStatus());
//        assertEquals(dto1.getEmail(), dto.getEmail());
//        assertEquals(dto1.getName(), dto.getName());
//        assertEquals(dto1.getPhoneNumber(), dto.getPhoneNumber());
//
//    }
//
//    @Test
//    public void userAlreadyExistTest() {
//        CreateUserDto dto = new CreateUserDto();
//        dto.setPassword("test1");
//        dto.setMatchingPassword("test1");
//        dto.setEmail("dlezniak@gmail.com");
//        dto.setPhoneNumber("+48514702363");
//        dto.setName("danieeeel");
//
//        String errMsg = "User already exist";
//
//        ReturnService ret = userService.createUser(dto);
//        assertEquals(0, ret.getStatus());
//        assertEquals(errMsg,ret.getErrList().get("user"));
//
//    }
//
//    @Test
//    public void resendEmailIncorrect() {
//        ResendMailConfirmationDto dto = new ResendMailConfirmationDto();
//        dto.setEmail("d1d.pl");
//        dto.setPassword("test1");
//
//        ReturnService ret = userService.resendConfirmationToken(dto);
//        assertEquals(0,ret.getStatus());
//        assertEquals("Please enter correct email address", ret.getErrList().get("user"));
//    }
//    @Test
//    public void resendEmailError() {
//        ResendMailConfirmationDto dto = new ResendMailConfirmationDto();
//        dto.setEmail("dlezniak@wp.pl");
//        dto.setPassword("test1");
//
//        ReturnService ret = userService.resendConfirmationToken(dto);
//        assertEquals(0,ret.getStatus());
//        assertEquals("Can't find user with given email", ret.getErrList().get("user"));
//    }
//
//    @Test
//    public void resendEmailNull() {
//        ResendMailConfirmationDto dto = new ResendMailConfirmationDto();
//        dto.setEmail(null);
//        dto.setPassword(null);
//
//        ReturnService ret = userService.resendConfirmationToken(dto);
//        assertEquals(0,ret.getStatus());
//        assertEquals("Please enter correct email address", ret.getErrList().get("user"));
//    }
//
//    @Test
//    public void resendEmail() {
//        ResendMailConfirmationDto dto = new ResendMailConfirmationDto();
//        dto.setEmail("dlezniak@gmail.com");
//        dto.setPassword("test1");
//
//        ReturnService ret = userService.resendConfirmationToken(dto);
//        assertEquals(1,ret.getStatus());
//        assertEquals("Send success", ret.getMessage());
//    }
//
//    @Test
//    public void userLoginValidationError() {
//        LoginUserDto dto = new LoginUserDto();
//        dto.setEmail("");
//        dto.setPassword("");
//        dto.setPhoneNumber("");
//
//        ReturnService ret = userService.loginUser(dto);
//        assertEquals("Please enter phone number or email", ret.getErrList().get("user"));
//        assertEquals(0, ret.getStatus());
//    }
//
//    @Test
//    public void userLoginIncorrectTest() {
//        LoginUserDto dto = new LoginUserDto();
//        dto.setEmail("kaaaaar@wp.pl");
//        dto.setPassword("test1");
//        dto.setPhoneNumber("");
//
//        ReturnService ret = userService.loginUser(dto);
//        assertEquals("Incorrect login or password", ret.getErrList().get("user"));
//        assertEquals(0, ret.getStatus());
//    }
//
//    @Test
//    public void userLoginTest() {
//        LoginUserDto dto = new LoginUserDto();
//        dto.setEmail("dlezniak@gmail.com");
//        dto.setPassword("test1");
//        dto.setPhoneNumber("");
//
//        ReturnService ret = userService.loginUser(dto);
//        InformationUserDto dtoR = (InformationUserDto)ret.getValue();
//        assertEquals("danieeeel", dtoR.getName());
//        assertEquals("dlezniak@gmail.com", dtoR.getEmail());
//        assertEquals("+48514702363", dtoR.getPhoneNumber());
//        assertEquals(1, ret.getStatus());
//    }
//    @Test
//    public void updateUserValidationErrorTest() {
//        UpdateUserDto dto = new UpdateUserDto();
//        dto.setId(1L);
//        dto.setPhoneNumber("+48743");
//        dto.setEmail("dez.pl");
//        dto.setName("Rw");
//        dto.setPassword("test");
//
//        Map<String, String> errList = new HashMap<>();
//        errList.put("password", "Password should match");
//        errList.put("email", "Please provide correct email address");
//        errList.put("password", "Passwords should match");
//        errList.put("name", "Name should have at least 3 characters");
//
//
//        ReturnService ret = userService.updateUser(dto);
//        Map<String, String> errListRet = ret.getErrList();
//
//        assertEquals(0, ret.getStatus());
//        assertEquals(errList.get("name"),errListRet.get("name"));
//        assertEquals(errList.get("email"),errListRet.get("email"));
//        assertEquals(errList.get("password"),errListRet.get("password"));
//        assertEquals(errList.get("phoneNumber"),errListRet.get("phoneNumber"));
//
//    }
//    //TODO fix test
//    @Test
//    public void updateUserErrorTest() {
//        UpdateUserDto dto = new UpdateUserDto();
//        dto.setId(3L);
//        dto.setPhoneNumber("+48741852963");
//        dto.setEmail("dlezniak@gmail.com");
//        dto.setName("Radosław");
//        dto.setPassword("test");
//
//        ReturnService ret = userService.updateUser(dto);
//        assertEquals(0,ret.getStatus());
//        if (ret.getErrList() != null)
//            assertEquals("Can't find user with given id",ret.getErrList().get("user"));
//
//
//    }
//
//    @Test
//    public void updateUserTest() {
//        UpdateUserDto dto = new UpdateUserDto();
//        dto.setId(1L);
//        dto.setPhoneNumber("+48741852963");
//        dto.setEmail("mentalnyyy@gmail.com");
//        dto.setName("Radosła");
//
//        InformationUserDto retDto = new InformationUserDto();
//        retDto.setId(8L);
//        retDto.setPhoneNumber("+48741852963");
//        retDto.setEmail("mentalnyyy@gmail.com");
//        retDto.setName("Radosław");
//
//        ReturnService ret = userService.updateUser(dto);
//        assertEquals(1,ret.getStatus());
//        assertEquals("Succ. update user:",ret.getMessage());
//
//    }
//
//
//    @Test
//    public void updateUserPasswordValidation() {
//        UpdateUserPasswordDto dto = new UpdateUserPasswordDto();
//        dto.setId(8L);
//        dto.setEmail("dez.pl");
//        dto.setPassword("test");
//        dto.setMatchingPassword("test1");
//
//        Map<String, String> errList = new HashMap<>();
//        errList.put("password", "Password should match");
//        errList.put("email", "Please provide correct email address");
//        errList.put("password", "Passwords should match");
//
//
//
//        ReturnService ret = userService.updateUserPassword(dto);
//        Map<String, String> errListRet = ret.getErrList();
//
//        assertEquals(0, ret.getStatus());
//        assertEquals(errList.get("email"),errListRet.get("email"));
//        assertEquals(errList.get("password"),errListRet.get("password"));
//
//
//    }
//
//    @Test
//    public void updateUserPasswordNull() {
//        UpdateUserPasswordDto dto = new UpdateUserPasswordDto();
//        dto.setId(null);
//        dto.setEmail(null);
//        dto.setPassword(null);
//
//
//        ReturnService ret = userService.updateUserPassword(dto);
//        Map<String, String> errListRet = ret.getErrList();
//
//        assertEquals(0, ret.getStatus());
//        assertEquals("Email cannot be null",errListRet.get("user"));
////        "password", "Password should not be empty"
//        assertEquals("Email cannot be null",errListRet.get("user"));
//
//    }
//
//    @Test
//    public void updateUserPasswordNull1() {
//        UpdateUserPasswordDto dto = new UpdateUserPasswordDto();
//        dto.setId(8L);
//        dto.setEmail("mentalnyyy@gmail.com");
//        dto.setCurrentPassword("test1");
//        dto.setPassword("null");
//        dto.setMatchingPassword("null1");
//
//
//        ReturnService ret = userService.updateUserPassword(dto);
//        Map<String, String> errListRet = ret.getErrList();
//
//        assertEquals(0, ret.getStatus());
//        assertEquals("Passwords should match",errListRet.get("password"));
//
//    }
//
//    @Test
//    public void updateUserPassword() {
//        UpdateUserPasswordDto dto = new UpdateUserPasswordDto();
//        dto.setId(4L);
//        dto.setEmail("dlezniak@gmail.com");
//        dto.setCurrentPassword("test1");
//        dto.setPassword("test");
//        dto.setMatchingPassword("test");
//
//        ReturnService ret = userService.updateUserPassword(dto);
//
//        assertEquals(1, ret.getStatus());
//        assertEquals("Confirm new password on email", ret.getMessage());
//
//    }
//
//    @Test
//    public void deleteUserTest() {
//        ReturnService ret = userService.deleteUser(6L);
//        assertEquals(1, ret.getStatus());
//        assertEquals("Succ. User deleted", ret.getMessage());
//
//    }
//
//    @Test
//    public void aaa() {
//        DeleteHobbyUserDto dto = new DeleteHobbyUserDto();
//        dto.setUserId(null);
//        dto.setHobbyId(3L);
//        boolean ret = userService.test(dto);
//        assertEquals(true, ret);
//    }

}
