package com.pacifique.todoapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String home() {
        return "{\"message\":\"Welcome to app todo.\"}";
    }
}
