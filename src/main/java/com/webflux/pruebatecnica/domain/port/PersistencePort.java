package com.webflux.pruebatecnica.domain.port;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersistencePort<T> {
    Mono<T> create(T entity);
    Mono<T> getById(String id);
    Flux<T> getAll();
    Mono<Void> deleteById(String id);
}
