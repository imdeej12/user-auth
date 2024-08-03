package com.main.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.model.ProjectEntity;

public interface ProjectRepo extends JpaRepository<ProjectEntity, Long>{

	List<ProjectEntity> findByStatus(String status);

	Optional<ProjectEntity> findByProjectAndStatus(String project, String activeStatus);

	Optional<ProjectEntity> findByIdAndStatus(Long id, String activeStatus);

	Optional<ProjectEntity> findByProjectIgnoreCaseAndStatus(String project, String activeStatus);


}
