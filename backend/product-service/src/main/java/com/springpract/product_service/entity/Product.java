package com.springpract.product_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor

@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private UUID productId;

    private String productName;
    private String productDescription;
    private String productPrice;
    private String productCategory;

    private Integer productQuantity;
    private String productImage;
    private String productStatus;






}
