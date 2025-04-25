package com.example.summarize.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import com.google.gson.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class GoogleAPI {

    @Value("${google.api.key}")
    private String API_KEY;

    private String buildGeminiUrl() {
        return "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
    }

    private final RestTemplate restTemplate;
    private final Gson gson;

    public GoogleAPI() {
        this.restTemplate = new RestTemplate();
        this.gson = new Gson();
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public String frageGemini(String frage) {
        try {
            // JSON-Body zusammenbauen
            JsonObject textObj = new JsonObject();
            textObj.addProperty("text", frage);

            JsonArray partsArray = new JsonArray();
            partsArray.add(textObj);

            JsonObject contentObj = new JsonObject();
            contentObj.add("parts", partsArray);

            JsonArray contentsArray = new JsonArray();
            contentsArray.add(contentObj);

            JsonObject requestBody = new JsonObject();
            requestBody.add("contents", contentsArray);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(gson.toJson(requestBody), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    buildGeminiUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonObject jsonResponse = gson.fromJson(response.getBody(), JsonObject.class);
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");

                if (candidates != null && candidates.size() > 0) {
                    JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                    JsonObject content = firstCandidate.getAsJsonObject("content");
                    JsonArray parts = content.getAsJsonArray("parts");

                    if (parts != null && parts.size() > 0) {
                        return parts.get(0).getAsJsonObject().get("text").getAsString();
                    }
                }

                return "Keine Antwort erhalten.";
            } else {
                return "Fehler bei der Anfrage: " + response.getStatusCode();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler bei der Anfrage.";
        }
    }

}
