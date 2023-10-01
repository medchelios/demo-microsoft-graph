package com.tmoh.demomicrosoftgraph.client;

import com.tmoh.demomicrosoftgraph.config.AzureProperties;
import com.microsoft.graph.requests.GraphServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
 class GraphClientTest {
    private final String AZURE_URL = "https://test.login.microsoftonline.com";
    private final String AZURE_SCOPE= "https://test.graph.microsoftonline.com";
    private final String AZURE_CLIENT_ID = "CLIENT ID";
    private final String AZURE_CLIENT_SECRET = "YOUR_CLIENT_SECRET";
    private  String AZURE_TENANT_ID = "YOUR_TENANT_ID";

    @Mock
    private AzureProperties azureProperties;
    @Mock
    private GraphServiceClient graphServiceClientMock;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGraphClientCreation() throws Exception {
        when(azureProperties.getClientId()).thenReturn(AZURE_CLIENT_ID);
        when(azureProperties.getTenantId()).thenReturn(AZURE_TENANT_ID);
        when(azureProperties.getClientSecret()).thenReturn(AZURE_CLIENT_SECRET);
        when(azureProperties.getScopes()).thenReturn(AZURE_SCOPE);
        when(azureProperties.getUrl()).thenReturn(AZURE_URL);

        GraphClient graphClient = new GraphClient(azureProperties);

        GraphServiceClient<?> client = graphClient.client();

        assertNotNull(client);

    }
}