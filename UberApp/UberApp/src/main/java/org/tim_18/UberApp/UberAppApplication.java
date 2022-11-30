package org.tim_18.UberApp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"org.tim_18.UberApp.controller"})
@EnableJpaRepositories("org.tim_18.UberApp.*")
@ComponentScan(basePackages = { "org.tim_18.UberApp.*" })
@EntityScan("org.tim_18.UberApp.*")
public class UberAppApplication {


	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(UberAppApplication.class, args);
	}

}
