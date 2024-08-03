package com.main.repo;

import com.main.model.UserMappingWithDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMappingWithDepartmentRepo extends JpaRepository<UserMappingWithDepartment, Long>{


	Optional<UserMappingWithDepartment> findByUserIdAndStatus(Long userId, String activeStatus);

	List<UserMappingWithDepartment> findByUserNameAndStatus(String username, String activeStatus);
}
