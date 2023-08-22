package com.taskhero;

import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Hidden
public class MainController {

  @GetMapping
  @ResponseStatus(OK)
  public void home(HttpServletResponse httpServletResponse, HttpServletRequest request)
      throws IOException {
    httpServletResponse.sendRedirect(request.getServletPath() + "swagger-ui/index.html#/");
  }
}
