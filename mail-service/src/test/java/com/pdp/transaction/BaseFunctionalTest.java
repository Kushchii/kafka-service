package com.pdp.transaction;

import com.pdp.transaction.persistent.repository.MailsRepository;
import net.christophschubert.cp.testcontainers.CPTestContainerFactory;
import net.christophschubert.cp.testcontainers.SchemaRegistryContainer;
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
import org.testcontainers.containers.KafkaContainer;
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

    private static final CPTestContainerFactory FACTORY = new CPTestContainerFactory();

    private static final KafkaContainer KAFKA = FACTORY.createKafka().withEnv("KAFKA_CREATE_TOPICS", "transaction-topic:1:1").withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true");

    private static final SchemaRegistryContainer SCHEMA_REGISTRY = FACTORY.createSchemaRegistry(KAFKA);

    private static final PostgreSQLContainer PSQL_CONTAINER = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withUsername("root")
            .withPassword("password")
            .withDatabaseName("mail_service")
            .withExposedPorts(5432);

    @Autowired
    protected WebTestClient client;

    @Autowired
    protected MailsRepository mailsRepository;

    @DynamicPropertySource
    protected static void registerMockServer(DynamicPropertyRegistry registry) {
        PSQL_CONTAINER.start();
        SCHEMA_REGISTRY.start();
        registry.add("spring.liquibase.url", PSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.r2dbc.url", () -> PSQL_CONTAINER.getJdbcUrl().replace(JDBC_PREFIX, R2DBC_PREFIX));
        registry.add("spring.r2dbc.username", PSQL_CONTAINER::getUsername);
        registry.add("spring.r2dbc.password", PSQL_CONTAINER::getPassword);
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.producer.properties.schema.registry.url", SCHEMA_REGISTRY::getBaseUrl);
        registry.add("spring.kafka.consumer.properties.schema.registry.url", SCHEMA_REGISTRY::getBaseUrl);
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
