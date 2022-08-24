package com.kme.maileverday.repository;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<UserKeyword, Long> {
    List<UserKeyword> findAllByEmail(UserEmail userEmail);
}
