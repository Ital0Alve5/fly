package com.dac.fly.saga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.dac.fly.saga.feign")
public class SagaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagaApplication.class, args);
    }

}
