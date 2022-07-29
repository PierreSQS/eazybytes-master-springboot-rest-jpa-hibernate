package com.eazybytes.eazyschool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class EazyschoolApplication implements CommandLineRunner {

	private final ApplicationContext appCtx;

	private final Environment env;

	public EazyschoolApplication(ApplicationContext appCtx, Environment env) {
		this.appCtx = appCtx;
		this.env = env;
	}

	public static void main(String[] args) {
		SpringApplication.run(EazyschoolApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("########## Let's inspect the beans in the context with " +
				"name containing 'profileController'##########");

		String[] beanNames = appCtx.getBeanDefinitionNames();

		Arrays.stream(beanNames)
				.filter(beanName -> beanName.contains("rofileController"))
				.forEach(log::info);

		log.info("########## username: {} ##########",env.getProperty("USERNAME")); // System Env. variable
		log.info("########## custom page size: {} ##########",env.getProperty("eazyschool.pageSize"));
	}
}
