package com.pacifique.todoapp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
public class HomeController {

    @Operation(summary = "Welcome to app todo")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String home() {
        return "{\"message\":\"Welcome to app todo.\"}";
    }
}
