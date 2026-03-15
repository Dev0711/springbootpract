package com.project.springpract.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "uuid")
    @JsonIgnore
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false, columnDefinition = "uuid")
    private UUID userId;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    @Enumerated(EnumType.STRING)
    private AddressType type; // HOME, WORK, OTHER

    private Boolean isDefault = false;
}