package com.eazybytes.eazyschool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class EazyschoolApplication implements CommandLineRunner {

	@Autowired
	ApplicationContext appCtx;

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
	}
}
