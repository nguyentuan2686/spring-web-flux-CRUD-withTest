package com.example.springreactive;

import com.example.springreactive.controller.ProductController;
import com.example.springreactive.dto.ProductDto;
import com.example.springreactive.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class SpringReactiveMongoDbCrudApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ProductService service;

	@Test
	public void addProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "mobile",1, 1000));
		when(service.saveProduct(productDtoMono)).thenReturn(productDtoMono);

		webTestClient.post().uri("/products")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus()
				.isOk();

	}

	@Test
	public void getProductTest(){
		Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("102", "mobile",1, 1000),
				new ProductDto("103", "laptop",1, 10000));
		when(service.getProducts()).thenReturn(productDtoFlux);
		Flux<ProductDto> responseBody = webTestClient.get().uri("/products")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();
		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto("102", "mobile",1, 1000))
				.expectNext(new ProductDto("103", "laptop",1, 10000))
				.verifyComplete();
	}

	@Test
	public void getProduct(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "mobile",1, 1000));
		when(service.getProduct(any())).thenReturn(productDtoMono);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/102")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();
		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p -> p.getId().equals("102") && p.getName().equals("mobile"))
				.verifyComplete();
	}

	@Test
	public void updateProduct(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "mobile",1, 1000));
		when(service.updateProduct(productDtoMono, productDtoMono.blockOptional().orElseThrow(()-> new IllegalAccessError("d")).getId()))
				.thenReturn(productDtoMono);
		webTestClient.put().uri("/products/102")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void deleteProduct(){
		given(service.deleteProduct(any())).willReturn(Mono.empty());
		webTestClient.delete().uri("/products/10")
				.exchange()
				.expectStatus().isOk();
	}
}
