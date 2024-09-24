package com.kchrzanowski.customer;

import com.kchrzanowski.AbstractTestcontainers;
import com.kchrzanowski.exception.DuplicateResourceException;
import com.kchrzanowski.exception.RequestValidationException;
import com.kchrzanowski.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();
        // Then
        verify(customerDao, times(1)).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(id);

        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void throwExceptionWhenCustomerDoesNotExist() {
        // Given
        Long id = 1L;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found.".formatted(id));
    }

    @Test
    void canAddCustomer() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        customer.setId(id);
        when(customerDao.existsCustomerWithEmail(customer.getEmail())).thenReturn(false);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                customer.getName(), customer.getEmail(), customer.getAge(),
                Gender.MALE);

        // When
        underTest.addCustomer(customerRegistrationRequest);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao, times(1)).insertCustomer(customerArgumentCaptor.capture());
        Customer customerCapture = customerArgumentCaptor.getValue();
        assertThat(customerCapture.getId()).isNull();
        assertThat(customerCapture.getName()).isEqualTo(customerRegistrationRequest.name());
        assertThat(customerCapture.getEmail()).isEqualTo(customerRegistrationRequest.email());
        assertThat(customerCapture.getAge()).isEqualTo(customerRegistrationRequest.age());
        assertThat(customerCapture.getGender()).isEqualTo(customerRegistrationRequest.gender());
    }

    @Test
    void throwExceptionWhenCustomerEmailExists() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        customer.setId(id);
        when(customerDao.existsCustomerWithEmail(customer.getEmail())).thenReturn(true);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                customer.getName(), customer.getEmail(), customer.getAge(),
                Gender.MALE);

        // When
        // Then
        assertThatThrownBy(() -> underTest.addCustomer(customerRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");

        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void canDeleteCustomer() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.existsCustomerById(id)).thenReturn(true);

        // When
        underTest.deleteCustomer(id);

        // Then
        verify(customerDao, times(1)).deleteCustomerById(id);
    }

    @Test
    void throwExceptionWhenDeleteCustomerDoesNotExist() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.existsCustomerById(id)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found.".formatted(id));
        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void updateAllCustomerProperties() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "test@test.pl";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Kondzio", newEmail, customer.getAge() + 12, customer.getGender());
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);
        // When
        underTest.updateCustomer(id, customerUpdateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao, times(1)).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCapture = customerArgumentCaptor.getValue();
        assertThat(customerCapture.getAge()).isEqualTo(customerUpdateRequest.age());
        assertThat(customerCapture.getName()).isEqualTo(customerUpdateRequest.name());
        assertThat(customerCapture.getEmail()).isEqualTo(customerUpdateRequest.email());
        assertThat(customerCapture.getGender()).isEqualTo(customerUpdateRequest.gender());
    }

    @Test
    void updateOnlyCustomerName() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Kondzio", null, null, Gender.MALE);
        // When
        underTest.updateCustomer(id, customerUpdateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao, times(1)).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCapture = customerArgumentCaptor.getValue();
        assertThat(customerCapture.getName()).isEqualTo(customerUpdateRequest.name());
        assertThat(customerCapture.getAge()).isEqualTo(customer.getAge());
        assertThat(customerCapture.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerCapture.getGender()).isEqualTo(customer.getGender());
    }

    @Test
    void updateOnlyCustomerEmail() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "test@test.pl";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, newEmail, null, Gender.MALE);
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, customerUpdateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao, times(1)).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCapture = customerArgumentCaptor.getValue();
        assertThat(customerCapture.getName()).isEqualTo(customer.getName());
        assertThat(customerCapture.getAge()).isEqualTo(customer.getAge());
        assertThat(customerCapture.getEmail()).isEqualTo(newEmail);
        assertThat(customerCapture.getGender()).isEqualTo(customer.getGender());
    }

    @Test
    void willThrowWhenTryingUpdateOnlyCustomerEmailWhenAlreadyExists() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "test@test.pl";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, newEmail, null, null);
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "test@test.pl";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge(), Gender.MALE);

        // When
        // Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void updateOnlyCustomerAge() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        int newAge = customer.getAge() + 11;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, null, newAge, null);
        // When
        underTest.updateCustomer(id, customerUpdateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao, times(1)).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCapture = customerArgumentCaptor.getValue();
        assertThat(customerCapture.getName()).isEqualTo(customer.getName());
        assertThat(customerCapture.getAge()).isEqualTo(newAge);
        assertThat(customerCapture.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerCapture.getGender()).isEqualTo(customer.getGender());
    }

    @Test
    void updateOnlyCustomerGender() {
        // Given
        Long id = 1L;
        Customer customer = AbstractTestcontainers.createFakeRandomCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        var newGender = customer.getGender().equals(Gender.MALE) ? Gender.FEMALE : Gender.MALE;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, null, null, newGender);
        // When
        underTest.updateCustomer(id, customerUpdateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao, times(1)).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCapture = customerArgumentCaptor.getValue();
        assertThat(customerCapture.getName()).isEqualTo(customer.getName());
        assertThat(customerCapture.getAge()).isEqualTo(customer.getAge());
        assertThat(customerCapture.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerCapture.getGender()).isEqualTo(newGender);
    }
}