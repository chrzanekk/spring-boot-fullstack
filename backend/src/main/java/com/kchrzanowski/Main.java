package com.kchrzanowski;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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


