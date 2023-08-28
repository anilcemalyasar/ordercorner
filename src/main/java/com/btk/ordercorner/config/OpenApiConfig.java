package com.btk.ordercorner.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name="Btk Akademi - Innova"
        ),
        description = "Anıl Cemal Yaşar - Hüseyin Safa Tosun - Eyüphan Oğuz - Samet Kaan Keskin - Yusuf Talha Kaya",
        title = "Btk Akademi - Innova Final Proje"
    ),
    servers = {
        @Server(
            description = "Local ENV",
            url = "http://localhost:8080"
        ),

    }
)

public class OpenApiConfig {
    
}
