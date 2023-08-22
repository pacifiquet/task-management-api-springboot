package com.taskhero.user.models;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.taskhero.user.security.UserPermission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "permissions")
public class Permission implements Serializable {
  @Serial private static final long serialVersionUID = 1905122041950251207L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private UserPermission userPermissions;
}
