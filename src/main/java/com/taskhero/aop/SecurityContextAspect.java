package com.taskhero.aop;

import com.taskhero.project.models.Project;
import com.taskhero.project.repository.ProjectRepository;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.service.authorservice.UserDetailsImpl;
import java.util.List;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class SecurityContextAspect {
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;

  @Before(
      "execution(* (com.taskhero.project.service..* || com.taskhero.task.service..*).*(..)) && args(projectId, ..)")
  public void beforeProjectRelatedMethod(JoinPoint joinPoint, Long projectId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl authUser = (UserDetailsImpl) authentication.getPrincipal();
    User user = userRepository.findById(authUser.getId()).orElseThrow();
    List<Project> owner = projectRepository.findByOwner(user);

    if (owner.isEmpty()) {
      throw new SecurityException("you do not have access to this project");
    }
  }
}
