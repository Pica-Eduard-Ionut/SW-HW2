package com.example.homework.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class GoogleAiClient {

    @Value("${google.ai.api.key}")
    private String apiKey;

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private String chatUrl = null;

    @PostConstruct
    public void discoverModel() {
        for (String version : List.of("v1beta", "v1")) {
            try {
                String listUrl = "https://generativelanguage.googleapis.com/" + version + "/models?key=" + apiKey;
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(listUrl)).GET().build();
                HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
                JsonNode root = mapper.readTree(res.body());

                if (root.has("error")) {
                    System.out.println("[GoogleAiClient] " + version + " error: " + root.path("error").path("message").asText());
                    continue;
                }

                for (JsonNode model : root.path("models")) {
                    for (JsonNode method : model.path("supportedGenerationMethods")) {
                        if ("generateContent".equals(method.asText())) {
                            String name = model.path("name").asText(); // "models/gemini-..."
                            chatUrl = "https://generativelanguage.googleapis.com/" + version + "/" + name + ":generateContent";
                            System.out.println("[GoogleAiClient] Using model: " + chatUrl);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("[GoogleAiClient] Failed to query " + version + ": " + e.getMessage());
            }
        }
        System.out.println("[GoogleAiClient] WARNING: no model found, chat will fail.");
    }

    public String chat(String systemPrompt, String userMessage) {
        if (chatUrl == null) return "No AI model available. Check API key.";
        try {
            String fullMessage = systemPrompt + "\n\nUser question: " + userMessage;

            String body = mapper.writeValueAsString(Map.of(
                "contents", List.of(Map.of(
                    "role", "user",
                    "parts", List.of(Map.of("text", fullMessage))
                ))
            ));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(chatUrl + "?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());

            if (root.has("error")) {
                return "API error: " + root.path("error").path("message").asText("unknown");
            }

            JsonNode candidates = root.path("candidates");
            if (candidates.isEmpty()) {
                String reason = root.path("promptFeedback").path("blockReason").asText("");
                return reason.isEmpty() ? "No response generated." : "Blocked: " + reason;
            }

            return candidates.get(0)
                .path("content").path("parts").get(0)
                .path("text").asText("No text in response.");

        } catch (Exception e) {
            throw new RuntimeException("Chat request failed: " + e.getMessage(), e);
        }
    }
}
