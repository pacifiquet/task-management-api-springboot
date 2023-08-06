package com.pacifique.todoapp.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private LocalDate createAt;
}
