package com.tmoh.demomicrosoftgraph.client;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import com.tmoh.demomicrosoftgraph.config.AzureProperties;
import com.tmoh.demomicrosoftgraph.exception.GraphClientException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class GraphClient {

    private final AzureProperties azureProperties;

    public GraphClient(AzureProperties azureProperties) {
        this.azureProperties = azureProperties;
    }


    @SneakyThrows
    public GraphServiceClient<Request> client() {
        try {
            final List<String> scopes = Collections.singletonList(azureProperties.getScopes());
            ClientSecretCredential credential = buildCredential();

            TokenCredentialAuthProvider authProvider =
                    new TokenCredentialAuthProvider(scopes, credential);

            return buildGraphServiceClient(authProvider);

        } catch (Exception ex) {
            log.error("Error while creating GraphServiceClient: {}", ex.getMessage());
            throw new GraphClientException("Error creating GraphServiceClient ", ex);
        }
    }

    private ClientSecretCredential buildCredential() {
        return new ClientSecretCredentialBuilder()
                .clientId(azureProperties.getClientId())
                .tenantId(azureProperties.getTenantId())
                .clientSecret(azureProperties.getClientSecret())
                .authorityHost(azureProperties.getUrl())
                .build();
    }

    private GraphServiceClient<Request> buildGraphServiceClient(
            TokenCredentialAuthProvider authProvider) {
        return GraphServiceClient.builder()
                .authenticationProvider(authProvider)
                .buildClient();
    }
}
