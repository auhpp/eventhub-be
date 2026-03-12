package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.EventSeriesStatus;
import com.auhpp.event_management.dto.request.EventSeriesFollowerCreateRequest;
import com.auhpp.event_management.dto.request.EventSeriesFollowerSearchRequest;
import com.auhpp.event_management.dto.response.EventSeriesFollowerResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.EventSeries;
import com.auhpp.event_management.entity.EventSeriesFollower;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventSeriesFollowerMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.EventSeriesFollowerRepository;
import com.auhpp.event_management.repository.EventSeriesRepository;
import com.auhpp.event_management.service.EventSeriesFollowerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventSeriesFollowerServiceImpl implements EventSeriesFollowerService {

    EventSeriesFollowerRepository esFollowerRepository;
    EventSeriesFollowerMapper esFollowerMapper;
    AppUserRepository appUserRepository;
    EventSeriesRepository eventSeriesRepository;

    @Override
    @Transactional
    public EventSeriesFollowerResponse createEventSeriesFollower(EventSeriesFollowerCreateRequest request) {
        if (esFollowerRepository.findByAppUserIdAndEventSeriesId(request.getUserId(),
                request.getEventSeriesId()).isPresent()) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        AppUser appUser = appUserRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        EventSeries eventSeries = eventSeriesRepository.findById(request.getEventSeriesId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (eventSeries.getStatus() != EventSeriesStatus.ACTIVE) {
            throw new AppException(ErrorCode.EVENT_SERIES_NOT_ACTIVE);
        }
        EventSeriesFollower eventSeriesFollower = EventSeriesFollower.builder()
                .appUser(appUser)
                .eventSeries(eventSeries)
                .build();
        esFollowerRepository.save(eventSeriesFollower);
        return esFollowerMapper.toEventSeriesFollowerResponse(eventSeriesFollower);
    }

    @Override
    @Transactional
    public void deleteEventSeriesFollower(Long id) {
        esFollowerRepository.deleteById(id);
    }

    @Override
    public PageResponse<EventSeriesFollowerResponse> getEventSeriesFollowers(
            EventSeriesFollowerSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<EventSeriesFollower> pageData = esFollowerRepository.filterEventSeriesFollower(
                request.getUserId(), request.getEventSeriesId(), pageable
        );
        List<EventSeriesFollowerResponse> responses = pageData.getContent().stream().map(
                esFollowerMapper::toEventSeriesFollowerResponse
        ).toList();
        return PageResponse.<EventSeriesFollowerResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public Integer countEventSeriesFollowers(EventSeriesFollowerSearchRequest request) {
        return esFollowerRepository.countEventSeriesFollower(request.getUserId(), request.getEventSeriesId());
    }
}
