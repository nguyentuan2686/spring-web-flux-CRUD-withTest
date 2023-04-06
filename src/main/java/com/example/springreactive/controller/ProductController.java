package com.example.springreactive.controller;


import com.example.springreactive.dto.ProductDto;
import com.example.springreactive.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")

public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public Flux<ProductDto> products(){
        return service.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> product(@PathVariable String id){
        return service.getProduct(id);
    }

    @GetMapping("/product-range")
    public Flux<ProductDto> productbetween(@RequestParam(value = "min") double min, @RequestParam(value = "max") double max){
        return service.getProductInRange(min,max);
    }

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return service.saveProduct(productDtoMono);
    }

    @PutMapping("/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDtoMono, @PathVariable String id){
        return service.updateProduct(productDtoMono,id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id){
        return service.deleteProduct(id);
    }

}
