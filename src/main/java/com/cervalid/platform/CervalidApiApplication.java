package com.cervalid.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.cervalid.platform")
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CervalidApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CervalidApiApplication.class, args);
	}

}
