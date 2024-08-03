package com.main.repo;

import com.main.model.UserMappingWithCompanyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMappingWIthCompanyCodeRepo extends JpaRepository<UserMappingWithCompanyCode, Long> {

    Optional<UserMappingWithCompanyCode> findByUserIdAndStatus(Long userId, String activeStatus);

    List<UserMappingWithCompanyCode> findByUserNameAndStatus(String username, String activeStatus);
}
