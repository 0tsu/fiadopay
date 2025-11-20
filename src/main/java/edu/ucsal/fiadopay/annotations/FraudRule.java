package edu.ucsal.fiadopay.annotations;

import edu.ucsal.fiadopay.domain.Payment;

public interface FraudRule {
    boolean check(Payment payment);
}
