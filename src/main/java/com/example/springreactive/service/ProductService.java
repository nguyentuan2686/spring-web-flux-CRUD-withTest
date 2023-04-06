package com.example.springreactive.service;

import com.example.springreactive.dto.ProductDto;
import com.example.springreactive.entity.Product;
import com.example.springreactive.repository.ProductRepository;
import com.example.springreactive.utils.Apputils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Flux<ProductDto> getProducts(){
        return repository.findAll().map(Apputils::entityToDto);
    }

    public Mono<ProductDto> getProduct(String id){
        return repository.findById(id).map(Apputils::entityToDto);
    }

    public Flux<ProductDto> getProductInRange(double min, double max){
        return repository.findByPriceBetween(Range.closed(min, max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
        return productDtoMono
                .map(Apputils::dtoToEntity)
                .flatMap(product -> repository.insert(product))
                .map(Apputils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id){
        return repository.findById(id)
                .flatMap(product -> productDtoMono.map(Apputils::dtoToEntity))
                .doOnNext(product -> product.setId(id))
                .flatMap(repository::save)
                .map(Apputils::entityToDto);
    }

    public Mono<Void> deleteProduct(String id){
        return repository.deleteById(id);
    }
}
