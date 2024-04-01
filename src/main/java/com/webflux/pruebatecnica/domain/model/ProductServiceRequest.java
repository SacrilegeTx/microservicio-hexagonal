package com.webflux.pruebatecnica.domain.model;

import com.webflux.pruebatecnica.domain.port.PersistencePort;

public class ProductServiceRequest extends ServiceRequest<Product> {
    public ProductServiceRequest(String url, PersistencePort<Product> service) {
        super(url, service);
    }
}