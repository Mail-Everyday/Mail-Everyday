package com.kme.maileverday.repository;

import com.kme.maileverday.entity.UserKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<UserKeyword, Long> {
}
