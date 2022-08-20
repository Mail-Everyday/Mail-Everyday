package com.kme.maileverday.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserKeywordRepository extends JpaRepository<UserKeyword, Long> {

    @Query("SELECT p FROM UserKeyword p WHERE p.email = :userEmail ORDER BY p.id ASC")
    List<UserKeyword> findAllAsc(@Param("userEmail") UserEmail userEmail);

    @Query("SELECT p FROM UserKeyword p WHERE p.email = :userEmail AND p.active = TRUE ORDER BY p.id ASC")
    List<UserKeyword> findActivedAllAsc(@Param("userEmail") UserEmail userEmail);
}
