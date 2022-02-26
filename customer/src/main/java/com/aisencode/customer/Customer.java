package com.aisencode.customer;

import javax.validation.constraints.*;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @SequenceGenerator(name = "customer_id_sequence", sequenceName = "customer_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_sequence")
    private Long id;

    @NotNull(message = "Firstname can't be blank!")
    @Size(min = 2, message = "First name must not be less than 2 characters")
    private String firstName;

    @NotNull(message = "Lastname can't be blank!")
    @Size(min = 2, message = "Last name must not be less than 2 characters")
    private String lastName;

    @NotNull(message = "Email can't be blank")
    @Email
    private String email;

    @NotNull(message = "Password can't be blank")
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "customer_roles", joinColumns=@JoinColumn(name="customer_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="roles_id", referencedColumnName = "id"))
    private Collection<Role> roles;

}
