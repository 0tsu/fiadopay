package edu.ucsal.fiadopay.processors;

import edu.ucsal.fiadopay.annotations.PaymentMethod;
import edu.ucsal.fiadopay.annotations.PaymentProcessor;
import edu.ucsal.fiadopay.domain.Payment;

@PaymentMethod("CARD")
public class CardProcessor implements PaymentProcessor {

    @Override
    public void process(Payment payment) {
        System.out.println("Processando pagamento no Cartão...");
        payment.setStatus(Payment.Status.PENDING);
    }
}
