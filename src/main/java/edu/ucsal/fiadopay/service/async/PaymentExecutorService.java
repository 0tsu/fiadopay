package edu.ucsal.fiadopay.service.async;

import edu.ucsal.fiadopay.annotations.registry.PaymentProcessorRegistry;
import edu.ucsal.fiadopay.antifraud.AntiFraudService;
import edu.ucsal.fiadopay.domain.Payment;
import edu.ucsal.fiadopay.repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;

@Service
public class PaymentExecutorService {

    @Autowired
    private ExecutorService paymentExecutor;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AntiFraudService antiFraudService;

    public void processPaymentAsync(Payment payment) {
        paymentExecutor.submit(() -> executePipeline(payment));
    }

    private void executePipeline(Payment payment) {

        // 1. Antifraude
        boolean ok = antiFraudService.evaluate(payment);
        if (!ok) {
            payment.setStatus(Payment.Status.DECLINED);
            paymentRepository.save(payment);
            return;
        }

        // 2. Calcular juros (1% por parcela)
        payment.setTotalWithInterest(
                calculateInterest(payment.getAmount(), payment.getInstallments())
        );

        // 3. Selecionar processador via registry
        var processor = PaymentProcessorRegistry.get(payment.getMethod());

        if (processor == null) {
            payment.setStatus(Payment.Status.DECLINED);
            paymentRepository.save(payment);
            return;
        }

        // 4. Executar processador
        try {
            processor.process(payment);
        } catch (Exception e) {
            payment.setStatus(Payment.Status.DECLINED);
        }

        paymentRepository.save(payment);
    }

    private BigDecimal calculateInterest(BigDecimal amount, int installments) {
        BigDecimal percent = new BigDecimal("0.01");
        BigDecimal interest = percent.multiply(BigDecimal.valueOf(installments));

        return amount
                .add(amount.multiply(interest))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
