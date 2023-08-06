package com.pacifique.todoapp.model;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
public class User {
    @Id
    @SequenceGenerator(
        name = "user_id_sequence",
        sequenceName = "user_id_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_id_sequence"
    )
    private Long id;

    private String email;
    private String fullName;
    private String role;
    private LocalDateTime createdAt;

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Todo> todos = new ArrayList<>();
}
