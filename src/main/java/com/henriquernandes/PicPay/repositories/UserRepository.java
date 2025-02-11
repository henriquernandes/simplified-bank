package com.henriquernandes.PicPay.repositories;

import com.henriquernandes.PicPay.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCpfCnpj(String cpfCnpj);
    Optional<User> findByEmail(String email);
}
