package com.kme.maileverday.repository;

import com.kme.maileverday.entity.userEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface userRepository extends JpaRepository<userEmail, Long> {

    Optional<userEmail> findByEmail(String email);
}
