package com.kchrzanowski;

import com.github.javafaker.Faker;
import com.kchrzanowski.customer.Customer;
import com.kchrzanowski.customer.CustomerRegistrationRequest;
import com.kchrzanowski.customer.CustomerService;
import com.kchrzanowski.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

//    @Bean
//    CommandLineRunner runner(CustomerService customerService) {
//        return args -> {
//            CustomerRegistrationRequest customer = createRandomCustomerRegistrationRequest();
//            CustomerRegistrationRequest customer2 = createRandomCustomerRegistrationRequest();
//            CustomerRegistrationRequest customer3 = createRandomCustomerRegistrationRequest();
//            CustomerRegistrationRequest customer4 = createRandomCustomerRegistrationRequest();
//            List<CustomerRegistrationRequest> list = List.of(customer, customer2, customer3, customer4);
//            list.forEach(customerService::addCustomer);
//        };
//    }

    private static CustomerRegistrationRequest createRandomCustomerRegistrationRequest() {
        Faker faker = new Faker();
        var firstName = faker.name().firstName();
        var lastName = faker.name().lastName();
        var email = faker.internet().domainName();
        var age = faker.number().numberBetween(16, 70);
        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        return new CustomerRegistrationRequest(
                firstName + " " + lastName,
                firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + email,
                "password", age, gender);
    }
}


