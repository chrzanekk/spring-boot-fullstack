package com.kchrzanowski.auth;

import com.kchrzanowski.customer.Customer;
import com.kchrzanowski.customer.CustomerDTO;
import com.kchrzanowski.customer.CustomerDTOMapper;
import com.kchrzanowski.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JWTUtil jwtUtil;


    public AuthenticationService(AuthenticationManager authenticationManager, CustomerDTOMapper customerDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        Authentication authenticated = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()
                )
        );
        Customer principal = (Customer) authenticated.getPrincipal();
        CustomerDTO customerDTO = customerDTOMapper.apply(principal);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customerDTO.id());
        claims.put("name", customerDTO.name());
        claims.put("email", customerDTO.email());
        claims.put("scopes", customerDTO.roles());
        claims.put("gender", customerDTO.gender());
        claims.put("age", customerDTO.age());
        String token = jwtUtil.issueToken(customerDTO.username(), claims);
        return new AuthenticationResponse(token, customerDTO);
    }
}
