package com.pacifique.todoapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Home Controller", description = "home endpoints")
public class HomeController {

    @Operation(summary = "Welcome to app todo")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to app todo.");
        return response;
    }
}
