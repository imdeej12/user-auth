
package com.main.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.model.Designation;

public interface DesignationRepo extends JpaRepository<Designation, Long> {

	Optional<Designation> findByIdAndStatus(Long id, String activeStatus);

	List<Designation> findByStatus(String activeStatus);

	Optional<Designation> findByDesignationAndStatus(String designation, String activeStatus);

	Optional<Designation> findByDesignationIgnoreCaseAndStatus(String designation, String activeStatus);

}
