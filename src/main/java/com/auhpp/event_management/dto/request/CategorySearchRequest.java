package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.SortBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearchRequest {
    private String name;

    private SortOrder sortOrder;

    private SortBy sortBy;
}
