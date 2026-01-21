package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.EventStaffStatus;
import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.EventStaffCreateRequest;
import com.auhpp.event_management.dto.response.EventStaffResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.EventStaff;
import com.auhpp.event_management.entity.Role;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventStaffMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.EventRepository;
import com.auhpp.event_management.repository.EventStaffRepository;
import com.auhpp.event_management.repository.RoleRepository;
import com.auhpp.event_management.service.EventStaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventStaffServiceImpl implements EventStaffService {
    EventStaffRepository eventStaffRepository;
    RoleRepository roleRepository;
    AppUserRepository appUserRepository;
    EventRepository eventRepository;
    EventStaffMapper eventStaffMapper;

    @Override
    @Transactional
    public EventStaffResponse createEventStaff(EventStaffCreateRequest eventStaffCreateRequest) {
        AppUser appUser = appUserRepository.findById(eventStaffCreateRequest.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Event event = eventRepository.findById(eventStaffCreateRequest.getEventId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Role role = roleRepository.findByName(eventStaffCreateRequest.getRoleName());
        EventStaff eventStaff = new EventStaff();

        eventStaff.setAppUser(appUser);
        eventStaff.setEvent(event);
        eventStaff.setRole(role);

        if (role.getName() != RoleName.EVENT_OWNER) {
            eventStaff.setStatus(EventStaffStatus.PENDING);
        } else {
            eventStaff.setStatus(EventStaffStatus.ACTIVE);
        }
        eventStaffRepository.save(eventStaff);

        return eventStaffMapper.toEventStaffResponse(eventStaff);
    }
}
