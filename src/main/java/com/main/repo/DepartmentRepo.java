package com.main.repo;

import com.main.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepo extends JpaRepository<Department, Long> {


    Optional<Department> findByDepartmentNameAndStatus(String departmentName, String activeStatus);

    Optional<Department> findByIdAndStatus(Long id, String activeStatus);

    List<Department> findByStatusOrderByModifiedOnDesc(String activeStatus);
    @Query(value="SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE state='idle'  ; ",nativeQuery = true)
    void killConnectionWhichAreIdleForFirstTime();

    @Query(value="SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE state='idle' and now()-state_change > '5 minute' \\:\\:interval  ; ",nativeQuery = true)
    void killConnectionWhichAreIdleForLastFiveMinutes();

}
