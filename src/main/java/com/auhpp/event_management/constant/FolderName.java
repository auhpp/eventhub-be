package com.auhpp.event_management.constant;

import lombok.Getter;

@Getter
public enum FolderName {
    ORGANIZER_REGISTRATION("eventhub/organizer_registration/"), CATEGORY("eventhub/category/"),
    EVENT("eventhub/event/"), USER_AVATAR("eventhub/avatar/");

    private final String value;

    FolderName(String value) {
        this.value = value;
    }
}
