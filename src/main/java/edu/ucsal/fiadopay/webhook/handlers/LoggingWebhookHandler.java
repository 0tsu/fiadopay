package edu.ucsal.fiadopay.webhook.handlers;

import edu.ucsal.fiadopay.annotations.WebhookSink;
import edu.ucsal.fiadopay.domain.Payment;

@WebhookSink(event = "Logging Webhook Handler")
public class LoggingWebhookHandler {

    public void handle(Payment payment) {
        System.out.println("[WEBHOOK LOG] Payment " + payment.getId() +
                " updated: " + payment.getStatus());
    }
}
