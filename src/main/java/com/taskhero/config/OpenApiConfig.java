package com.taskhero.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info =
        @Info(
            contact =
                @Contact(
                    name = "Pacifique Twagirayesu",
                    email = "pacifiquetwagirayesu@gmail.com",
                    url = "https://github.com/pacifiquet"),
            description = "OpenApi documentation for TaskHero Personal Project",
            title = "API documentation - TaskHero Spring Boot Personal Project",
            version = "1.0",
            license = @License(name = "Licence name", url = "https://myapp.com"),
            termsOfService = "Terms of service"),
    servers = {
      @Server(description = "Local", url = "http://157.245.198.239:8080"),
    },
    security = {@SecurityRequirement(name = "bearerAuth")})
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {}
