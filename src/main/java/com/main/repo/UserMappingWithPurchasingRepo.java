package com.main.repo;

import com.main.model.User;
import com.main.model.UserMappingWithPurchasing;
import com.main.model.UserMappingWithUserAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface UserMappingWithPurchasingRepo extends JpaRepository<UserMappingWithPurchasing, Long> {


    List<UserMappingWithPurchasing> findByUserNameAndStatus(String username, String activeStatus);
}
