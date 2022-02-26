package com.aisencode.customer;

import com.aisencode.customer.repository.AuthorityRepository;
import com.aisencode.customer.repository.CustomerRepository;
import com.aisencode.customer.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class InitialAdminSetup {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Authority read_authority = createAuthority("READ_AUTHORITY");
        Authority write_authority = createAuthority("WRITE_AUTHORITY");
        Authority delete_authority = createAuthority("DELETE_AUTHORITY");

        Role role_user = createRole("ROLE_USER", Arrays.asList(read_authority, write_authority));
        Role role_admin = createRole("ROLE_ADMIN", Arrays.asList(read_authority, write_authority, delete_authority));

        if (role_admin == null)
            return;

        String password = bCryptPasswordEncoder.encode("password");

        Customer customer = Customer.builder()
                .firstName("root")
                .lastName("admin")
                .email("aisencode@gmail.com")
                .password(password)
                .roles(List.of(role_admin))
                .build();
        customerRepository.save(customer);
    }


    private Authority createAuthority(String name) {
        Authority authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = Authority.builder().name(name).build();
            authorityRepository.save(authority);
        }
        return authority;
    }

    private Role createRole(String name, List<Authority> authorities) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = Role.builder().name(name).authorities(authorities).build();
            roleRepository.save(role);
        }
        return role;
    }
}
