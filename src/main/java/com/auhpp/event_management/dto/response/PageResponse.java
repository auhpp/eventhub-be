package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse <T>{
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private long totalElements;
    private List<T> data;
}
