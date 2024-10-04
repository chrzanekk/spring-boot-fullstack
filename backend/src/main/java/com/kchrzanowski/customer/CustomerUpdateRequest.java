package com.kchrzanowski.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        String password,
        Integer age,
        Gender gender) {
}
