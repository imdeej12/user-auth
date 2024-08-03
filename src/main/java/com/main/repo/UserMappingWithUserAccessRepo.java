package com.main.repo;

import com.main.model.UserMappingWithUserAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMappingWithUserAccessRepo extends JpaRepository<UserMappingWithUserAccess, Long> {


    Optional<UserMappingWithUserAccess> findByUserIdAndStatus(Long userId, String activeStatus);

    List<UserMappingWithUserAccess> findByUserNameAndStatus(String username, String activeStatus);

    List<UserMappingWithUserAccess> findByUserAccessIdAndStatusOrderByIdDesc(Long id, String activeStatus);
}
