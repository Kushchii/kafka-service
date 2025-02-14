package com.pdp.transaction;

import com.pdp.transaction.persistent.repository.MailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {MailApplication.class},
        properties = "spring.main.allow-bean-definition-overriding=true")
@DirtiesContext
@AutoConfigureWebTestClient(timeout = "PT10M")
@Tag("FunctionalTest")
public abstract class BaseFunctionalTest {

    private static final String JDBC_PREFIX = "jdbc";
    private static final String R2DBC_PREFIX = "r2dbc";

    private static final PostgreSQLContainer PSQL_CONTAINER = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withUsername("root")
            .withPassword("password")
            .withDatabaseName("mail_service")
            .withExposedPorts(5432);

    private static final GenericContainer RABBIT_CONTAINER = new GenericContainer("rabbitmq:3-management").withExposedPorts(5672);

    @Autowired
    protected WebTestClient client;

    @Autowired
    protected MailsRepository mailsRepository;

    @DynamicPropertySource
    protected static void registerMockServer(DynamicPropertyRegistry registry) {
        PSQL_CONTAINER.start();
        RABBIT_CONTAINER.start();
        registry.add("spring.liquibase.url", PSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.r2dbc.url", () -> PSQL_CONTAINER.getJdbcUrl().replace(JDBC_PREFIX, R2DBC_PREFIX));
        registry.add("spring.r2dbc.username", PSQL_CONTAINER::getUsername);
        registry.add("spring.r2dbc.password", PSQL_CONTAINER::getPassword);
        registry.add("spring.rabbitmq.host", RABBIT_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", RABBIT_CONTAINER::getFirstMappedPort);
    }


    @BeforeEach
    void setup() {
        mailsRepository.deleteAll().block();
    }

    protected <T, R> R doPost(String uri, T request, Class<R> returnType) {
        return client.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .returnResult(returnType)
                .getResponseBody()
                .blockFirst();
    }
}
