package com.auhpp.event_management.constant;

import lombok.Getter;

@Getter
public enum FolderName {
    ORGANIZER_REGISTRATION("eventhub/organizer_registration/");

    private final String value;

    FolderName(String value) {
        this.value = value;
    }
}
