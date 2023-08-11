package com.pacifique.todoapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "todos",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_title", columnNames = "title"),
    }
)
public class Todo {
    @Id
    @SequenceGenerator(
        name = "todo_id_generator_sequence",
        sequenceName = "todo_id_generator_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "todo_id_generator_sequence"
    )
    private Integer id;

    private String title;
    private String memo;

    @Column(name = "user_id")
    private Long userId;

    private LocalDateTime createdAt;
    private LocalDateTime dateCompleted;
    private Boolean important;
}
