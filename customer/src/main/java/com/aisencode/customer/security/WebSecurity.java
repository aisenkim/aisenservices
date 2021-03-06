package com.aisencode.customer.security;

import com.aisencode.customer.repository.CustomerRepository;
import com.aisencode.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // need this for method level security
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final Environment environment; // FOR USING DEFINED IP ADDRESS OF APIGW INSIDE CONFIG FILE
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomerService customerService;
    private final SecretKey secretKey;
    private final CustomerRepository customerRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.authorizeRequests()
//                .antMatchers("/api/v1/customers/**")
//                .permitAll();

        // ONLY ALLOW REQUESTS FROM APIGW
        http.authorizeRequests()
//                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")  // Trying method level security
                .antMatchers("/**")
                .hasIpAddress(environment.getProperty("gateway.ip"))
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager(), customerRepository, environment)); // adding fields because AuthorizationFilter can't do constructor injection due to super constructor

        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customerService, environment, secretKey);
        authenticationFilter.setAuthenticationManager(authenticationManager());
        // FOR SETTING CUSTOM LOGIN URL
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));

        return authenticationFilter;
    }


    /**
     * Tells Spring Security where to look for User/Customer information
     * and which password encoder to use
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerService).passwordEncoder(bCryptPasswordEncoder);
    }
}
