package com.tobeict.intergamma.cucumber.containers;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@Testcontainers
public class PostgresDockerContainer {
    int POSTGRES_SERVICE_PORT = 5432;
    PostgreSQLContainer<?> dockerContainer;

    boolean started;

    private String user="treinorientatie_buffer_admin";
    private String password="treinorientatie_buffer_admin";

    public PostgresDockerContainer() throws InterruptedException {
        this.start();
    }

    public void start() throws InterruptedException {
        if (started) {
//            log.info("POSTGRES ALREADY RUNNING AT: {}:{}", dockerContainer.getHost(),
//                    dockerContainer.getExposedPorts());
        } else {
            dockerContainer = createDockerContainerForPostgres();
            dockerContainer.start();
//            log.info("POSTGRES STARTED AT: {}:{}", dockerContainer.getHost(),
//                    dockerContainer.getExposedPorts());
            try {
//                log.info("RUNNING LIQUIBASE SCRIPTS");
                executeLiquidBaseScripts();
            } catch (SQLException | LiquibaseException e) {
                throw new IllegalStateException(e);
            }
            started = true;
        }
    }

    public void stop() {
        dockerContainer.stop();
        dockerContainer = null;
        started = false;
//        log.info("POSTGRES STOPPED");
    }

    private PostgreSQLContainer<?> createDockerContainerForPostgres() {
        return new PostgreSQLContainer<>("postgres:latest")
                .withUsername(this.user)
                .withPassword(this.password)
                .withExposedPorts(POSTGRES_SERVICE_PORT)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                                new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(POSTGRES_SERVICE_PORT), new ExposedPort(POSTGRES_SERVICE_PORT)))
                        )
                );
    }

    public String getUrl() {return dockerContainer.getJdbcUrl();}

    public String getUsername() {return dockerContainer.getUsername();}

    public String getPassword() {return dockerContainer.getPassword();}

    private void executeLiquidBaseScripts() throws SQLException, LiquibaseException {
        java.sql.Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());

        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
        } finally {
            if (connection != null) {
                connection.rollback();
                connection.close();
            }
        }
    }
}
