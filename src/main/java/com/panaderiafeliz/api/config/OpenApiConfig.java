package com.panaderiafeliz.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Panadería Feliz API")
                        .description("API REST para gestionar panes (CRUD) con validaciones y manejo de errores.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Ivan Montesino")
                                .email("imontesinor@gmail.com")
                                .url("https://github.com/imontesinor"))
                );
    }
}
