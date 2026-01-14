package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.EmailType;
import com.auhpp.event_management.constant.RedisPrefix;
import com.auhpp.event_management.dto.request.AuthenticationRequest;
import com.auhpp.event_management.dto.request.LogoutRequest;
import com.auhpp.event_management.dto.request.RegisterRequest;
import com.auhpp.event_management.dto.request.VerifyAndRegisterRequest;
import com.auhpp.event_management.dto.response.AuthenticationResponse;
import com.auhpp.event_management.dto.response.UserResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.UserMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.service.AuthenticationService;
import com.auhpp.event_management.service.EmailService;
import com.auhpp.event_management.service.OtpService;
import com.auhpp.event_management.service.UserService;
import com.auhpp.event_management.util.SecurityUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    EmailService emailService;
    AppUserRepository appUserRepository;
    OtpService otpService;
    UserService userService;
    PasswordEncoder passwordEncoder;
    RedisTemplate<String, String> stringValueRedisTemplate;
    UserMapper userMapper;

    @NonFinal
    @Value("${spring.jwt.signer-key}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${spring.jwt.valid-duration}")
    long VALID_DURATION;

    @NonFinal
    @Value("${spring.jwt.refreshable-duration}")
    long REFRESHABLE_DURATION;


    @Override
    public void sendRegistrationOtp(RegisterRequest registerRequest) {
        Optional<AppUser> appUser = appUserRepository.findByEmail(registerRequest.getEmail());
        if (appUser.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        String otpCode = otpService.generateOTP();
        otpService.saveOtp(registerRequest.getEmail(), otpCode);
        emailService.sendOtpEmail(registerRequest.getEmail(), EmailType.REGISTER, "Mã OTP xác nhận tài khoản");
    }

    @Override
    public void verifyAndCreateUser(VerifyAndRegisterRequest verifyAndRegisterRequest) {
        if (otpService.verifyOtp(verifyAndRegisterRequest.getEmail(), verifyAndRegisterRequest.getOtp())) {
            RegisterRequest registerRequest = RegisterRequest.builder()
                    .email(verifyAndRegisterRequest.getEmail())
                    .password(verifyAndRegisterRequest.getPassword())
                    .build();
            userService.createUser(registerRequest);
        } else {
            throw new AppException(ErrorCode.OTP_NOT_VALID);
        }
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        AppUser user = appUserRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        if (!user.getStatus()) {
            throw new AppException(ErrorCode.USER_LOCKED);
        }
        boolean valid = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!valid) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        appUserRepository.save(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        //create JWSVerifier pass SIGNER_KEY
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        //Get SIGNER_KEY from token request
        SignedJWT signedJWT = SignedJWT.parse(token);
        //Get expiry time
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        //verify
        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        // Handle access token
        String accessToken = logoutRequest.getAccessToken();
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            String jti = signedJWT.getJWTClaimsSet().getJWTID();
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            long currentTime = System.currentTimeMillis();
            long ttl = expirationTime.getTime() - currentTime;

            if (ttl > 0) {
                stringValueRedisTemplate.opsForValue().set(
                        RedisPrefix.TOKEN_BLACKLIST + jti,
                        "logged_out",
                        ttl,
                        TimeUnit.MILLISECONDS
                );
            }
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // Handle refresh token
        String email = SecurityUtil.getCurrentUserLogin();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        user.setRefreshToken(null);
        appUserRepository.save(user);
    }

    @Override
    public UserResponse getCurrentUserInfo() {
        String email = SecurityUtil.getCurrentUserLogin();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        return userMapper.toUserResponse(user);
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(refreshToken);
        String email = signedJWT.getJWTClaimsSet().getSubject();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        if (!user.getStatus()) {
            throw new AppException(ErrorCode.USER_LOCKED);
        }
        if (!user.getRefreshToken().equals(refreshToken)) {
            user.setRefreshToken(null);
            appUserRepository.save(user);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String newAccessToken = generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);
        user.setRefreshToken(newRefreshToken);
        appUserRepository.save(user);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private String generateAccessToken(AppUser user) {
        return generateToken(user, VALID_DURATION, true);
    }

    public String generateRefreshToken(AppUser user) {
        return generateToken(user, REFRESHABLE_DURATION, false);
    }

    @Override
    public String generateToken(AppUser user, long duration, boolean withScope) {
        //Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //Create claims
        JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("event.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.MINUTES).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString());
        if (withScope) {
            claimsSetBuilder.claim("scope", user.getRole().getName());
        }
        //Payload
        Payload payload = new Payload(claimsSetBuilder.build().toJSONObject());

        //create jwsObject
        JWSObject jwsObject = new JWSObject(header, payload);

        //sign token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error creating token", e);
        }
    }
}
