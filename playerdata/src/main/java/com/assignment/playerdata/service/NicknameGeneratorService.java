package com.assignment.playerdata.service;


import com.assignment.playerdata.exception.NicknameGenerationException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class NicknameGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(NicknameGeneratorService.class);

    @Value("${ai.llm.api.url:http://localhost:11434/api/chat}")
    private String llmApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, String>> generateNicknamesFromCsv() {
        List<Map<String, String>> results = new ArrayList<>();
        try (InputStream input = new ClassPathResource("Player.csv").getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String playerName = parts[0].trim();
                    String country = parts[1].trim();
                    logger.info("Generating nickname for player '{}' from '{}'.", playerName, country);
                    Map<String, String> nicknameData = new HashMap<>(generateNickname(country));
                    nicknameData.put("player", playerName);
                    results.add(nicknameData);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading Player.csv: {}", e.getMessage(), e);
            throw new NicknameGenerationException("Failed to read Player.csv: " + e.getMessage());
        }
        return results;
    }

    public Map<String, String> generateNickname(String country) {
        String systemPrompt = "You are an AI that generates fun and culturally inspired player nicknames.";
        String userPrompt = "Generate a unique nickname for a sports player from " + country + ".";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "tinyllama");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));
        requestBody.put("stream", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            logger.debug("Sending prompt to LLM for country '{}'.", country);
            StringBuilder nickname = new StringBuilder();

            RequestCallback requestCallback = restTemplate.httpEntityCallback(request, Map.class);
            ResponseExtractor<Void> responseExtractor = response -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        JsonFactory factory = new JsonFactory();
                        try (JsonParser parser = factory.createParser(line)) {
                            while (!parser.isClosed()) {
                                JsonToken token = parser.nextToken();
                                if (JsonToken.FIELD_NAME.equals(token) && "content".equals(parser.getCurrentName())) {
                                    parser.nextToken();
                                    nickname.append(parser.getText());
                                }
                            }
                        }
                    }
                }
                return null;
            };

            restTemplate.execute(llmApiUrl, HttpMethod.POST, requestCallback, responseExtractor);
            String result = nickname.toString().trim();
            logger.info("Nickname for '{}': '{}'.", country, result);
            return Map.of("country", country, "nickname", result);

        } catch (Exception e) {
            logger.error("Failed to generate nickname for '{}': {}", country, e.getMessage(), e);
            throw new NicknameGenerationException("Failed to generate nickname for: " + country);
        }
    }
}