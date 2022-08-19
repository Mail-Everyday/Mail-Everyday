package com.kme.maileverday.repository;

import com.kme.maileverday.entity.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<UserEmail, Long> {
    UserEmail findByEmail(String email);
}