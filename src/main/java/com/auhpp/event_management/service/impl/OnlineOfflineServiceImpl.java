package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.RedisPrefix;
import com.auhpp.event_management.mapper.UserBasicMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.service.OnlineOfflineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OnlineOfflineServiceImpl implements OnlineOfflineService {

    AppUserRepository appUserRepository;
    SimpMessageSendingOperations simpMessageSendingOperations;
    UserBasicMapper userBasicMapper;
    RedisTemplate<String, String> stringValueRedisTemplate;
    static long TTL_MINUTES = 5;

    @Override
    @Transactional
    public void addOnlineUser(String userEmail, String sessionId) {
        String redisKey = RedisPrefix.ONLINE_USERS.getValue() + userEmail;

        stringValueRedisTemplate.opsForSet().add(redisKey, sessionId);
        stringValueRedisTemplate.expire(redisKey, Duration.ofMinutes(TTL_MINUTES));

        Long sessionCount = stringValueRedisTemplate.opsForSet().size(redisKey);

        if (sessionCount != null && sessionCount == 1) {
            log.info("{} is currently ONLINE", userEmail);
            // update
            updateUserOnlineInDb(userEmail, true);
            // broadcast
            broadcastUserOnline(userEmail);
        }
    }

    @Override
    @Transactional
    public void removeOnlineUser(String userEmail, String sessionId) {
        String redisKey = RedisPrefix.ONLINE_USERS.getValue() + userEmail;

        if (sessionId != null) {
            stringValueRedisTemplate.opsForSet().remove(redisKey, sessionId);
        }

        Long sessionCount = stringValueRedisTemplate.opsForSet().size(redisKey);

        if (sessionCount != null && sessionCount == 0) {
            log.info("{} went OFFLINE", userEmail);
            stringValueRedisTemplate.delete(redisKey);
            // update
            updateUserOnlineInDb(userEmail, false);
            // broadcast
            broadcastUserOnline(userEmail);
        }
    }

    @Override
    public void extendExpire(String userEmail) {
        String redisKey = RedisPrefix.ONLINE_USERS.getValue() + userEmail;
        Boolean hasKey = stringValueRedisTemplate.hasKey(redisKey);
        if (hasKey) {
            stringValueRedisTemplate.expire(redisKey, Duration.ofMinutes(TTL_MINUTES));
        }
    }

    private void updateUserOnlineInDb(String email, boolean isOnline) {
        appUserRepository.findByEmail(email).ifPresent(
                appUser -> {
                    appUser.setIsOnline(isOnline);
                    appUser.setLastSeen(isOnline ? null : LocalDateTime.now());
                    appUserRepository.save(appUser);
                }
        );
    }

    private void broadcastUserOnline(String email) {
        appUserRepository.findByEmail(email).ifPresent(
                appUser -> {
                    simpMessageSendingOperations.convertAndSend("/topic/public/user-status",
                            userBasicMapper.toUserBasicResponse(appUser)
                    );
                }
        );
    }
}
