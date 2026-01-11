package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.RedisPrefix;
import com.auhpp.event_management.service.OtpService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    RedisTemplate<String, String> stringValueRedisTemplate;
    static long OTP_TTL_MINUTES = 5;

    @Override
    public String generateOTP() {
        return String.valueOf((int) ((Math.random() * 900000) + 100000));
    }

    @Override
    public void saveOtp(String email, String otpCode) {
        String key = RedisPrefix.OTP_KEY + email;
        stringValueRedisTemplate.opsForValue().set(key, otpCode, OTP_TTL_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public String getOtp(String email) {
        String key = RedisPrefix.OTP_KEY + email;
        return stringValueRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteOtp(String email) {
        String key = RedisPrefix.OTP_KEY + email;
        stringValueRedisTemplate.delete(key);
    }

    @Override
    public boolean verifyOtp(String email, String otpCode) {
        String storedOtp = getOtp(email);
        if (storedOtp == null) {
            return false;
        }
        if (storedOtp.equals(otpCode)) {
            deleteOtp(email);
            return true;
        }
        return false;
    }


}
