package com.espe.drivex.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "DriveX API",
                version = "1.0.0",
                description = "API REST para la plataforma de alquiler de vehículos DriveX. " +
                        "Use el botón Authorize con sus credenciales para acceder a los endpoints protegidos.",
                contact = @Contact(
                        name = "Diego",
                        email = "diego@espe.edu.ec"
                )
        )
)
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class SwaggerConfig {
}
