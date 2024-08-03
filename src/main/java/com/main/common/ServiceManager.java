package com.main.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.repo.DepartmentRepo;
import com.main.repo.DesignationRepo;
import com.main.repo.UserRepo;

@Service
public class ServiceManager {

	@Autowired
	public UserRepo userRepo;
	
	@Autowired
	public DepartmentRepo departmentRepo;
	
	@Autowired
	public DesignationRepo designationRepo;


	
	
	
}
