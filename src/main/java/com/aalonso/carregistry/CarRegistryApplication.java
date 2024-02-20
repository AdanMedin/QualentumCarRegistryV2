package com.aalonso.carregistry;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * El el directorio raiz del proyecto, hay un docker-compose.yml.
 * Ejecutandolo se levanta el contenedor de la base de datos y se ejecuta el script (init_vehicles_db.sql) que la crea.
 * La ruta para acceder a swagger es: <a href="http://localhost:8080/v1/car-registry-API/swagger-ui/index.html">...</a>
 * Documentación con open API en el archivo /src/main/resources/static/openapi.yaml
 * */
@Slf4j
@SpringBootApplication
public class CarRegistryApplication {

    @PostConstruct
    public void init() {
        log.info("**************************************");
        log.info("CAR REGISTRY APPLICATION RUNNING...");
        log.info("**************************************");
        log.info("CarRegistryApplication is operational...");
    }

    public static void main(String[] args) {
        SpringApplication.run(CarRegistryApplication.class, args);
    }

}
