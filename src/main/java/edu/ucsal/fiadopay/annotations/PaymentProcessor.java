package edu.ucsal.fiadopay.annotations;

import edu.ucsal.fiadopay.domain.Payment;

public interface PaymentProcessor {
    void process(Payment payment);
}
