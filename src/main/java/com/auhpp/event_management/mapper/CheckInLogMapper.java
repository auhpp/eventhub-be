package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.CheckInLogResponse;
import com.auhpp.event_management.entity.CheckInLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CheckInLogMapper {
    @Mapping(target = "attendeeId", source = "attendee.id")
    @Mapping(target = "eventStaffId", source = "eventStaff.id")
    @Mapping(target = "eventStaffEmail", source = "eventStaff.appUser.email")
    @Mapping(target = "eventStaffFullName", source = "eventStaff.appUser.fullName")
    CheckInLogResponse toCheckInLogResponse(CheckInLog checkInLog);
}
