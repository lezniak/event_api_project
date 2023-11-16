package com.example.praca.service;

import com.example.praca.exception.TokenNotFoundException;
import com.example.praca.model.RefreshToken;
import com.example.praca.model.User;
import com.example.praca.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
public class JwtTokenService {
    private static String secret;
    private static int tokenExpirationTime;
    private RefreshTokenRepository refreshTokenRepository;
    private  RefreshTokenService refreshTokenService;

    @Value("${jwt.secret}")
    public void setSecret(String secret){
        JwtTokenService.secret = secret;
    }

    @Value("${jwt.jwtExpirationTime}")
    public void setTokenExpirationTime(int tokenExpirationTime) {
        JwtTokenService.tokenExpirationTime = tokenExpirationTime;
    }

    public static String generateJwtoken(String email, Claims claims) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(Date.from(Instant.now().plusMillis(tokenExpirationTime)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static void verifyJwtoken(String token) throws JwtException {
        Jwts.parser()
                .setSigningKey(secret)
                .parse(token.substring(7));
    }

    public static Claims getClaimsFromJwtoken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.substring(7))
                .getBody();
    }

    public static String updateJwToken(String jwToken) {
        Claims claims = getClaimsFromJwtoken(jwToken);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(Date.from(Instant.now().plusMillis(tokenExpirationTime)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static String getJwtokenFromHeader() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
    }

    public  ReturnService refreshJwtToken(String refreshToken) {
        int status = -1;
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(refreshToken);
        if (refreshTokenOptional.isEmpty())
            throw new TokenNotFoundException();

        ReturnService rs = refreshTokenService.verifyRefreshTokenExpiration(refreshTokenOptional.get());
        if (rs.getStatus() == 1) {
            User user = (User) rs.getRefreshToken().getUser();

            //Set claims for new JWToken
            Claims claims = new DefaultClaims();
            List<String> authorities = user.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList());
            claims.put("authorities", authorities);
            String jwtToken = generateJwtoken(user.getEmail(), claims);
            return ReturnService.returnInformation("Succ. new Jwtoken", jwtToken, 1);
        } else {
            return ReturnService.returnError("Err. Refresh token expired",status);
        }

    }
}