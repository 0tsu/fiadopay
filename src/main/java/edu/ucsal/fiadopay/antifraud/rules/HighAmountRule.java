package edu.ucsal.fiadopay.antifraud.rules;

import edu.ucsal.fiadopay.annotations.AntiFraud;
import edu.ucsal.fiadopay.annotations.FraudRule;
import edu.ucsal.fiadopay.domain.Payment;

@AntiFraud(value = "High Amount Rule")
public class HighAmountRule implements FraudRule{

    @Override
    public boolean check(Payment payment) {
        return payment.getAmount().doubleValue() <= 5000.00;
    }
}
