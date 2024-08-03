package com.main.common;


import com.main.UserAuthMainApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UserAuthMainApplication.class);
    }

    public ServletInitializer() {
        super();
        setRegisterErrorPageFilter(false);
    }

}
