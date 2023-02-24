package org.jt.common.utils;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
		        .allowedOrigins("*")
		        .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
		        .allowCredentials(true)
		        .maxAge(3600);
    }
}

