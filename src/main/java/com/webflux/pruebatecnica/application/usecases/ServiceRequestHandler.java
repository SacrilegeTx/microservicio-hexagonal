package com.webflux.pruebatecnica.application.usecases;

import com.webflux.pruebatecnica.domain.model.ServiceRequest;
import com.webflux.pruebatecnica.domain.port.PersistencePort;
import com.webflux.pruebatecnica.infraestructure.adapter.EmployeeMongoReactiveAdapter;
import com.webflux.pruebatecnica.infraestructure.adapter.ProductCassandraReactiveAdapter;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServiceRequestHandler {
    private final KieContainer kieContainer;
    private final EmployeeMongoReactiveAdapter employeeAdapter;
    private final ProductCassandraReactiveAdapter productAdapter;

    public ServiceRequestHandler(EmployeeMongoReactiveAdapter employeeAdapter, ProductCassandraReactiveAdapter productAdapter) {
        this.employeeAdapter = employeeAdapter;
        this.productAdapter = productAdapter;
        this.kieContainer = KieServices.Factory.get().getKieClasspathContainer();
    }

    public PersistencePort<?> handleRequest(ServiceRequest<?> request) {
        KieSession kieSession = kieContainer.newKieSession("ksession-rules");
        try {
            kieSession.setGlobal("employeeAdapter", employeeAdapter);
            kieSession.setGlobal("productAdapter", productAdapter);
            kieSession.insert(request);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        log.info("Service request handled successfully: " + request.getService().toString());

        return request.getService();
    }
}