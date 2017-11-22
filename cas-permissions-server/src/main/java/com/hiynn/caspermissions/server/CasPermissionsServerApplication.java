package com.hiynn.caspermissions.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

@SpringBootApplication
public class CasPermissionsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasPermissionsServerApplication.class, args);
	}

}
