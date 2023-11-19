package eu.echodream.medhead.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Value("${medhead.openapi.dev-url}")
    private String devUrl;

    @Value("${medhead.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("URL du serveur en environnement de développement MedHead");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("URL du serveur en environnement de production MedHead");

        Contact contact = new Contact();
        contact.setEmail("contact@medheadconsortium.org");
        contact.setName("MedHead Consortium");
        contact.setUrl("https://www.medheadconsortium.org");

        License license = new License().name("Licence spécifique").url("URL_de_la_licence");

        Info info = new Info()
                .title("API de Gestion des Interventions d'Urgence")
                .version("1.0")
                .contact(contact)
                .description("Cette API expose des endpoints pour gérer les interventions d'urgence.").termsOfService("URL_des_termes_de_service")
                .license(license);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}