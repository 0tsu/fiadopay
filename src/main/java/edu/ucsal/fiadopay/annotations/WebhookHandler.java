package edu.ucsal.fiadopay.annotations;

import com.fasterxml.jackson.databind.JsonNode;

public interface WebhookHandler {
    void handle(JsonNode eventData);
}
