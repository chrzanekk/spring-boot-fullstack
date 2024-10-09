package com.kchrzanowski.journey;

import com.kchrzanowski.AbstractTestcontainers;
import com.kchrzanowski.auth.AuthenticationRequest;
import com.kchrzanowski.auth.AuthenticationResponse;
import com.kchrzanowski.customer.Customer;
import com.kchrzanowski.customer.CustomerDTO;
import com.kchrzanowski.customer.CustomerRegistrationRequest;
import com.kchrzanowski.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthenticationIntegrationTest extends AbstractTestcontainers {

    public static final String API_V_1_AUTH_PATH = "/api/v1/auth";
    public static final String API_V_1_CUSTOMERS_PATH = "/api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    void canLogin() {
        Customer fakeCustomer = AbstractTestcontainers.createFakeRandomCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                fakeCustomer.getName(), fakeCustomer.getEmail(), fakeCustomer.getPassword(), fakeCustomer.getAge(), fakeCustomer.getGender());

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(fakeCustomer.getEmail(), fakeCustomer.getPassword());


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


        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(API_V_1_AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus().isOk().expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                }).returnResult();

        String token = result.getResponseHeaders().get(AUTHORIZATION).get(0);
        AuthenticationResponse response = result.getResponseBody();

        CustomerDTO customerDTO = response.customerDTO();

        assertThat(jwtUtil.isTokenValid(token, customerDTO.username()));
        assertThat(customerDTO.email()).isEqualTo(fakeCustomer.getEmail());
        assertThat(customerDTO.name()).isEqualTo(fakeCustomer.getName());
        assertThat(customerDTO.age()).isEqualTo(fakeCustomer.getAge());
        assertThat(customerDTO.gender()).isEqualTo(fakeCustomer.getGender());
        assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));
    }
}
