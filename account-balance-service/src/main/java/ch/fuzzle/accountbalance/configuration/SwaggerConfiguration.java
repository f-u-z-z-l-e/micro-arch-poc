package ch.fuzzle.accountbalance.configuration;

import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${spring.application.name:'api documentation'}")
    private String apiTitle;

    @Value("${spring.application.description:no application description provided.}")
    private String apiDescription;

    @Value("${spring.application.api.version: no api version provided.}")
    private String apiVersion;

    @Value("${spring.application.api.tos-url: no terms of service url provided.}")
    private String apiTos;

    @Value("${spring.application.api.contact.name: no api contact name provided.}")
    private String apiContactName;

    @Value("${spring.application.api.contact.url: no api contact url provided.}")
    private String apiContactUrl;

    @Value("${spring.application.api.contact.email: no api contact email provided.}")
    private String apiContactEmail;

    @Value("${spring.application.api.contact.license: no api contact license provided.}")
    private String apiContactLicense;

    @Value("${spring.application.api.contact.license-url: no api contact license url provided.}")
    private String apiContactLicenseUrl;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("ch.fuzzle"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .useDefaultResponseMessages(false) // Don't use default response messages
            .globalResponseMessage(RequestMethod.GET, new ArrayList<>()) // no predefined response messages for GET
            .globalResponseMessage(RequestMethod.PATCH, new ArrayList<>()) // no predefined response messages for PATCH
            .globalResponseMessage(RequestMethod.POST, new ArrayList<>()) // no predefined response messages for POST
            .globalResponseMessage(RequestMethod.PUT, new ArrayList<>()); // no predefined response messages for PUT
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
            apiTitle,
            apiDescription,
            apiVersion,
            apiTos,
            new Contact(apiContactName, apiContactUrl, apiContactEmail), apiContactLicense, apiContactLicenseUrl, Collections.emptyList());
    }
}
