# Prueba Tecnica: Microservicio Orquestador de Servicios

Para la pruebca se crearon 2 repositorios:

1. Repositorio Empleado (Gradle + WebFlux + MongoDB - 8081)
2. Repositorio Producto (Maven + WebFlux + Appache Cassandra - 8082)

### Requisitos

1. Java - 17
2. Maven - 3.x.x

### Pasos para instalar

**1. Clonar la aplicación**

```bash
git clone url_repo
```

**2. Compilar y empaquetar la aplicación**

```bash
mvn clean install
```

**3. Ejecutar la aplicación**

```bash
mvn spring-boot:run
```

La aplicación se ejecutará en <http://localhost:8080>.

### Explore Rest APIs

La aplicación define los siguientes endpoints.

Empleado
```
GET /employee/list
GET /employee/{id}
POST /employee/
DELETE /employee/{id}
```

Producto
```
GET /product/list
GET /product/{id}
POST /product/
DELETE /product/{id}
```

Se ha creado una regla de negocio con la librería Drools, la cual se encarga de acceder a los servicios de empleado y producto para realizar la operación correspondiente.
En el archivo `src/main/resources/rules/ConnectEmployeeOrProductRule.drl` se encuentra la regla de negocio.

También se ha implementado Circuit Breaker con Resilience4j para manejar las fallas en los servicios de empleado y producto.
La configuración se encuentra en el archivo `src/main/resources/application.properties`.

**registerHealthIndicator**: Si es verdadero, se registrará un indicador de salud para este Circuit Breaker.
**slidingWindowSize**: El tamaño de la ventana deslizante que se utiliza para registrar las llamadas.
**minimumNumberOfCalls**: El número mínimo de llamadas que se deben registrar antes de que el Circuit Breaker pueda calcular la tasa de fallos.
**permittedNumberOfCallsInHalfOpenState**: El número de llamadas permitidas cuando el Circuit Breaker está en estado semiabierto.
**waitDurationInOpenState**: El tiempo en milisegundos que el Circuit Breaker debe esperar en estado abierto antes de cambiar a estado semiabierto.
**failureRateThreshold**: El umbral de tasa de fallos. Si la tasa de fallos es igual o superior a este umbral, el Circuit Breaker se abrirá.
**eventConsumerBufferSize**: El tamaño del buffer para los eventos del Circuit Breaker.
**automaticTransitionFromOpenToHalfOpenEnabled**: Si es verdadero, el Circuit Breaker cambiará automáticamente de estado abierto a estado semiabierto cuando se cumpla el waitDurationInOpenState.
