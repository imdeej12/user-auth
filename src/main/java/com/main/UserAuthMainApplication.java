package com.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class UserAuthMainApplication extends SpringBootServletInitializer {

	 private static final Logger log = LogManager.getLogger(UserAuthMainApplication.class);
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UserAuthMainApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(UserAuthMainApplication.class, args);
    }
    
    public void run(ApplicationArguments applicationArguments) throws Exception {
    	log.debug("Log testing for debug");
    	log.info("Log testing for Info ");
    	log.warn("Log testing for warning!");
    	log.error("Log testing for  Error.");
    	log.fatal("Log testing for fatal");
    }
}
