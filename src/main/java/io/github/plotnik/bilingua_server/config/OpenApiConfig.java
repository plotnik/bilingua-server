package io.github.plotnik.bilingua_server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bilinguaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bilingua Server API")
                        .description("REST API for managing bilingual text paragraphs and navigation")
                        .version("v0.0.1")
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}
