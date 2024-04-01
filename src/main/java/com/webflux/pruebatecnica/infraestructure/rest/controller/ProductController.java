package com.webflux.pruebatecnica.infraestructure.rest.controller;

import com.webflux.pruebatecnica.application.usecases.ServiceRequestHandler;
import com.webflux.pruebatecnica.domain.model.Product;
import com.webflux.pruebatecnica.domain.model.ServiceRequest;
import com.webflux.pruebatecnica.domain.port.PersistencePort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final PersistencePort<Product> adapter;

    public ProductController(ServiceRequestHandler requestHandler, PersistencePort<Product> productPersistencePort) {
        ServiceRequest<Product> request = new ServiceRequest<>("/product", productPersistencePort);
        this.adapter = (PersistencePort<Product>) requestHandler.handleRequest(request);
    }

    @GetMapping("/list")
    public Flux<Product> getAll() {
        return adapter.getAll();
    }

    @PostMapping
    public Mono<Product> save(@RequestBody Product product) {
        return adapter.create(product);
    }

    @GetMapping("/{id}")
    public Mono<Product> getById(@PathVariable String id) {
        return adapter.getById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return adapter.deleteById(id);
    }
}
