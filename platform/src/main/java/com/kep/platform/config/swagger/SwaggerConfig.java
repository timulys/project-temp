package com.kep.platform.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * springdoc swagger 설정
 *
 * @author volka.yun
 */
@Profile("dev")
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().addServersItem(createSwaggerServer())
                .components(createSwaggerComponents())
                .info(new Info().title("Platform API").version("1"));
    }

    private Server createSwaggerServer() {
        return new Server()
                .url("https://always-talk.kakaoiconnect.ai/platform");
    }

    private Components createSwaggerComponents() {
        return new Components()
                .addSecuritySchemes(
                        "basicAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JSESSIONID")
                                .scheme("basic")
                )
                ;
    }
}
