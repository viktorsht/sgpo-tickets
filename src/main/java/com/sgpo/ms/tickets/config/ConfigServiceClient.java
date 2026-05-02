package com.sgpo.ms.tickets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConfigServiceClient {

    private final RestTemplate restTemplate;
    private final String configServiceUrl;

    public ConfigServiceClient(RestTemplate restTemplate,
                               @Value("${config-service.url}") String configServiceUrl) {
        this.restTemplate = restTemplate;
        this.configServiceUrl = configServiceUrl;
    }

    public String getServiceUrl(String serviceName) {
        String endpoint = configServiceUrl + "/config/" + serviceName;
        try {
            var response = restTemplate.getForObject(endpoint, java.util.Map.class);
            if (response != null && response.containsKey("url")) {
                return (String) response.get("url");
            }
            throw new RuntimeException("URL não encontrada para o serviço: " + serviceName);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar ConfigService para '" + serviceName + "': " + e.getMessage());
        }
    }
}