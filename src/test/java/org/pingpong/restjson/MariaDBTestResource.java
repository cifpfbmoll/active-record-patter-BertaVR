package org.pingpong.restjson;
import org.testcontainers.containers.MariaDBContainer;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.pingpong.restjson.MariaDBTestResource.Initializer;


import java.util.HashMap;
import java.util.Map;

@QuarkusTestResource(Initializer.class)
public class MariaDBTestResource {

    public static class Initializer implements QuarkusTestResourceLifecycleManager { //interfaz
        private MariaDBContainer mariaDBContainer; //ccontenedor mariadb

        @Override
        public Map<String, String> start() {
            // Instanciar contenedor usando la imagen de mariadb, hay que definir application properties
            this.mariaDBContainer = new MariaDBContainer<>("mariadb:latest");
            // Tirar el contenedor
            this.mariaDBContainer.start();
            return getConfigurationParameters();
        }

        private Map<String, String> getConfigurationParameters() {
/*           Esto pilla las properties que he definido en application properties y las aplica en el contenedor:

            quarkus.datasource.username=developer
            quarkus.datasource.password=developer
            quarkus.datasource.jdbc.url=jdbc:mariadb://localhost:3000/developer*/

            final Map<String, String> conf = new HashMap<>();
            conf.put("quarkus.datasource.jdbc.url", this.mariaDBContainer.getJdbcUrl());
            conf.put("quarkus.datasource.username", this.mariaDBContainer.getUsername());
            conf.put("quarkus.datasource.password", this.mariaDBContainer.getPassword());

            return conf;
        }

        @Override
        public void stop() {
            // Parar el contenedor
            if (this.mariaDBContainer != null) {
                this.mariaDBContainer.close();
            }
        }

    }

    }
