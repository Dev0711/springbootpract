package com.springpract.product_service.service;

import com.springpract.product_service.dto.ProductRequest;
import com.springpract.product_service.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {

    Product getProductById(UUID id);

    List<Product> getProductByCategory(String productCategory);



}
