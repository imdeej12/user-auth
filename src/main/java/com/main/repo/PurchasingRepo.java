package com.main.repo;

import com.main.model.Purchasing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchasingRepo extends JpaRepository<Purchasing, Long> {

    Optional<Purchasing> findByPurchasingIgnoreCaseAndStatus(String purchasing, String activeStatus);

    Optional<Purchasing> findByIdAndStatus(Long id,String activeStatus);

    List<Purchasing> findByStatusOrderByModifiedOnDesc(String activeStatus);


    Optional<Purchasing> findByPurchasingAndAuthorizationGroupIdAndStatus(String purchasing, Long authorizationGroupId, String activeStatus);
}
