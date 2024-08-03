package com.main.repo;

import com.main.model.CompanyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyCodeRepo extends JpaRepository<CompanyCode, Long> {

    Optional<CompanyCode> findByCompanyCodeIgnoreCaseAndStatus(String companyCode, String activeStatus);

    Optional<CompanyCode> findByIdAndStatus(Long id, String status);

    List<CompanyCode> findByStatusOrderByModifiedOnDesc(String activeStatus);


}
