package com.zdk.rabbitmqspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author zdk
 */
@SpringBootApplication
@EnableOpenApi
public class RabbitmqSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqSpringBootApplication.class, args);
    }

}
