package com.kchrzanowski.auth;

import com.kchrzanowski.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO) {
}
