package khantorecrm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String schemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(schemeName,
                                        new SecurityScheme()
                                                .name(schemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(apiInfo());
    }

    private Info apiInfo() {
        Info info = new Info();
        String appName = "Khantore-crm";
        info.title(appName);
        String version = "0.1";
        info.version(version);
        info.description("This is Api documentation for developer or any person who interested my api.");
        final Contact contact = new Contact();
        contact.name("Sardorbek Matniyazov");
        contact.email("sardorbekmatniyazov03@gmail.com");
        info.contact(contact);
        return info;
    }
}