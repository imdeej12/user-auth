package com.main.repo;

import com.main.model.UserMappingWithAuthorizationGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMappingWithAuthorizationGroupRepo extends JpaRepository<UserMappingWithAuthorizationGroup, Long> {


    Optional<UserMappingWithAuthorizationGroup> findByUserIdAndStatus(Long userId, String activeStatus);

    List<UserMappingWithAuthorizationGroup> findByUserNameAndStatus(String username, String activeStatus);
}
