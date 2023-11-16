package com.example.praca.service;

import com.example.praca.exception.TokenNotFoundException;
import com.example.praca.exception.UserNotFoundException;
import com.example.praca.model.RefreshToken;
import com.example.praca.model.User;
import com.example.praca.repository.RefreshTokenRepository;
import com.example.praca.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository REFRESH_TOKEN_REPOSITORY;
    private final UserRepository USER_REPOSITORY;
    private static int refreshTokenExpirationTime;

    @Value("${jwt.refreshTokenExpirationTime}")
    public void setRefreshTokenExpirationTime(int refreshTokenExpirationTime) {
        RefreshTokenService.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public ReturnService createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        Optional<User> userOptional = USER_REPOSITORY.findById(userId);
        if (userOptional.isEmpty())
            throw new UserNotFoundException();

        refreshToken.setUser(userOptional.get());
        refreshToken.setExpirationDate(Instant.now().plusMillis(refreshTokenExpirationTime));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = REFRESH_TOKEN_REPOSITORY.save(refreshToken);

        return ReturnService.returnRefreshTokenInformation(refreshToken, 1);
    }

    public ReturnService verifyRefreshTokenExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpirationDate().compareTo(Instant.now()) < 0) {
            REFRESH_TOKEN_REPOSITORY.delete(refreshToken);
            return ReturnService.returnError("Refresh token was expired, please sign in again", 0);
        }
        return ReturnService.returnRefreshTokenInformation(refreshToken, 1);
    }

    public ReturnService deleteRefreshToken(Long userId) {
        RefreshToken refreshToken = findRefreshTokenByUserId(userId);
        try {
            REFRESH_TOKEN_REPOSITORY.delete(refreshToken);
            return ReturnService.returnInformation("Succ. delete refresh token", 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. delete refresh token: " + ex.getMessage(), -1);
        }
    }

    private RefreshToken findRefreshTokenByUserId(Long userId) {
        return REFRESH_TOKEN_REPOSITORY.findByUserId(userId)
                .orElseThrow(TokenNotFoundException::new);
    }


}
