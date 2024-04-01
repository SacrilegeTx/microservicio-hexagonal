package com.webflux.pruebatecnica;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@WebFluxTest
public class GlobalExceptionHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Ignore
    public void handleWebClientResponseException() {
        webTestClient.get().uri("/testWebClientResponseException")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class).isEqualTo("Error de conexión a la base de datos: Error de prueba");
    }

    @Ignore
    public void handleException() {
        webTestClient.get().uri("/testException")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class).isEqualTo("Error interno del servidor: Error de prueba");
    }

    @RestController
    public class TestController {

        @GetMapping("/testWebClientResponseException")
        public void testWebClientResponseException() {
            throw new WebClientResponseException("Error de prueba", HttpStatus.BAD_REQUEST.value(), "", null, null, null);
        }

        @GetMapping("/testException")
        public void testException() {
            throw new RuntimeException("Error de prueba");
        }
    }
}