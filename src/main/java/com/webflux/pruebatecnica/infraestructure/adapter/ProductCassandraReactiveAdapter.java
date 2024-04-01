package com.webflux.pruebatecnica.infraestructure.adapter;

import com.webflux.pruebatecnica.domain.model.Product;
import com.webflux.pruebatecnica.domain.port.PersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductCassandraReactiveAdapter implements PersistencePort<Product> {
    public static final String BASE_URL_PRODUCT = "http://localhost:8082/product";
    private final WebClient webClientProduct;

    public ProductCassandraReactiveAdapter(WebClient.Builder webClientBuilder) {
        this.webClientProduct = webClientBuilder.baseUrl(BASE_URL_PRODUCT).build();
    }

    @Override
    public Mono<Product> create(Product product) {
        return webClientProduct.post()
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class);
    }

    @Override
    public Mono<Product> getById(String id) {
        return webClientProduct.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }

    @Override
    public Flux<Product> getAll() {
        return webClientProduct.get()
                .uri("/list")
                .retrieve()
                .bodyToFlux(Product.class);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return webClientProduct.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
