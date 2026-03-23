package com.springpract.product_service.mapper;

import com.springpract.product_service.dto.ProductRequest;
import com.springpract.product_service.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product ToEntity(ProductRequest productRequest) {
        if(productRequest == null) {
            return null;
        }
        Product product = new Product();

        product.setProductName(productRequest.ProductName());
        product.setProductPrice(productRequest.ProductPrice());
        product.setProductDescription(productRequest.ProductDescription());
        product.setProductCategory(productRequest.ProductCategory());
        return product;





    }
   public ProductRequest ToProductResponse(Product product) {
        if(product == null) {return null;}

        return new ProductRequest(
                product.getProductName(),
                product.getProductDescription(),
                product.getProductPrice(),
                product.getProductCategory()
        );



   }
}
