package com.tmoh.demomicrosoftgraph.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "microsoft.azure")
public class AzureProperties {
    private String url;
    private String scopes;
    private String tenantId;
    private String clientId;
    private String clientSecret;
}
