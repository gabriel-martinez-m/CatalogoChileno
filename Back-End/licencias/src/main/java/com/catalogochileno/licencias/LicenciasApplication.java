package com.catalogochileno.licencias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
public class LicenciasApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicenciasApplication.class, args);
    }
}