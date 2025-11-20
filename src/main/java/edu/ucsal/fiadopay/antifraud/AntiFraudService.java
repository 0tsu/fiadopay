package edu.ucsal.fiadopay.antifraud;

import edu.ucsal.fiadopay.annotations.registry.AntiFraudRegistry;
import edu.ucsal.fiadopay.domain.Payment;
import edu.ucsal.fiadopay.annotations.AntiFraud;
import edu.ucsal.fiadopay.antifraud.rules.*;

import java.util.List;

public class AntiFraudService {

    public boolean evaluate(Payment payment) {
        List<AntiFraud> rules = AntiFraudRegistry.all();

        for (Object rule : rules) {
            AntiFraud annotation = rule.getClass().getAnnotation(AntiFraud.class);
            if (annotation == null) continue;

            try {
                boolean result = (boolean) rule.getClass()
                        .getMethod("check", Payment.class)
                        .invoke(rule, payment);

                if (!result) return false;
            } catch (Exception e) {
                throw new RuntimeException("Erro ao executar regra antifraude: " + rule.getClass().getSimpleName(), e);
            }
        }

        return true;
    }
}
