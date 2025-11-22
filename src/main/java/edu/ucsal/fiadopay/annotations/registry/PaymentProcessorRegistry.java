package edu.ucsal.fiadopay.annotations.registry;

import edu.ucsal.fiadopay.annotations.PaymentProcessor;

import java.util.HashMap;
import java.util.Map;

public class PaymentProcessorRegistry {

    private static final Map<String, PaymentProcessor> processors = new HashMap<>();

    public static void register(String method, PaymentProcessor processor) {
        processors.put(method.toUpperCase(), processor);
    }

    public static PaymentProcessor get(String method) {
        return processors.get(method.toUpperCase());
    }

    public static Map<String, PaymentProcessor> all() {
        return processors;
    }
}
