package com.project.springpract.dto;

public record AddressDTO(
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String zipCode,
        String country,
        String addressType
) {
}
