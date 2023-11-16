package com.example.praca.controller;

import com.example.praca.dto.user.ResendMailConfirmationDto;
import com.example.praca.dto.user.*;
import com.example.praca.exception.SessionNotFoundException;
import com.example.praca.service.JwtTokenService;
import com.example.praca.service.ReturnService;
import com.example.praca.service.UserService;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daniel Lezniak
 */
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService USER_SERVICE;
    private final JwtTokenService JWT_TOKEN_SERVICE;
    @PostMapping(value = "")
    public ReturnService createUser(@RequestBody CreateUserDto dto) {
        return USER_SERVICE.createUser(dto);
    }

    /**
     *
     * @param token
     * @return InformationUserDto
     * Potwierdzenie użytkownika po rejestracji
     */
    @GetMapping("/confirm")
    public ReturnService confirmUser(@RequestParam("token") String token) {
        return USER_SERVICE.confirmUser(token);
    }

    /**
     * Potwierdzenie nowego email po zmiaaniu
     * @param token
     * @return
     */
    @GetMapping("/confirm-email")
    public ReturnService confirmNewEmail(@RequestParam("token") String token) {
        return USER_SERVICE.confirmMail(token);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PutMapping("/update")
    public ReturnService updateUser(@RequestBody UpdateUserDto dto) {
        return USER_SERVICE.updateUser(dto);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PutMapping("/change-password")
    public ReturnService updateUserPassword(@RequestBody UpdateUserPasswordDto dto) {
        return USER_SERVICE.updateUserPassword(dto);
    }


    /**
     * Potwierdzenie nowego hasła po zmianie
     * @param token
     * @return
     */
    @GetMapping("/confirm-password")
    public ReturnService confirmNewPassword(@RequestParam("token") String token) {
        return USER_SERVICE.confirmPassword(token);
    }


    /**
     * Ponowna wysyłka email z potwierdzeniem utworzenia konta
     * @param dto
     * @return RertunService
     */
    @PostMapping("/resend-mail")
    public ReturnService resendMail(@RequestBody ResendMailConfirmationDto dto) {
        return USER_SERVICE.resendConfirmationToken(dto);
    }

    @PostMapping("/login")
    public ReturnService loginUser(@RequestBody LoginUserDto dto) {
        return USER_SERVICE.loginUser(dto);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @DeleteMapping("/delete")
    public ReturnService deleteUser() {
        return USER_SERVICE.deleteUser(getUserEmail());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/ban-user")
    public ReturnService banUser(@RequestParam Long userId) {
        return USER_SERVICE.banUser(userId);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("")
    public ReturnService getUser() {
        return USER_SERVICE.userProfile(getUserEmail());
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("/log-out")
    public ReturnService logOut() {
        return USER_SERVICE.logOut(getUserEmail());
    }
    @PostMapping("/refresh-token")
    public ReturnService refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return JWT_TOKEN_SERVICE.refreshJwtToken(refreshTokenRequest.getRefreshToken());
    }

    @GetMapping("/get")
    public ReturnService getUserById(@RequestParam Long userId) {
        return USER_SERVICE.getUserById(userId);
    }

    private String getUserEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (StringUtil.isNullOrEmpty(email))
            throw new SessionNotFoundException();
        return email;
    }

}


