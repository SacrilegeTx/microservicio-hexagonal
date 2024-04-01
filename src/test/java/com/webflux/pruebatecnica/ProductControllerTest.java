package com.webflux.pruebatecnica;

import com.webflux.pruebatecnica.domain.model.Employee;
import com.webflux.pruebatecnica.domain.model.Product;
import com.webflux.pruebatecnica.domain.port.PersistencePort;
import com.webflux.pruebatecnica.infraestructure.adapter.ProductCassandraReactiveAdapter;
import com.webflux.pruebatecnica.infraestructure.rest.controller.ProductController;
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
public class ProductControllerTest {
    private static final String BASE_URL_PRODUCT = "http://localhost:8080/product";

    @Autowired
    private WebTestClient webClient;

    @LocalServerPort
    private int port;

    @Autowired
    private ProductController productController;

    @MockBean
    private PersistencePort<Product> productPersistencePort;

    @MockBean
    private ProductCassandraReactiveAdapter productCassandraReactiveAdapter;

    @Test
    void getAll() {
        webClient.get().uri(BASE_URL_PRODUCT + "/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class).hasSize(2);

    }

    @ParameterizedTest
    @ValueSource(strings = {"e64fae0c-c1bb-4953-a9e1-9c19e1a9b920"})
    void getById(String id) {
        webClient.get().uri(BASE_URL_PRODUCT + "/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"e64fae0c-c1bb-4953-a9e1-9c19e1a9b921"})
    void getByIdNotFound(String id) {
        webClient.get().uri(BASE_URL_PRODUCT + "/{id}", id)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void createProduct() {
        Product newProduct = new Product();
        newProduct.setName("Product Test");
        newProduct.setPrice(7500);

        webClient.post().uri(BASE_URL_PRODUCT)
                .bodyValue(newProduct)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"e64fae0c-c1bb-4953-a9e1-9c19e1a9b920"})
    void deleteProduct(String id) {
        webClient.delete().uri(BASE_URL_PRODUCT + "/{id}", id)
                .exchange()
                .expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(strings = {"e64fae0c-c1bb-4953-a9e1-9c19e1a9b921"})
    void deleteProductIdNotFound(String id) {
        webClient.delete().uri(BASE_URL_PRODUCT + "/{id}", id)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void saveProductCircuitBreakerTest() {
        Product newProduct = new Product();
        newProduct.setName("Product Test");
        newProduct.setPrice(7500);

        // Simula una falla en la operación de guardar un empleado
        Mockito.when(productPersistencePort.create(newProduct))
                .thenReturn(Mono.error(new RuntimeException("Error de prueba")));

        // Verifica si el método de fallback se invoca como se esperaba
        StepVerifier.create(productController.save(newProduct))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().contains("Error de prueba"))
                .verify();
    }
}
