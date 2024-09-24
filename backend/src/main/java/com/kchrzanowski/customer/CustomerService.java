package com.kchrzanowski.customer;

import com.kchrzanowski.exception.DuplicateResourceException;
import com.kchrzanowski.exception.RequestValidationException;
import com.kchrzanowski.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id [%s] not found.".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDao.existsCustomerWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Email already exists");
        }
        customerDao.insertCustomer(
                new Customer(
                        customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age(),
                        customerRegistrationRequest.gender())
        );
    }

    public void deleteCustomer(Long id) {
        if (!customerDao.existsCustomerById(id)) {
            throw new ResourceNotFoundException("Customer with id [%s] not found.".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Long id, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(id);

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsCustomerWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("Email already exists");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (updateRequest.age() != null && updateRequest.age() != customer.getAge()) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if(updateRequest.gender() != null && updateRequest.gender() != customer.getGender()) {
            customer.setGender(updateRequest.gender());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        customerDao.updateCustomer(customer);

    }
}
