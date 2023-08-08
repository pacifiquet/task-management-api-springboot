package com.pacifique.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("full_name")
    private String fullName;

    private String role;

    @JsonProperty("create_at")
    private LocalDateTime createAt;
}
