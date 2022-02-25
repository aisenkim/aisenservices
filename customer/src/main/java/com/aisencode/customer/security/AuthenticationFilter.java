package com.aisencode.customer.security;

import com.aisencode.customer.Customer;
import com.aisencode.customer.dto.CustomerLoginRequest;
import com.aisencode.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final CustomerService customerService;
    private final Environment environment;
//    private final AuthenticationManager authenticationManager;
    private final SecretKey secretKey;

//    @Autowired
//    public AuthenticationFilter(CustomerService customerService, Environment environment, AuthenticationManager authenticationManager) {
//        this.customerService = customerService;
//        this.environment = environment;
//        super.setAuthenticationManager(authenticationManager);
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {

            CustomerLoginRequest credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), CustomerLoginRequest.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            new ArrayList<>()
                    )
            );

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        Customer customerDetailsByEmail = customerService.getCustomerDetailsByEmail(username);

        // Generate JWT token
        String token = Jwts.builder()
                .setSubject(customerDetailsByEmail.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
                .signWith(secretKey)
                .compact();


        // add token to response so that client can retrieve it
        response.addHeader("Authorization", "Bearer " + token);
    }
}
