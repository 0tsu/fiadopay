package edu.ucsal.fiadopay.webhook;

import edu.ucsal.fiadopay.annotations.WebhookSink;
import edu.ucsal.fiadopay.annotations.registry.WebhookRegistry;
import edu.ucsal.fiadopay.domain.Payment;

import java.lang.reflect.Method;
import java.util.List;

public class WebhookExecutorService {

    public void send(Payment payment) {
        List<WebhookSink> handlers = WebhookRegistry.all();

        for (Object handler : handlers) {
            try {
                Method m = handler.getClass().getMethod("handle", Payment.class);
                m.invoke(handler, payment);
            } catch (Exception e) {
                System.out.println("[Webhook ERROR] " + e.getMessage());
            }
        }
    }
}
