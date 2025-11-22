package edu.ucsal.fiadopay.webhook.handlers;

import edu.ucsal.fiadopay.annotations.WebhookSink;
import edu.ucsal.fiadopay.domain.Payment;

@WebhookSink(event = "Merchant Webhook Handler")
public class MerchantWebhookHandler {

    public void handle(Payment payment) {
        System.out.println("Enviando webhook para merchant: "
                + payment.getMerchantId()
                + " | status=" + payment.getStatus()
        );
    }
}
