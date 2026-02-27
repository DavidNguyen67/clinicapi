package com.clinicsystem.clinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> records;
    private String nextCursor;
    private boolean hasMore;
    private int pageSize;
    private long totalElements;

    public static <T> PageResponse<T> of(List<T> records, String nextCursor, boolean hasMore, int pageSize,
            long totalElements) {
        return PageResponse.<T>builder()
                .records(records)
                .nextCursor(nextCursor)
                .hasMore(hasMore)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .build();
    }
}
