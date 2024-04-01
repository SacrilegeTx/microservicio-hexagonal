package com.webflux.pruebatecnica;

import com.webflux.pruebatecnica.infraestructure.adapter.EmployeeMongoReactiveAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan(basePackages = {"com.webflux.pruebatecnica.infraestructure.adapter"})
public class AppConfig {
    @Bean
    public EmployeeMongoReactiveAdapter employeeMongoReactiveAdapter(WebClient.Builder webClientBuilder) {
        // Aquí debes retornar una instancia de EmployeeMongoReactiveAdapter.
        // Esto puede implicar la inyección de otras dependencias o la configuración de propiedades.
        return new EmployeeMongoReactiveAdapter(webClientBuilder);
    }
}
