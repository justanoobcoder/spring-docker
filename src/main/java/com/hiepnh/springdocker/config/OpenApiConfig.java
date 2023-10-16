package com.hiepnh.springdocker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${app.dev.url}")
    private String devUrl;

    @Value("${app.prod.url}")
    private String prodUrl;

    @Bean
    public OpenAPI appOpenApi() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Development server");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Production server");

        Info info = getInfo();

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }

    private Info getInfo() {
        Contact contact = new Contact();
        contact.setName("Just A Noob Coder");
        contact.setEmail("abc@gmail.com");

        License license = new License();
        license.setName("Apache License Version 2.0");
        license.setUrl("https://www.apache.org/licenses/LICENSE-2.0");

        Info info = new Info();
        info.setTitle("Spring Boot Docker");
        info.setDescription("This API is for Spring Boot Docker");
        info.setVersion("1.0");
        info.setContact(contact);
        info.setLicense(license);
        return info;
    }
}
