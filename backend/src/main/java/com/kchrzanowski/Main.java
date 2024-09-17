package com.kchrzanowski;

import com.github.javafaker.Faker;
import com.kchrzanowski.customer.Customer;
import com.kchrzanowski.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

//    @Bean
//    CommandLineRunner runner(CustomerRepository customerRepository) {
//        return args -> {
//            Faker faker = new Faker();
//            var firstName = faker.name().firstName();
//            var lastName = faker.name().lastName();
//            var email = faker.internet().domainName();
//            var age = faker.number().numberBetween(1, 50);
//            Customer customer = new Customer(
//                    firstName + " " + lastName,
//                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + email,
//                    age);
//            customerRepository.save(customer);
//        };
//    }
}


