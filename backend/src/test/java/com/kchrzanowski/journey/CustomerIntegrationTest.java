package com.kchrzanowski.journey;

import com.kchrzanowski.AbstractTestcontainers;
import com.kchrzanowski.customer.*;
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
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest extends AbstractTestcontainers {

    public static final String API_V_1_CUSTOMERS_PATH = "/api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterCustomer() {
        // create registration request
        Customer fakeCustomer = AbstractTestcontainers.createFakeRandomCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                fakeCustomer.getName(), fakeCustomer.getEmail(), fakeCustomer.getPassword(), fakeCustomer.getAge(), fakeCustomer.getGender());
        // send post request
        String jwtToken = webTestClient.post()
                .uri(API_V_1_CUSTOMERS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(API_V_1_CUSTOMERS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that customer is present
        // get customer by id
        Long id = allCustomers.stream()
                .filter(customer -> customer.email().equals(fakeCustomer.getEmail()))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                fakeCustomer.getName(),
                fakeCustomer.getEmail(),
                fakeCustomer.getGender(),
                fakeCustomer.getAge(),
                List.of("ROLE_USER"),
                fakeCustomer.getUsername());
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        webTestClient.get()
                .uri(API_V_1_CUSTOMERS_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        // create registration request
        Customer fakeCustomer = AbstractTestcontainers.createFakeRandomCustomer();
        Customer fakeCustomerToDelete = AbstractTestcontainers.createFakeRandomCustomer();

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                fakeCustomer.getName(), fakeCustomer.getEmail(), fakeCustomer.getPassword(), fakeCustomer.getAge(), fakeCustomer.getGender());

         CustomerRegistrationRequest requestToDelete = new CustomerRegistrationRequest(
                 fakeCustomerToDelete.getName(), fakeCustomerToDelete.getEmail(), fakeCustomerToDelete.getPassword(), fakeCustomerToDelete.getAge(), fakeCustomerToDelete.getGender());
        // send post request
        String jwtToken = webTestClient.post()
                .uri(API_V_1_CUSTOMERS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk().returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        webTestClient.post()
                .uri(API_V_1_CUSTOMERS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestToDelete), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(API_V_1_CUSTOMERS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(fakeCustomerToDelete.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //delete customer
        webTestClient.delete()
                .uri(API_V_1_CUSTOMERS_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri(API_V_1_CUSTOMERS_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create registration request
        Customer fakeCustomer = AbstractTestcontainers.createFakeRandomCustomer();
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(
                fakeCustomer.getName(), fakeCustomer.getEmail(), fakeCustomer.getPassword(), fakeCustomer.getAge(), fakeCustomer.getGender());
        // send post request
        String jwtToken = webTestClient.post()
                .uri(API_V_1_CUSTOMERS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);
        // get all customers

        List<Customer> allCustomers = webTestClient.get()
                .uri(API_V_1_CUSTOMERS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken)
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
                newName, fakeCustomer.getEmail(), fakeCustomer.getPassword(), fakeCustomer.getAge(), fakeCustomer.getGender());

        webTestClient.put()
                .uri(API_V_1_CUSTOMERS_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus().isOk();

        CustomerDTO updatedCustomer = webTestClient.get()
                .uri(API_V_1_CUSTOMERS_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .returnResult().getResponseBody();

        CustomerDTO expected = new CustomerDTO(id, newName, fakeCustomer.getEmail(), fakeCustomer.getGender(), fakeCustomer.getAge(), List.of("ROLE_USER"), fakeCustomer.getUsername());

        assertThat(updatedCustomer).isEqualTo(expected);
    }
}
