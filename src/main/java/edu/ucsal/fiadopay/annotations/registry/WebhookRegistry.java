package edu.ucsal.fiadopay.annotations.registry;

import edu.ucsal.fiadopay.annotations.WebhookSink;
import java.util.ArrayList;
import java.util.List;

public class WebhookRegistry {

    private static final List<WebhookSink> sinks = new ArrayList<>();

    public static void register(WebhookSink sink) {
        sinks.add(sink);
    }

    public static List<WebhookSink> all() {
        return sinks;
    }
}
