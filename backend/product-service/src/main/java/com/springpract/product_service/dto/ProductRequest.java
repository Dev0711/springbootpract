package com.springpract.product_service.dto;

import lombok.Data;

public record ProductRequest(
        String ProductName,
        String ProductDescription,
        String ProductPrice,
        String ProductCategory



) {
}
