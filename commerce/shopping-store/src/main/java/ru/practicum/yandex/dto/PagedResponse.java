package ru.practicum.yandex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;

    public static <T> PagedResponse<T> of(List<T> content) {
        return new PagedResponse<>(content, 1, content.size(), content.size(), 0);
    }
}