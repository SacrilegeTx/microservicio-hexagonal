package com.webflux.pruebatecnica.domain.model;

import com.webflux.pruebatecnica.domain.port.PersistencePort;

public class EmployeeServiceRequest extends ServiceRequest<Employee> {
    public EmployeeServiceRequest(String url, PersistencePort<Employee> service) {
        super(url, service);
    }
}