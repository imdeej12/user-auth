package com.main.repo;

import com.main.model.AuthorizationGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorizationGroupRepo extends JpaRepository<AuthorizationGroup, Long> {

    Optional<AuthorizationGroup> findByAuthorizationGroupCodeIgnoreCaseAndStatus(String authorizationGroupCode, String activeStatus);

    Optional<AuthorizationGroup> findByIdAndStatus(Long id, String activeStatus);

    List<AuthorizationGroup> findByStatusOrderByModifiedOnDesc(String activeStatus);


}
