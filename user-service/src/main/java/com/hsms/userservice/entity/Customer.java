package com.hsms.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "customer_seq"
//    )
//    @SequenceGenerator(
//            name = "customer_seq",
//            sequenceName = "customer_seq",
//            allocationSize = 1
//    )
//    private Long customerId;

    private Long userId;

    private String address;

    private String city;

    private String pincode;
}