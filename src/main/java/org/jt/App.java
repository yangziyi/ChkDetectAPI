package org.jt;

import org.jt.datasource.dynamic.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({DynamicDataSourceRegister.class})
public class App 
{
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
