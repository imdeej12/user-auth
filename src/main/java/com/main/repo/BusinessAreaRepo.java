package com.main.repo;

import com.main.model.BusinessArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessAreaRepo extends JpaRepository<BusinessArea, Long> {

    Optional<BusinessArea> findByBusinessAreaIgnoreCaseAndStatus(String companyCode, String activeStatus);

    Optional<BusinessArea> findByIdAndStatus(Long id, String status);

    List<BusinessArea> findByStatusOrderByModifiedOnDesc(String activeStatus);


    List<BusinessArea> findByStatusOrderByIdDesc(String activeStatus);
}
