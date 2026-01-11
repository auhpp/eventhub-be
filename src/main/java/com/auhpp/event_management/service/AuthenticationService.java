package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.AuthenticationRequest;
import com.auhpp.event_management.dto.request.LogoutRequest;
import com.auhpp.event_management.dto.request.RegisterRequest;
import com.auhpp.event_management.dto.request.VerifyAndRegisterRequest;
import com.auhpp.event_management.dto.response.AuthenticationResponse;
import com.auhpp.event_management.entity.AppUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthenticationService {
    void sendRegistrationOtp(RegisterRequest registerRequest);

    void verifyAndCreateUser(VerifyAndRegisterRequest verifyAndRegisterRequest);

    String generateToken(AppUser user, long duration, boolean withScope);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(String refreshToken) throws ParseException, JOSEException;

    SignedJWT verifyToken(String token) throws ParseException, JOSEException;

    void logout(LogoutRequest logoutRequest);
}
