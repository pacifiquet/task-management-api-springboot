package com.pacifique.todoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
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
        sequenceName = "todo_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "todo_sequence"
    )
    private Integer id;

    private String title;
    private String memo;
    private Long user_id;
    private LocalDate createdAt;
    private LocalDate dateCompleted;
    private Boolean important;
}
