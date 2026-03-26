package com.springpract.product_service.service.Impl;

import com.springpract.product_service.dto.ProductRequest;
import com.springpract.product_service.dto.ProductResponse;
import com.springpract.product_service.entity.Product;
import com.springpract.product_service.repository.ProductRepository;
import com.springpract.product_service.service.ProductService;

import java.util.List;
import java.util.UUID;

public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    @Override
    public Product getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        return product;


    }

    @Override
    public List<Product> getProductByCategory(String productCategory) {
         List<Product> product = productRepository.findByProductCategory(productCategory);
         return product;

    }


}
