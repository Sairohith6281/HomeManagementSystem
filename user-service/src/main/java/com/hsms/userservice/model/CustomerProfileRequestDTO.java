package com.hsms.userservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerProfileRequestDTO {

    private Long userId;
    private String name;
    private String address;
    private String city;
    private String pincode;
}