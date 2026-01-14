package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.OrganizerCreateRequest;
import com.auhpp.event_management.dto.request.OrganizerUpdateRequest;
import com.auhpp.event_management.dto.request.RejectionRequest;
import com.auhpp.event_management.dto.response.OrganizerRegistrationResponse;
import com.auhpp.event_management.dto.response.PageResponse;

public interface OrganizerRegistrationService {
    OrganizerRegistrationResponse createOrganizerRegistration(OrganizerCreateRequest organizerCreateRequest);

    OrganizerRegistrationResponse updateOrganizerRegistration(long id,
                                                              OrganizerUpdateRequest organizerUpdateRequest);

    void deleteOrganizerRegistration(long id);

    void cancelOrganizerRegistration(long id);

    void rejectOrganizerRegistration(long id, RejectionRequest rejectionRequest);

    void approveOrganizerRegistration(long id);

    PageResponse<OrganizerRegistrationResponse> getOrganizerRegistrations(int page, int size);

    PageResponse<OrganizerRegistrationResponse> getOrganizerRegistrationsByUser(int page, int size);
    OrganizerRegistrationResponse getOrganizerRegistrationById(Long id);
}
