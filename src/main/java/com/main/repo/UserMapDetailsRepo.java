package com.main.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.model.UserMapDetails;

public interface UserMapDetailsRepo extends JpaRepository<UserMapDetails, Long> {

	List<UserMapDetails> findByStatus(String activeStatus);

	List<UserMapDetails> findByUserIdAndStatus(Long id, String activeStatus);




}
