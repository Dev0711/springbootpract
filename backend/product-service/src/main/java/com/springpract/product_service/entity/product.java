package com.springpract.product_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class product {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private UUID id;



}
