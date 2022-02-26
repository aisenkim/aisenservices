package com.aisencode.customer.security;

import com.aisencode.customer.Authority;
import com.aisencode.customer.Customer;
import com.aisencode.customer.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class UserPrincipal implements UserDetails {

    private final Customer customer;
    private Long id;

    public UserPrincipal(Customer customer) {
        this.customer = customer;
        this.id = customer.getId();
    }

    /**
     * Will return GrantedAuthority List containing both
     * Roles and Authorities names
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // RETURNING TO SPRING SECURITY
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Collection<Authority> authorityEntities = new ArrayList<>();

        // Get user roles
        Collection<Role> roles = customer.getRoles();
        if (roles == null)
            return authorities;

        roles.forEach(role -> {
            // Add role name to GrantedAuthority
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            // Get all authorities form the role this user has
            authorityEntities.addAll(role.getAuthorities());
        });

        // Adding authorities to Granted Authority
        authorityEntities.forEach(authorityEntity ->
                // Add authority name to GrantedAuthority
                authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()))
        );

        return authorities;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        // Returning email because that is what I set for the username for UserDetails (Security)
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // THIS IS FOR EMAIL VERIFICATION (example below)
        // return this.customer.getEmailVerificationStatus();
        // TRUE MEANS EMAIL AUTHENTICATED
        // FALSE - NOT AUTHENTICATED
        return true;
    }
}
