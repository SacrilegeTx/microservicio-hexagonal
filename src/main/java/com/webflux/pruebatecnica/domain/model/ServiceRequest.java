package com.webflux.pruebatecnica.domain.model;

import com.webflux.pruebatecnica.domain.port.PersistencePort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest<T> {
    private String url;
    private PersistencePort<T> service;
}
