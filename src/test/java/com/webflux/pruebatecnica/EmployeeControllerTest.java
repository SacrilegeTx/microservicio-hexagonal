package com.webflux.pruebatecnica;

import com.webflux.pruebatecnica.domain.model.Employee;
import com.webflux.pruebatecnica.domain.port.PersistencePort;
import com.webflux.pruebatecnica.infraestructure.adapter.EmployeeMongoReactiveAdapter;
import com.webflux.pruebatecnica.infraestructure.rest.controller.EmployeeController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {
    private static final String BASE_URL_EMPLOYEE = "http://localhost:8080/employee";

    @Autowired
    private WebTestClient webClient;

    @LocalServerPort
    private int port;

    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private PersistencePort<Employee> employeePersistencePort;

    @MockBean
    private EmployeeMongoReactiveAdapter employeeMongoReactiveAdapter;

    @Test
    void getAll() {
        webClient.get().uri(BASE_URL_EMPLOYEE + "/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Employee.class).hasSize(3);

    }

    @ParameterizedTest
    @ValueSource(strings = {"660502e0964aff51352708c8"})
    void getById(String id) {
        webClient.get().uri(BASE_URL_EMPLOYEE + "/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"660502e0964aff51352708c9"})
    void getByIdNotFound(String id) {
        webClient.get().uri(BASE_URL_EMPLOYEE + "/{id}", id)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void createEmployee() {
        Employee newEmployee = new Employee();
        newEmployee.setName("John Doe");
        newEmployee.setSalary(7500);

        webClient.post().uri(BASE_URL_EMPLOYEE)
                .bodyValue(newEmployee)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"660502e0964aff51352708c8"})
    void deleteEmployee(String id) {
        webClient.delete().uri(BASE_URL_EMPLOYEE + "/{id}", id)
                .exchange()
                .expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(strings = {"660502e0964aff51352708c9"})
    void deleteEmployeeIdNotFound(String id) {
        webClient.delete().uri(BASE_URL_EMPLOYEE + "/{id}", id)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void saveEmployeeCircuitBreakerTest() {
        Employee newEmployee = new Employee();
        newEmployee.setName("John Doe");
        newEmployee.setSalary(7500);

        // Simula una falla en la operación de guardar un empleado
        Mockito.when(employeePersistencePort.create(newEmployee))
                .thenReturn(Mono.error(new RuntimeException("Error de prueba")));

        // Verifica si el método de fallback se invoca como se esperaba
        StepVerifier.create(employeeController.save(newEmployee))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().contains("Error de prueba"))
                .verify();
    }
}
