package com.project.springpract.dto;

import java.util.UUID;

public record AddressDTO(
        UUID id,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String zipCode,
        String country,
        String addressType,
        Boolean isDefault
) {
}
