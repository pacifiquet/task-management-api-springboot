package com.pacifique.todoapp.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponse implements Serializable {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private LocalDateTime createAt;
}
