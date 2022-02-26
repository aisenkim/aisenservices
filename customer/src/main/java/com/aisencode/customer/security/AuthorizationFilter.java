package com.aisencode.customer.security;

import com.aisencode.customer.Customer;
import com.aisencode.customer.repository.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final Environment environment;
    private final CustomerRepository customerRepository;

    public AuthorizationFilter(AuthenticationManager authenticationManager, CustomerRepository customerRepository, Environment environment) {
        super(authenticationManager);
        this.customerRepository = customerRepository;
        this.environment = environment;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        String key = null;

        if (token != null) {

            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

            try {
                key = environment.getProperty("token.secret");
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            String userId = claimsJws.getBody().getSubject(); // email

            if (userId == null || userId.isEmpty())
                return null;

            Customer customer = customerRepository.findCustomerByEmail(userId);
            UserPrincipal userPrincipal = new UserPrincipal(customer);
            return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        }

        return null;
    }
}
