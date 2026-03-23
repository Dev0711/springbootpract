package com.springpract.product_service.dto;

import java.util.UUID;

public record ProductResponse(
        UUID productId,
        String productName,
        String productDescription,
        Integer productPrize,
        String productCategory
) {
}
