package com.project.springpract.mapper;

import com.project.springpract.dto.AddressDTO;
import com.project.springpract.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public Address toEntity(AddressDTO addressDTO){
        if (addressDTO==null){
            return null;
        }
        Address address = new Address();
        address.setId(addressDTO.id());
        address.setAddressLine2(addressDTO.addressLine2());
        address.setAddressLine1(addressDTO.addressLine1());
        address.setCity(addressDTO.city());
        address.setState(addressDTO.state());
        address.setZipCode(addressDTO.zipCode());
        address.setCountry(addressDTO.country());
        return address;
    }
    public AddressDTO toDTO(Address address){
        if(address==null){
            return null;
        }
        return new AddressDTO(
                address.getId(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity() ,
                address.getState(),
                address.getCountry(),
                address.getZipCode(),
                address.getType().toString(),
                address.getIsDefault()
        );
    }


}
