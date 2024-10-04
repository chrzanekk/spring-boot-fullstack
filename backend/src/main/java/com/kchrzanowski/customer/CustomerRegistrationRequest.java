package com.kchrzanowski.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        String password,
        int age,
        Gender gender) {
}
