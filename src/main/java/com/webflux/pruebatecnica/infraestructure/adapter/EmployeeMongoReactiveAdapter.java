package com.webflux.pruebatecnica.infraestructure.adapter;

import com.webflux.pruebatecnica.domain.model.Employee;
import com.webflux.pruebatecnica.domain.port.PersistencePort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Primary
public class EmployeeMongoReactiveAdapter implements PersistencePort<Employee> {
    public static final String BASE_URL_EMPLOYEE = "http://localhost:8081/employee";
    private final WebClient webClientEmployee;

    public EmployeeMongoReactiveAdapter(WebClient.Builder webClientBuilder) {
        this.webClientEmployee = webClientBuilder.baseUrl(BASE_URL_EMPLOYEE).build();
    }

    @Override
    public Mono<Employee> create(Employee employee) {
        return webClientEmployee.post()
                .bodyValue(employee)
                .retrieve()
                .bodyToMono(Employee.class);
    }

    @Override
    public Mono<Employee> getById(String id) {
        return webClientEmployee.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Employee.class);
    }

    @Override
    public Flux<Employee> getAll() {
        return webClientEmployee.get()
                .uri("/list")
                .retrieve()
                .bodyToFlux(Employee.class);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return webClientEmployee.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
