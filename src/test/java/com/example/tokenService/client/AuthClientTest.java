package com.example.tokenService.client;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

public class AuthClientTest {

    /**
     * Flujo exitoso: El RestTemplate retorna un JSON válido que contiene el token.
     */
    @Test
    public void testGetToken_Success() throws Exception {
        // Instanciar la clase y setear la URL de forma manual
        AuthClient authClient = new AuthClient();
        ReflectionTestUtils.setField(authClient, "authServiceUrl", "http://dummy-url");

        // Mockear la construcción del RestTemplate
        try (MockedConstruction<RestTemplate> mocked = Mockito.mockConstruction(RestTemplate.class,
            (mock, context) -> {
                Mockito.when(mock.postForObject(anyString(), any(), eq(String.class)))
                        .thenReturn("{\"token\":\"abc123\"}");
            })) {

            String token = authClient.getToken();
            assertEquals("abc123", token);
        }
    }

    /**
     * Flujo: La respuesta es null, se espera lanzar Exception indicando respuesta vacía.
     */
    @Test
    public void testGetToken_EmptyResponse() {
        AuthClient authClient = new AuthClient();
        ReflectionTestUtils.setField(authClient, "authServiceUrl", "http://dummy-url");

        try (MockedConstruction<RestTemplate> mocked = Mockito.mockConstruction(RestTemplate.class,
            (mock, context) -> {
                Mockito.when(mock.postForObject(anyString(), any(), eq(String.class)))
                        .thenReturn(null);
            })) {

            Exception exception = assertThrows(Exception.class, () -> authClient.getToken());
            assertTrue(exception.getMessage().contains("Empty response from authentication server"));
        }
    }

    /**
     * Flujo: La respuesta no es un JSON válido, lo que provoca un JSONException.
     */
    @Test
    public void testGetToken_InvalidJSON() {
        AuthClient authClient = new AuthClient();
        ReflectionTestUtils.setField(authClient, "authServiceUrl", "http://dummy-url");

        try (MockedConstruction<RestTemplate> mocked = Mockito.mockConstruction(RestTemplate.class,
            (mock, context) -> {
                Mockito.when(mock.postForObject(anyString(), any(), eq(String.class)))
                        .thenReturn("invalid json");
            })) {

            Exception exception = assertThrows(Exception.class, () -> authClient.getToken());
            assertTrue(exception.getMessage().contains("Error parsing authentication response"));
        }
    }

    /**
     * Flujo: Se lanza una RestClientException al llamar al servicio externo.
     */
    @Test
    public void testGetToken_RestClientException() {
        AuthClient authClient = new AuthClient();
        ReflectionTestUtils.setField(authClient, "authServiceUrl", "http://dummy-url");

        try (MockedConstruction<RestTemplate> mocked = Mockito.mockConstruction(RestTemplate.class,
            (mock, context) -> {
                Mockito.when(mock.postForObject(anyString(), any(), eq(String.class)))
                        .thenThrow(new RestClientException("Service unavailable"));
            })) {

            Exception exception = assertThrows(Exception.class, () -> authClient.getToken());
            assertTrue(exception.getMessage().contains("Error while calling authentication server"));
            assertNotNull(exception.getCause());
            assertTrue(exception.getCause() instanceof RestClientException);
        }
    }
}
