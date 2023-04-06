package com.example.springreactive.utils;

import com.example.springreactive.dto.ProductDto;
import com.example.springreactive.entity.Product;
import org.springframework.beans.BeanUtils;

public class Apputils {
    public static ProductDto entityToDto(Product product){
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    public static Product dtoToEntity(ProductDto productDto){
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }
}
