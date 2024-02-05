package com.eazybytes.eazyschool;

import com.eazybytes.eazyschool.config.EazySchoolProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class EazyschoolApplication implements CommandLineRunner {

	private final ApplicationContext appCtx;

	private final Environment env;

	private final EazySchoolProps eazySchoolProps;


	public static void main(String[] args) {
		SpringApplication.run(EazyschoolApplication.class, args);
	}

	@Override
	public void run(String... args) {
		displayBeansByName();
		displayCustomProps();
		displayCitiesFromPropsFile(eazySchoolProps);
		displayContactsFromPropsFile(eazySchoolProps);
	}

	private void displayCustomProps() {
		log.info("########## username: {} ##########",env.getProperty("USERNAME")); // System Env. variable
		log.info("########## custom default page size: {} ##########",env.getProperty("eazyschool.defaultPageSize"));
	}

	private void displayBeansByName() {
		log.info("########## Let's inspect the beans in the context with " +
				"name containing 'profileController'##########");

		String[] beanNames = appCtx.getBeanDefinitionNames();

		Arrays.stream(beanNames)
				.filter(beanName -> beanName.contains("rofileController"))
				.forEach(log::info);
	}

	private void displayCitiesFromPropsFile(EazySchoolProps eazySchoolProps) {
		log.info("");
		log.info("########## the cities in props-file ##########");
		eazySchoolProps.getCities().forEach(city -> log.info("########## {} ##########",city));
	}

	private void displayContactsFromPropsFile(EazySchoolProps eazySchoolProps) {
		log.info("");
		log.info("########## the contacts in props-file ##########");
		eazySchoolProps.getContact().entrySet().forEach(mapEntry -> log.info("########## {} ##########",mapEntry));
	}
}
