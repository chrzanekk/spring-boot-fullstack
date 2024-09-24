package com.kchrzanowski.journey;

import com.kchrzanowski.AbstractTestcontainers;
import com.kchrzanowski.customer.Customer;
import com.kchrzanowski.customer.CustomerRegistrationRequest;
import com.kchrzanowski.customer.CustomerUpdateRequest;
import com.kchrzanowski.customer.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest extends AbstractTestcontainers {

    public static final String API_V_1_CUSTOMERS = "/api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterCustomer() {
        // create registration request
        Customer fakeCustomer = AbstractTestcontainers.createFakeRandomCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                fakeCustomer.getName(), fakeCustomer.getEmail(), fakeCustomer.getAge(), fakeCustomer.getGender());
        // send post request
        webTestClient.post()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        // get all customers

        List<Customer> allCustomers = webTestClient.get()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that customer is present
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(fakeCustomer);

        // get customer by id
        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(fakeCustomer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        fakeCustomer.setId(id);

        webTestClient.get()
                .uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(fakeCustomer);
    }

    @Test
    void canDeleteCustomer() {
        // create registration request
        Customer fakeCustomer = AbstractTestcontainers.createFakeRandomCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                fakeCustomer.getName(), fakeCustomer.getEmail(), fakeCustomer.getAge(), fakeCustomer.getGender());
        // send post request
        webTestClient.post()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        // get all customers

        List<Customer> allCustomers = webTestClient.get()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(fakeCustomer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //delete customer
        webTestClient.delete()
                .uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create registration request
        Customer fakeCustomer = AbstractTestcontainers.createFakeRandomCustomer();
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(
                fakeCustomer.getName(), fakeCustomer.getEmail(), fakeCustomer.getAge(), fakeCustomer.getGender());
        // send post request
        webTestClient.post()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        // get all customers

        List<Customer> allCustomers = webTestClient.get()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(fakeCustomer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //update customer
        String newName = "Kondzio";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName, fakeCustomer.getEmail(), fakeCustomer.getAge(), fakeCustomer.getGender());

        webTestClient.put()
                .uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus().isOk();

        Customer updatedCustomer = webTestClient.get()
                .uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .returnResult().getResponseBody();

        Customer expected = new Customer(id, newName, fakeCustomer.getEmail(), fakeCustomer.getAge(), fakeCustomer.getGender());

        assertThat(updatedCustomer).isEqualTo(expected);
    }
}
