package com.webflux.pruebatecnica.infraestructure.rest.controller;

import com.webflux.pruebatecnica.application.usecases.ServiceRequestHandler;
import com.webflux.pruebatecnica.domain.model.Employee;
import com.webflux.pruebatecnica.domain.model.ServiceRequest;
import com.webflux.pruebatecnica.domain.port.PersistencePort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final PersistencePort<Employee> adapter;

    public EmployeeController(ServiceRequestHandler requestHandler, @Qualifier("employeeMongoReactiveAdapter") PersistencePort<Employee> employeePersistencePort) {
        ServiceRequest<Employee> request = new ServiceRequest<>("/employee", employeePersistencePort);
        this.adapter = (PersistencePort<Employee>) requestHandler.handleRequest(request);
    }

    @GetMapping("/list")
    public Flux<Employee> getAll() {
        return adapter.getAll();
    }

    @PostMapping
    @CircuitBreaker(name = "orchestrator", fallbackMethod = "fallback")
    public Mono<Employee> save(@RequestBody Employee employee) {
        return adapter.create(employee);
    }

    @GetMapping("/{id}")
    public Mono<Employee> getById(@PathVariable String id) {
        return adapter.getById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return adapter.deleteById(id);
    }

    public Mono<Employee> fallback(Employee employee, Exception e) {
        return Mono.error(new RuntimeException("Error occurred while trying to communicate with the service." + e.getMessage()));
    }
}
