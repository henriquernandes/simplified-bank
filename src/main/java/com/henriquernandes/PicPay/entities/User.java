package com.henriquernandes.PicPay.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.henriquernandes.PicPay.dtos.UserDTO;
import com.henriquernandes.PicPay.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Table(name = "users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private String cpfCnpj;

    @Column(unique = true)
    private String email;

    @Column
    @JsonIgnore
    private String password;

    @Column
    private BigDecimal balance;

    @Column
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }


    public User(UserDTO data) {
        this.firstName = data.firstName();
        this.lastName = data.lastName();
        this.cpfCnpj = data.cpfCnpj();
        this.email = data.email();
        this.password = data.password();
        this.balance = data.balance() == null ? BigDecimal.ZERO : data.balance();
        this.type = data.type();
    }

    public User(String firstName, String lastName, String cpfCnpj, String email, String password, BigDecimal balance, UserType type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.type = type;
    }
}
