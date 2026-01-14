package com.auhpp.event_management.util;

import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtil {
    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject(); // return email
        }
        return null;
    }

    public static void isOwner(AppUser appUser) {
        if (!appUser.getEmail().equals(getCurrentUserLogin())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
    }
}
