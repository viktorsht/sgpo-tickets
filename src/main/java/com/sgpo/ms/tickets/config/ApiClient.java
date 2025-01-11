package com.sgpo.ms.tickets.config;

import com.sgpo.ms.tickets.dto.TravelRoutes;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import com.sgpo.ms.tickets.dto.UserDataResponse;

@Component
public class ApiClient {

    private final RestTemplate restTemplate;

    public ApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDataResponse getUserData(String token) {
        // URL do microserviço de usuários
        String userServiceUrl = "http://3.143.208.119:8081/users/me";

        // Chamada para o microserviço de usuários usando RestTemplate
        ResponseEntity<UserDataResponse> response = restTemplate.exchange(
                userServiceUrl,
                HttpMethod.GET,
                new HttpEntity<>(getHeaders(token)),
                UserDataResponse.class // Classe de destino para desserialização
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody(); // Retorna o objeto UserDataResponse
        } else {
            throw new RuntimeException("Failed to fetch user data: " + response.getStatusCode());
        }
    }

    public TravelRoutes getTravelRoutes(String token, String routeId) {
        String routeURL = "http://3.14.147.151:8082/routes/" + routeId;
        ResponseEntity<TravelRoutes> response = restTemplate.exchange(
                routeURL,
                HttpMethod.GET,
                new HttpEntity<>(getHeaders(token)),
                TravelRoutes.class // Classe de destino para desserialização
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody(); // Retorna o objeto UserDataResponse
        } else {
            throw new RuntimeException("Failed to fetch user data: " + response.getStatusCode());
        }
    }

    private HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Adicionando o prefixo "Bearer"
        return headers;
    }

}
