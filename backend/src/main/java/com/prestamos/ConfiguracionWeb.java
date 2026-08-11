package com.prestamos;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Permite que el frontend Angular (http://localhost:4200) llame a esta API,
 * que vive en otro puerto (8080). Sin esto el navegador bloquea las
 * peticiones por la politica de mismo origen.
 *
 * El origen esta restringido a localhost:4200 a proposito, en lugar de
 * abrirlo a cualquiera con "*".
 */
@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE");
    }
}
