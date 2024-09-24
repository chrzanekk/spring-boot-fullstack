package com.kchrzanowski.customer;

import com.kchrzanowski.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);
        // When
        List<Customer> customers = underTest.selectAllCustomers();
        // Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId).findFirst().orElseThrow();

        // When
        Optional<Customer> optionalCustomer = underTest.selectCustomerById(id);

        // Then
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerId() {
        // Given
        Long id = -1L;

        // When
        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();

        // When
        underTest.insertCustomer(customer);

        // Then
        assertThat(underTest.selectAllCustomers()).isNotEmpty();
    }

    @Test
    void existsCustomerWithEmail() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);
        // When
        var actual = underTest.existsCustomerWithEmail(customer.getEmail());
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWhitEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = faker.internet().emailAddress() + UUID.randomUUID();
        // When
        var actual = underTest.existsCustomerWithEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerWhitEmailReturnsFalseWhenNull() {
        // When
        var actual = underTest.existsCustomerWithEmail(null);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerWhitEmailReturnsFalseWhenEmptyString() {
        // When
        var actual = underTest.existsCustomerWithEmail("");
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId).findFirst().orElseThrow();

        // When
        underTest.deleteCustomerById(id);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerById(id);
        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void existsCustomerById() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId).findFirst().orElseThrow();

        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdWillReturnFalseWhenIdNotPresent() {
        // Given
        Long id = -1L;

        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerByIdWillReturnFalseWhenIdIsNull() {
        // When
        var actual = underTest.existsCustomerById(null);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerAllData() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);

        Customer actual = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst().orElseThrow();

        String newEmail = faker.internet().safeEmailAddress();
        String newName = faker.name().fullName();
        int newAge = faker.number().numberBetween(1, 100);

        Customer toUpdate = new Customer();
        toUpdate.setId(actual.getId());
        toUpdate.setEmail(newEmail);
        toUpdate.setName(newName);
        toUpdate.setAge(newAge);
        toUpdate.setGender(Gender.MALE);

        // When
        underTest.updateCustomer(toUpdate);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerById(actual.getId());
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(updated -> {
          assertThat(updated.getId()).isEqualTo(actual.getId());
          assertThat(updated.getName()).isEqualTo(newName);
          assertThat(updated.getAge()).isEqualTo(newAge);
          assertThat(updated.getGender()).isEqualTo(Gender.MALE);
        });
    }

    @Test
    void updateCustomerNothingToUpdate() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);

        Customer actual = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst().orElseThrow();

        Customer toUpdate = new Customer();
        toUpdate.setId(actual.getId());

        // When
        underTest.updateCustomer(toUpdate);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerById(actual.getId());
        assertThat(optionalCustomer).isPresent().hasValue(actual);
    }

    @Test
    void updateCustomerEmail() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);

        Customer actual = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst().orElseThrow();

        String newEmail = faker.internet().safeEmailAddress();

        Customer toUpdate = new Customer();
        toUpdate.setId(actual.getId());
        toUpdate.setEmail(newEmail);

        // When
        underTest.updateCustomer(toUpdate);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerById(actual.getId());
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(actual.getId());
            assertThat(c.getName()).isEqualTo(actual.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(actual.getAge());
        });
    }

    @Test
    void updateCustomerName() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);

        Customer actual = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst().orElseThrow();

        String newName = faker.name().fullName();

        Customer toUpdate = new Customer();
        toUpdate.setId(actual.getId());
        toUpdate.setName(newName);

        // When
        underTest.updateCustomer(toUpdate);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerById(actual.getId());
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(actual.getId());
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(actual.getEmail());
            assertThat(c.getAge()).isEqualTo(actual.getAge());
            assertThat(c.getGender()).isEqualTo(actual.getGender());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        underTest.insertCustomer(customer);

        Customer actual = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst().orElseThrow();

        int newAge = actual.getAge() + 12;

        Customer toUpdate = new Customer();
        toUpdate.setId(actual.getId());
        toUpdate.setAge(newAge);

        // When
        underTest.updateCustomer(toUpdate);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerById(actual.getId());
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(actual.getId());
            assertThat(c.getName()).isEqualTo(actual.getName());
            assertThat(c.getEmail()).isEqualTo(actual.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getGender()).isEqualTo(actual.getGender());
        });
    }
}