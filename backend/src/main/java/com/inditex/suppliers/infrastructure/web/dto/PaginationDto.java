package com.inditex.suppliers.infrastructure.web.dto;

public record PaginationDto(Integer limit, Integer offset, Integer total) {
}
