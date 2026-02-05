package com.auhpp.event_management.constant;

import lombok.Getter;

@Getter
public enum RoleName {
    ADMIN("Quản trị viên"), USER("Người dùng"), ORGANIZER("Nhà tổ chức"),
    EVENT_OWNER("Chủ sự kiện"), EVENT_ADMIN("Quản trị viên"), EVENT_MANAGER("Quản lý"),
    EVENT_STAFF("Nhân viên - checkin");

    private final String value;

    RoleName(String value) {
        this.value = value;
    }
}
