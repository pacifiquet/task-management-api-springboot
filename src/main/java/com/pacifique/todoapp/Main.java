package com.pacifique.todoapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Todo Spring boot Restful API"))
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
