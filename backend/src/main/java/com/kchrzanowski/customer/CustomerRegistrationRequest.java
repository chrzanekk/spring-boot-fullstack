package com.kchrzanowski.customer;

public record CustomerRegistrationRequest(
        String name, String email, int age
) {
}
