package com.kchrzanowski.customer;

import com.kchrzanowski.AbstractTestcontainers;
import com.kchrzanowski.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.save(customer);

        // When
        var actual = underTest.existsCustomerByEmail(customer.getEmail());

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailDoesNotExist() {
        // Given
        String email = faker.internet().emailAddress();
        // When
        var actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.save(customer);
        Long id = underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId).findFirst().orElseThrow();

        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdFailWhenCustomerIdDoesNotExist() {
        // Given
        Long id = -1L;

        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isFalse();
    }
}