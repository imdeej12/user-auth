package com.main.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.main.repo.DepartmentRepo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Component
public class DBConnectionKillSchedular {
	@Autowired
	DepartmentRepo departmentRepo;
	
	private String checkSchedular="Completed";
	
	private static final Logger log = LogManager.getLogger(DBConnectionKillSchedular.class);
	
	@Scheduled(fixedDelay =5*60*1000) // scheduled for 5 minutes
	public void updateEmployeeInventory(){
		
		log.info("checkSchedular1...{}",checkSchedular);
		try {
			if("Completed".equalsIgnoreCase(checkSchedular)) {
				log.info("Running only once scheduler to kill db connection which are idle ");	
				departmentRepo.killConnectionWhichAreIdleForFirstTime();
				checkSchedular="Occupied";
			}
			else {
				log.info("Running every 5 minutes scheduler to kill db connection which are idle ");	
				departmentRepo.killConnectionWhichAreIdleForLastFiveMinutes();
			}
			log.info("checkSchedular1...{}",checkSchedular);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	    
	}
}
