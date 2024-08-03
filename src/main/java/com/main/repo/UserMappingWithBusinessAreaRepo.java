package com.main.repo;

import com.main.model.UserMappingWithBusinessArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMappingWithBusinessAreaRepo extends JpaRepository<UserMappingWithBusinessArea, Long> {


    Optional<UserMappingWithBusinessArea> findByUserIdAndStatus(Long  userId, String activeStatus);

    List<UserMappingWithBusinessArea> findByUserNameAndStatus(String username, String activeStatus);
}
