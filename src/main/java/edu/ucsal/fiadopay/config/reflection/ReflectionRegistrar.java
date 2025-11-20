package edu.ucsal.fiadopay.config.reflection;

import edu.ucsal.fiadopay.annotations.*;
import org.reflections.Reflections;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ReflectionRegistrar {

    // Registries acessados por outros módulos
    public static final Map<String, PaymentProcessor> PAYMENT_PROCESSORS = new HashMap<>();
    public static final List<FraudRule> FRAUD_RULES = new ArrayList<>();
    public static final Map<String, WebhookHandler> WEBHOOK_HANDLERS = new HashMap<>();
    public static final List<ScheduledJobRunnable> JOBS = new ArrayList<>();


    public ReflectionRegistrar() {
        Reflections reflections = new Reflections("edu.ucsal.fiadopay");

        registerPaymentProcessors(reflections);
        registerFraudRules(reflections);
        registerWebhookHandlers(reflections);
        registerScheduledJobs(reflections);

        System.out.println("🔍 [ReflectionRegistrar] Plugins carregados com sucesso.");
        System.out.println("🟦 Payment processors: " + PAYMENT_PROCESSORS.keySet());
        System.out.println("🟪 Antifraude rules: " + FRAUD_RULES.size());
        System.out.println("🟧 Webhook handlers: " + WEBHOOK_HANDLERS.keySet());
        System.out.println("🟩 Scheduled jobs: " + JOBS.size());
    }


    // ------------------ 1) Payment Processors --------------------
    private void registerPaymentProcessors(Reflections reflections) {
        var classes = reflections.getTypesAnnotatedWith(PaymentMethod.class);

        for (Class<?> clazz : classes) {
            try {
                PaymentMethod annotation = clazz.getAnnotation(PaymentMethod.class);
                PaymentProcessor instance = (PaymentProcessor) clazz.getDeclaredConstructor().newInstance();
                PAYMENT_PROCESSORS.put(annotation.value(), instance);
            } catch (Exception e) {
                System.err.println("Erro registrando PaymentProcessor: " + clazz.getName());
            }
        }
    }


    // ------------------ 2) Fraud Rules --------------------
    private void registerFraudRules(Reflections reflections) {
        var classes = reflections.getTypesAnnotatedWith(AntiFraud.class);

        for (Class<?> clazz : classes) {
            try {
                FraudRule instance = (FraudRule) clazz.getDeclaredConstructor().newInstance();
                FRAUD_RULES.add(instance);
            } catch (Exception e) {
                System.err.println("Erro registrando FraudRule: " + clazz.getName());
            }
        }
    }


    // ------------------ 3) Webhook Handlers --------------------
    private void registerWebhookHandlers(Reflections reflections) {
        var classes = reflections.getTypesAnnotatedWith(WebhookSink.class);

        for (Class<?> clazz : classes) {
            try {
                WebhookSink annotation = clazz.getAnnotation(WebhookSink.class);
                WebhookHandler instance = (WebhookHandler) clazz.getDeclaredConstructor().newInstance();
                WEBHOOK_HANDLERS.put(annotation.event(), instance);
            } catch (Exception e) {
                System.err.println("Erro registrando WebhookHandler: " + clazz.getName());
            }
        }
    }


    // ------------------ 4) Scheduled Jobs --------------------
    private void registerScheduledJobs(Reflections reflections) {
        var classes = reflections.getTypesAnnotatedWith(ScheduledJob.class);

        for (Class<?> clazz : classes) {
            try {
                ScheduledJobRunnable instance = (ScheduledJobRunnable) clazz.getDeclaredConstructor().newInstance();
                JOBS.add(instance);
            } catch (Exception e) {
                System.err.println("Erro registrando ScheduledJob: " + clazz.getName());
            }
        }
    }
}
