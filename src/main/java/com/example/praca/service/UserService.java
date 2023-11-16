package com.example.praca.service;


import com.example.praca.dto.common.PageableDto;
import com.example.praca.dto.user.ResendMailConfirmationDto;
import com.example.praca.dto.filter.UserFilterParam;
import com.example.praca.dto.user.*;
import com.example.praca.exception.*;
import com.example.praca.model.*;
import com.example.praca.repository.*;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */

@Service
//@AllArgsConstructor
@Transactional
@Slf4j
public class UserService   {
    private final UserRepository USER_REPOSITORY;
    private final HobbyRepository HOBBY_REPOSITORY;
    private final UserRoleRepository USER_ROLE_REPOSITORY;
    private final RefreshTokenService REFRESH_TOKEN_SERVICE;
    private final ConfirmationTokenRepository CONFIRMATION_TOKEN_REPOSITORY;
    private final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER;
    private final ConfirmationTokenService CONFIRMATION_TOKEN_SERVICE;
    private final EmailService EMAIL_SERVICE;
    private final EventMembersRepository EVENT_MEMBERS_REPOSITORY;
    private final UserLastLocationRepository USER_LAST_LOCATION_REPOSITORY;

    public UserService(UserRepository USER_REPOSITORY, HobbyRepository HOBBY_REPOSITORY, UserRoleRepository USER_ROLE_REPOSITORY, RefreshTokenService REFRESH_TOKEN_SERVICE, ConfirmationTokenRepository CONFIRMATION_TOKEN_REPOSITORY, BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER, ConfirmationTokenService CONFIRMATION_TOKEN_SERVICE, EmailService EMAIL_SERVICE, EventMembersRepository EVENT_MEMBERS_REPOSITORY, UserLastLocationRepository USER_LAST_LOCATION_REPOSITORY) {
        this.USER_REPOSITORY = USER_REPOSITORY;
        this.HOBBY_REPOSITORY = HOBBY_REPOSITORY;
        this.USER_ROLE_REPOSITORY = USER_ROLE_REPOSITORY;
        this.REFRESH_TOKEN_SERVICE = REFRESH_TOKEN_SERVICE;
        this.CONFIRMATION_TOKEN_REPOSITORY = CONFIRMATION_TOKEN_REPOSITORY;
        this.BCRYPT_PASSWORD_ENCODER = BCRYPT_PASSWORD_ENCODER;
        this.CONFIRMATION_TOKEN_SERVICE = CONFIRMATION_TOKEN_SERVICE;
        this.EMAIL_SERVICE = EMAIL_SERVICE;
        this.EVENT_MEMBERS_REPOSITORY = EVENT_MEMBERS_REPOSITORY;
        this.USER_LAST_LOCATION_REPOSITORY = USER_LAST_LOCATION_REPOSITORY;
    }

    private Map<String, String> validationError = new HashMap<>();

    public ReturnService createUser(CreateUserDto dto) {
        if (!validationError.isEmpty())
            validationError.clear();
        validationError = ValidationService.createUserValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);

        if (userExist(dto.getEmail(), dto.getPhoneNumber())) {
            validationError.put("user", "User already exist");
            return ReturnService.returnError("error", validationError, dto, 0);
        }

        dto.setPassword(encodePassword(dto.getPassword()));
        Optional<UserRole> optionalRole = USER_ROLE_REPOSITORY.findByName("UNAUTHENTICATED_USER");

        if (optionalRole.isEmpty()) {
            throw new RoleNotFoundException();
        }
        dto.setUserRole(Collections.singletonList(optionalRole.get()));
        try {
            User user = USER_REPOSITORY.save(User.of(dto));

            final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(user);

            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
            Thread sendConfirmationThread = new Thread(() -> EMAIL_SERVICE.sendConfirmationToken(user.getName(),user.getEmail(), CONFIRMATION_TOKEN.getConfirmationToken()));

            sendConfirmationThread.start();

            return ReturnService.returnInformation("Succ. user created", InformationUserDto.ofAfterRegister(user), 1);
        } catch (Exception e) {
            log.error("Błąd podczas rejestracji użytkowika o adres email: "+ dto.getEmail() + "Err: " + e.getMessage());
            return ReturnService.returnError("Err. create user exception: " + e.getMessage(), -1);
        }
    }

    public ReturnService confirmUser(String token) {
        Optional<ConfirmationToken> optionalConfirmationToken = CONFIRMATION_TOKEN_REPOSITORY.findConfirmationTokenByConfirmationToken(token);
        if (optionalConfirmationToken.isEmpty())
            throw new TokenNotFoundException();

        User user = optionalConfirmationToken.get().getUser();
        user.setEnabled(true);
        Optional<UserRole> optionalUserRole = USER_ROLE_REPOSITORY.findByName("USER");
        if (optionalUserRole.isEmpty())
            return ReturnService.returnError("Can't find role", 0);

        /**
         * Usuwanie starej roli z listy oraz dodanie roli potwierdzonego użytkownika
         */
        List<UserRole> currentUserRoles = user.getRoles();
        currentUserRoles.removeIf(x -> x.getName().equals("UNAUTHENTICATED_USER"));

        currentUserRoles.add(optionalUserRole.get());
        user.setRoles(currentUserRoles);

        try {
            User activatedUser = USER_REPOSITORY.save(user);
            CONFIRMATION_TOKEN_REPOSITORY.deleteById(optionalConfirmationToken.get().getId());
            /**
             * Tworzenie nowego JWToken ponieważ zmieniła się rola użytkownika, która jest wykorzystywana do autoryzacji przy zapytaniach API
             */
            Claims claims = new DefaultClaims();
            List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            claims.put("authorities", authorities);
            String jwtToken = JwtTokenService.generateJwtoken(user.getEmail(), claims);
            InformationUserDto informationUserDto = InformationUserDto.of(activatedUser);
            informationUserDto.setToken(jwtToken);

            return ReturnService.returnInformation("User active", informationUserDto, 1);
        } catch (Exception ex) {
            log.error("Błąd podczas aktywowania użytkownika o id: %s", user.getId());
            return ReturnService.returnError("Err. User activation: " + ex.getMessage(), -1);
        }
    }

    public ReturnService confirmMail(String token) {
        Optional<ConfirmationToken> optionalConfirmationToken = CONFIRMATION_TOKEN_REPOSITORY.findConfirmationTokenByConfirmationToken(token);
        if (optionalConfirmationToken.isEmpty())
            throw new TokenNotFoundException();

        User user = optionalConfirmationToken.get().getUser();
        user.setLocked(false);
        user.setEnabled(true);
        try {
            User activatedUser = USER_REPOSITORY.save(user);
            CONFIRMATION_TOKEN_REPOSITORY.deleteById(optionalConfirmationToken.get().getId());
            return ReturnService.returnInformation("User active", InformationUserDto.of(activatedUser), 1);
        } catch (Exception ex) {
            log.error("Błąd podczas potwierdzania maila dla uzytkownika: %s oraz email: %s", user.getId(), user.getEmail());
            return ReturnService.returnError("Err. User activation: " + ex.getMessage(), -1);
        }
    }

    public ReturnService confirmPassword(String token) {
        Optional<ConfirmationToken> optionalConfirmationToken = CONFIRMATION_TOKEN_REPOSITORY.findConfirmationTokenByConfirmationToken(token);
        if (optionalConfirmationToken.isEmpty())
            throw new UserNotFoundException();

        User user = optionalConfirmationToken.get().getUser();
        user.setLocked(false);
        user.setEnabled(true);
        try {
            User activatedUser = USER_REPOSITORY.save(user);
            CONFIRMATION_TOKEN_REPOSITORY.deleteById(optionalConfirmationToken.get().getId());
            return ReturnService.returnInformation("User active", InformationUserDto.of(activatedUser), 1);
        } catch (Exception ex) {
            log.error("Wystąpił błąd podczas potwierdzania hasła dla użytkwoika: %s", user.getId());
            return ReturnService.returnError("Err. User activation: " + ex.getMessage(), -1);
        }
    }

    public ReturnService loginUser(LoginUserDto dto) {
        if (ServiceFunctions.isNull(dto.getEmail()) && ServiceFunctions.isNull(dto.getPassword()))
            return ReturnService.returnError("error", Collections.singletonMap("user", "Please enter phone number or email"), dto, 0);

//        if (ServiceFunctions.isNull(dto.getLat()) || ServiceFunctions.isNull(dto.getLng()))
//            return ReturnService.returnError("error", Collections.singletonMap("location", "Location is null"), dto, 0);

        if (!userExist(dto.getEmail(), dto.getPhoneNumber())) {
            dto.setEmail("");
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Incorrect login or password"), dto, 0);
        }
        /*
            Jeśli email jest null(logowanie przez numer telefonu) to szukamy po nr telefonu
         */
        Optional<User> optionalUser;
        optionalUser = USER_REPOSITORY.findByEmail(dto.getEmail()).or(() -> USER_REPOSITORY.findAllByPhoneNumber(dto.getPhoneNumber()));


        if (!optionalUser.get().isEnabled()) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Please active your account"), dto, 0);
        }

        if (!BCRYPT_PASSWORD_ENCODER.matches(dto.getPassword(), optionalUser.get().getPassword())) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Incorrect login or password"), dto, 0);
        }

        //Create refresh token
        RefreshToken refreshToken = REFRESH_TOKEN_SERVICE.createRefreshToken(optionalUser.get().getId()).getRefreshToken();
        String[] refreshTokenArray = new String[]{refreshToken.getToken()};
        InformationUserDto informationUserDto = InformationUserDto.of(optionalUser.get(), refreshTokenArray);

        //Create JWToken
        Claims claims = new DefaultClaims();
        List<String> authorities = informationUserDto.getAuthorities();
        claims.put("authorities", authorities);
        String jwtToken = JwtTokenService.generateJwtoken(informationUserDto.getEmail(), claims);
        informationUserDto.setToken(jwtToken);

        //Get last user location for nearby events
        if (dto.getLat() == null || dto.getLng() == null) {
            return ReturnService.returnInformation("Login success", informationUserDto, 1);
        }

        UserLastLogin userLastLogin = getLastUserLocation(optionalUser.get().getId());
        if (userLastLogin == null) {
            informationUserDto.setLastLat(null);
            informationUserDto.setLastLng(null);

            //Saving last user location
            saveUserLastLocation(optionalUser.get(), dto.getLat(), dto.getLng());
            return ReturnService.returnInformation("Login success", informationUserDto, 1);
        }

        informationUserDto.setLastLat(userLastLogin.getLat());
        informationUserDto.setLastLng(userLastLogin.getLng());
        updateUserLastLocation(optionalUser.get(), dto.getLat(), dto.getLng());

        return ReturnService.returnInformation("Login success", informationUserDto, 1);


    }

    public ReturnService updateUser(UpdateUserDto dto) {
        clearError();
        if (!userExist(dto.getId()))
            throw new UserNotFoundException();
        if (!isMyProfile(dto.getId()))
            return ReturnService.returnError("Not your profile",0);

        validationError = ValidationService.updateUserValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);

        User user = findUserById(dto.getId());
        String oldUserEmail = user.getEmail();

        try {
            User updatedUser = USER_REPOSITORY.save(User.updateUser(user, dto));
            if (!oldUserEmail.equals(dto.getEmail())) {
                updatedUser.setLocked(true);
                User updatedUserWithEmail = USER_REPOSITORY.save(updatedUser);
                final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(updatedUser);
                Thread sendConfirmationToken = new Thread(() -> {
                    CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
                    EMAIL_SERVICE.sendConfirmationTokenChangedEmail(updatedUser.getName(),updatedUser.getEmail(), CONFIRMATION_TOKEN.getConfirmationToken());
                    updatedUser.setLocked(true);
                    USER_REPOSITORY.save(updatedUser);
                });

                sendConfirmationToken.start();
                //Stworzenie JWToken na podstawie nowego maila
                Claims claims = new DefaultClaims();
                List<String> authorities = updatedUserWithEmail.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
                claims.put("authorities", authorities);
                String newJwtoken = JwtTokenService.generateJwtoken(updatedUserWithEmail.getEmail(), claims);
                return ReturnService.returnInformation("Your email changed, please confirm",InformationUserDto.of(updatedUserWithEmail, newJwtoken), 1);

            }
            return ReturnService.returnInformation("Succ. update user: ", InformationUserDto.of(updatedUser), 1);
        } catch (Exception ex) {
            log.error("Błąd podczas aktualizowania użytkowika o id: %s", user.getId());
            return ReturnService.returnError("Err. update user " + ex.getMessage(), -1);
        }
    }

    public ReturnService resendConfirmationToken(ResendMailConfirmationDto dto) {

        if (ServiceFunctions.isNull(dto)) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Object cannot be null"), dto, 0);
        }

        if (!ServiceFunctions.validEmail(dto.getEmail())) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Please enter correct email address"), dto, 0);
        }

        if (!userExist(dto.getEmail(), "")) {
            dto.setPassword("");
            throw new UserNotFoundException();
        }

        Optional<User> optionalUser = USER_REPOSITORY.findByEmail(dto.getEmail());
        if (optionalUser.get().isEnabled())
            return ReturnService.returnInformation("User already enabled", 1);
        final ConfirmationToken confirmationToken = new ConfirmationToken(optionalUser.get());
        try {
            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(confirmationToken);
            Thread resendConfirmationTokenThread = new Thread(() -> EMAIL_SERVICE.sendConfirmationToken(optionalUser.get().getName(),dto.getEmail(), confirmationToken.getConfirmationToken()));

            resendConfirmationTokenThread.start();
            return ReturnService.returnInformation("Send success", 1);
        } catch (Exception ex) {
            log.error("Bład podczas wysyłki maila dla uzytkownika %s oraz email %s");
            return ReturnService.returnError("Err. send confirmation token " + ex.getMessage(), -1);
        }

    }

    public ReturnService updateUserPassword(UpdateUserPasswordDto dto) {
        clearError();
        if (ServiceFunctions.isNull(dto.getEmail())) {
            dto.setPassword("");
            dto.setMatchingPassword("");
            dto.setCurrentPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Email cannot be null"), dto, 0);
        }

        validationError = ValidationService.updateUserPasswordValidator(dto);
        if (!validationError.isEmpty()) {
            dto.setPassword("");
            dto.setMatchingPassword("");
            dto.setCurrentPassword("");
            return ReturnService.returnError("error", validationError, dto, 0);
        }

        ReturnService ret = loginUser(LoginUserDto.of(dto));
        if (ret.getStatus() != 1) {
            dto.setPassword("");
            dto.setMatchingPassword("");
            dto.setCurrentPassword("");
            return ReturnService.returnError("error", ret.getErrList(), dto, 0);
        }

        User oldUser = User.of((InformationUserDto) ret.getValue());
        oldUser.setPassword(encodePassword(dto.getPassword()));

        try {
            User user = USER_REPOSITORY.save(oldUser);
            oldUser.setLocked(true);
            USER_REPOSITORY.save(oldUser);
            final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(user);

            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
            Thread sendConfirmationThread = new Thread(() -> EMAIL_SERVICE.sendConfirmationTokenChangedPassword(user.getName(), user.getEmail(), CONFIRMATION_TOKEN.getConfirmationToken()));

            sendConfirmationThread.start();
            return ReturnService.returnInformation("Confirm new password on email ", 1);
        } catch (Exception ex) {
            log.error("Błąd aktualizacji hasłą dla uzytkownika: %s", oldUser.getId());
            return ReturnService.returnError("Err. update user password " + ex.getMessage(), -1);
        }

    }

    public ReturnService deleteUser(String email) {
        User user = findUserByEmail(email);

        if (!isMyProfile(user.getId()))
            return ReturnService.returnError("Not your profile",0);

        try {
            USER_REPOSITORY.delete(user);
            HOBBY_REPOSITORY.deleteAllByUsers(user);
            leaveAllEvents(user.getId());
            return ReturnService.returnInformation("Succ. User deleted", 1);
        } catch (Exception ex) {
            log.error("Błąd usuwania uztkownika dla uzytkownika: %s", user.getId());
            return ReturnService.returnError("Err. create user exception: " + ex.getMessage(), -1);
        }

    }

    public ReturnService banUser(Long userId) {
        User user = findUserById(userId);
        user.setLocked(true);
        try {
            USER_REPOSITORY.save(user);
            return ReturnService.returnInformation("Succ. ban user", InformationUserDto.of(user), 1);
        } catch (Exception ex) {
            log.error("Błąd blokowania uztkownika dla uzytkownika: %s", user.getId());
            return ReturnService.returnError("Err. ban user: " + ex.getMessage(), -1);
        }
    }

    public ReturnService userProfile(String email) {
        if (ServiceFunctions.isNull(email))
            throw new UserNotFoundException();

        User user = findUserByEmail(email);
        InformationUserDto dto =  InformationUserDto.of(user);
        return ReturnService.returnInformation("Succ.",dto, 1);

    }

    private  boolean isMyProfile(Long userId) {
        String token = JwtTokenService.getJwtokenFromHeader();
        Claims claims = new DefaultClaims();

        if (StringUtils.isNotBlank(token) && !token.isEmpty())
             claims = JwtTokenService.getClaimsFromJwtoken(token);

        Optional<User> user = USER_REPOSITORY.findByEmail(claims.get("sub").toString());
        if (user.isEmpty())
            return false;

        return userId == user.get().getId();

    }

    private boolean userExist(String email, String phoneNumber) {
        return USER_REPOSITORY.findByEmail(email).isPresent() || USER_REPOSITORY.findAllByPhoneNumber(phoneNumber).isPresent();
    }

    private String encodePassword(String password) {
        return BCRYPT_PASSWORD_ENCODER.encode(password);
    }

    public boolean userExist(Long id) {
        return USER_REPOSITORY.findById(id).isPresent();
    }

    private void clearError() {
        validationError.clear();
    }

    public  User findUserById(Long id) {
        return USER_REPOSITORY.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User findUserByEmail(String email) {
        return USER_REPOSITORY.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public String getUserEmailFromJwt() {
        String token = JwtTokenService.getJwtokenFromHeader();
        Claims claims = new DefaultClaims();

        if (StringUtils.isNotBlank(token) && !token.isEmpty())
            claims = JwtTokenService.getClaimsFromJwtoken(token);

        return claims.get("sub").toString();
    }

    public void userAlreadyJoin(Long userId, Long eventId) {
        EVENT_MEMBERS_REPOSITORY.findByUserIdAndEventId(userId, eventId)
                .ifPresent(x -> {
                    throw new AlreadyJoinEventException();
                });
    }

    public void userInEvent(Long userId, Long eventId) {
        EVENT_MEMBERS_REPOSITORY.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(UserNotFoundInEvent::new);
    }

    public ReturnService leaveAllEvents(Long userId) {
        List<Long> eventMemebersIdList = EVENT_MEMBERS_REPOSITORY.findAllByUserId(userId).stream()
                .map(x -> x.getId())
                .collect(Collectors.toList());
        try {
            EVENT_MEMBERS_REPOSITORY.deleteAllById(eventMemebersIdList);
            return ReturnService.returnInformation("Succ. leave evemt", 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. leave event: " + ex.getMessage(), -1);
        }
    }

    public ReturnService logOut(String email) {
        SecurityContextHolder.clearContext();
        User user = findUserByEmail(email);
        ReturnService rs = REFRESH_TOKEN_SERVICE.deleteRefreshToken(user.getId());
        return rs;
    }

    public ReturnService getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir, UserFilterParam userFilterParam, String... value) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> userPage = null;

        switch (userFilterParam) {
            case AH:
                Long hobbyId = Long.valueOf(value[0]);
                userPage = USER_REPOSITORY.findAllByHobby(hobbyId, pageable);
            break;

            case AHN:
                Long hobbyIdN = Long.valueOf(value[0]);
                boolean newsletter = Boolean.getBoolean(value[1]);
                userPage = USER_REPOSITORY.findAllByHobbyAndNewsletter(hobbyIdN, true, pageable);
        }

        if (userPage == null)
            throw new UserNotFoundException();
        List<User> userList = userPage.getContent();
        List<InformationUserDto> informationUserDtos = userList.stream()
                .map(x -> InformationUserDto.of(x))
                .collect(Collectors.toList());

        return ReturnService.returnInformation("", PageableDto.of(informationUserDtos,userPage), 1);
    }

    public ReturnService getUserById(Long userId) {
        User user = USER_REPOSITORY.findById(userId).orElseThrow(UserNotFoundException::new);
        return ReturnService.returnInformation("", InformationUserDto.of(user),1);
    }

    private void updateUserLastLocation(User user, Double lat, Double lng) {
        UserLastLogin userLastLogin = getLastUserLocation(user.getId());

        UserLastLogin updatedLastLogin = new UserLastLogin();

        //update values
        updatedLastLogin.setLat(lat);
        updatedLastLogin.setLng(lng);

        try {
            USER_LAST_LOCATION_REPOSITORY.save(UserLastLogin.update(userLastLogin, updatedLastLogin));
        } catch (Exception ex) {
            log.error(String.format("Err. update last user location for user: %s", user.getId()));
        }
    }

    private void saveUserLastLocation(User user, Double lat, Double lng) {
        UserLastLogin userLastLogin = new UserLastLogin();

        userLastLogin.setUser(user);
        userLastLogin.setLat(lat);
        userLastLogin.setLng(lng);

        try {
            USER_LAST_LOCATION_REPOSITORY.save(userLastLogin);
        } catch (Exception ex) {
            log.error(String.format("Err. update last user location for user: %s", user.getId()));
        }
    }

    private UserLastLogin getLastUserLocation(Long userId) {
        return USER_LAST_LOCATION_REPOSITORY.findByUserId(userId).orElse(null);
    }

    public Long getUserIdByEmail(String email) {
        return USER_REPOSITORY.findByEmail(email).orElseThrow(() -> new UserNotFoundException()).getId();
    }

    public String getUserPhoneNumberByEmail(String id) {
        return USER_REPOSITORY.findUserPhoneNumberById(id);
    }

    public List<User> findAllUsersByEmailDomain(String email) {
        return USER_REPOSITORY.findByEmailEndingWith(email);
    }
}
