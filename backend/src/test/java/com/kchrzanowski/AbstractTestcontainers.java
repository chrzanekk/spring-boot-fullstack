package com.kchrzanowski;

import com.github.javafaker.Faker;
import com.kchrzanowski.customer.Customer;
import com.kchrzanowski.customer.Gender;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.UUID;

@Testcontainers
public abstract class AbstractTestcontainers {

    @BeforeAll
    static void setup() {
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        ).load();
        flyway.migrate();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("postgres-dao-unit-test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    protected static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    private static DataSource getDataSource() {
        DataSourceBuilder<?> build = DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword());
        return build.build();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    public static final Faker faker = new Faker();

    public static Customer createFakeRandomCustomer() {
        var firstName = faker.name().firstName();
        var lastName = faker.name().lastName();
        var email = faker.internet().domainName();
        var age = faker.number().numberBetween(1, 50);
        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        return new Customer(
                firstName + " " + lastName,
                firstName.toLowerCase() + "." + lastName.toLowerCase() + "-" + UUID.randomUUID() + "@" + email,
                age, gender);
    }
}
