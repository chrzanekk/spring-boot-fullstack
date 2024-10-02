package com.kchrzanowski;

import com.github.javafaker.Faker;
import com.kchrzanowski.customer.Customer;
import com.kchrzanowski.customer.CustomerRepository;
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
//    CommandLineRunner runner(CustomerRepository customerRepository) {
//        return args -> {
//            Customer customer = createRandomCustomer();
//            Customer customer2 = createRandomCustomer();
//            Customer customer3 = createRandomCustomer();
//            Customer customer4 = createRandomCustomer();
//            customerRepository.saveAll(List.of(customer, customer2, customer3, customer4));
//        };
//    }
//
//    private static Customer createRandomCustomer() {
//        Faker faker = new Faker();
//        var firstName = faker.name().firstName();
//        var lastName = faker.name().lastName();
//        var email = faker.internet().domainName();
//        var age = faker.number().numberBetween(16, 70);
//        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
//        Customer customer = new Customer(
//                firstName + " " + lastName,
//                firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + email,
//                age, gender);
//        return customer;
//    }
}


