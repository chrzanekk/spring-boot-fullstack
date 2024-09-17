package com.kchrzanowski.customer;

import com.kchrzanowski.AbstractTestcontainers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();

        // Then
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        Long id = 1L;

        // When
        underTest.selectCustomerById(id);

        // Then
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();

        // When
        underTest.insertCustomer(customer);

        // Then
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void existsCustomerWithEmail() {
        // Given
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();

        // When
        underTest.existsCustomerWithEmail(customer.getEmail());

        // Then
        verify(customerRepository, times(1)).existsCustomerByEmail(customer.getEmail());
    }

    @Test
    void deleteCustomerById() {
        // Given
        Long id = 1L;

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerRepository, times(1)).deleteById(id);
    }

    @Test
    void existsCustomerById() {
        // Given
        Long id = 1L;

        // When
        underTest.existsCustomerById(id);

        // Then
        verify(customerRepository, times(1)).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("email@email.com");

        // When
        underTest.updateCustomer(customer);

        // Then
        verify(customerRepository, times(1)).save(customer);
    }
}