package com.pdp.transaction;

import com.pdp.transaction.persistent.repository.TransactionRepository;
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
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@AutoConfigureWebTestClient(timeout = "PT10M")
@Tag("FunctionalTest")
public abstract class BaseFunctionalTest extends BaseTest {

    @Autowired
    protected WebTestClient client;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final CPTestContainerFactory FACTORY = new CPTestContainerFactory();

    private static final KafkaContainer KAFKA = FACTORY.createKafka().withEnv("KAFKA_CREATE_TOPICS", "transaction-topic:1:1").withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true");

    private static final SchemaRegistryContainer SCHEMA_REGISTRY = FACTORY.createSchemaRegistry(KAFKA);
    private static final PostgreSQLContainer PSQL_CONTAINER = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withUsername("root")
            .withPassword("password")
            .withDatabaseName("transactionService")
            .withExposedPorts(5432);

    @DynamicPropertySource
    protected static void registerMockServer(DynamicPropertyRegistry registry) {
        PSQL_CONTAINER.start();
        SCHEMA_REGISTRY.start();
        registry.add("spring.liquibase.url", PSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.url", PSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", PSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", PSQL_CONTAINER::getPassword);
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.producer.properties.schema.registry.url", SCHEMA_REGISTRY::getBaseUrl);
        registry.add("spring.kafka.consumer.properties.schema.registry.url", SCHEMA_REGISTRY::getBaseUrl);
    }

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll().block();
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
