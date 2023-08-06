package com.pacifique.todoapp.exceptions;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ApiError {
    private String path;
    private String message;
    private int statusCode;
    private LocalDateTime localDateTime;
}
