package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.RegisterRequest;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Role;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.UserMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.RoleRepository;
import com.auhpp.event_management.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    AppUserRepository appUserRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Override
    @Transactional
    public void createUser(RegisterRequest registerRequest) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(registerRequest.getEmail());
        if (appUserOptional.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        Role role = roleRepository.findByName(RoleName.USER.name());
        AppUser appUser = userMapper.toAppUser(registerRequest);
        String passwordEncoded = passwordEncoder.encode(registerRequest.getPassword());

        appUser.setPassword(passwordEncoded);
        appUser.setStatus(true);
        appUser.setRole(role);

        appUserRepository.save(appUser);
    }


}
