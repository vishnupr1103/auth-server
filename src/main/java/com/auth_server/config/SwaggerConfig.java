package com.auth_server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openApiSpec() {
        return new OpenAPI()
                // Add global security requirement (applies Bearer token to all endpoints)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        // Register the Bearer token security scheme
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter your JWT token. Example: eyJhbGciOiJIUzI1NiJ9..."))
                        .addSchemas("ApiErrorResponse", new ObjectSchema()
                                .addProperty("status", new IntegerSchema())
                                .addProperty("code", new StringSchema())
                                .addProperty("message", new StringSchema())
                                .addProperty("fieldErrors", new ArraySchema().items(
                                        new Schema<ArraySchema>().$ref("ApiFieldError"))))
                        .addSchemas("ApiFieldError", new ObjectSchema()
                                .addProperty("code", new StringSchema())
                                .addProperty("message", new StringSchema())
                                .addProperty("property", new StringSchema())
                                .addProperty("rejectedValue", new ObjectSchema())
                                .addProperty("path", new StringSchema())));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        // add error type to each operation
        return (operation, handlerMethod) -> {
            operation.getResponses().addApiResponse("4xx/5xx", new ApiResponse()
                    .description("Error")
                    .content(new Content().addMediaType("*/*", new MediaType().schema(
                            new Schema<MediaType>().$ref("ApiErrorResponse")))));
            return operation;
        };
    }



}
