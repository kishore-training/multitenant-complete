package com.udemy.multitenancy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@ComponentScan({"com.udemy.multitenancy.service","com.udemy.multitenancy.security"})
@EnableMongoRepositories
@EnableSwagger2
@EntityScan(basePackages = "{com.udemy.multitenancy.model,com.udemy.multitenancy.vo,com.udemy.multitenancy.security}")
public class MultitenancyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultitenancyApplication.class, args);
    }


}
