package com.pacifique.todoapp.controller;

import com.pacifique.todoapp.dto.UserRequest;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/todos")
@Tag(name = "User controller", description = "All users endpoints")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Register a user")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "user created",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Long.class)
                    ),
                }
            ),
            @ApiResponse(
                responseCode = "500",
                description = "server error",
                content = @Content
            ),
        }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register/user")
    public Long registerUser(@RequestBody @Validated UserRequest request) {
        return userService.registerUser(request);
    }

    @Operation(summary = "all user list")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "user List",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserResponse.class)
                    ),
                }
            ),
            @ApiResponse(
                responseCode = "500",
                description = "server error",
                content = @Content
            ),
        }
    )
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.allUsers();
    }

    @Operation(summary = "get user by Id")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "user details",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserResponse.class)
                    ),
                }
            ),
        }
    )
    @GetMapping("/users/{id}")
    public UserResponse getUser(
        @Parameter(description = "user id to look up") @PathVariable Long id
    ) {
        return userService.getUser(id);
    }
}
