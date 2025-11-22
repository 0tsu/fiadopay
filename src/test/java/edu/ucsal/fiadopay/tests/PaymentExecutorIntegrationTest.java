package edu.ucsal.fiadopay.tests;

import edu.ucsal.fiadopay.domain.Payment;
import edu.ucsal.fiadopay.repo.PaymentRepository;
import edu.ucsal.fiadopay.service.async.PaymentExecutorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PaymentExecutorIntegrationTest {

    @Autowired
    private PaymentExecutorService executor;

    @Autowired
    private PaymentRepository payments;

    @Test
    void executorProcessesPaymentAndUpdatesStatus() throws Exception {
        Payment p = Payment.builder()
                .id("pay_" + UUID.randomUUID().toString().substring(0,8))
                .merchantId(1L)
                .method("PIX")
                .amount(new BigDecimal("10.00"))
                .currency("BRL")
                .installments(1)
                .monthlyInterest(null)
                .totalWithInterest(new BigDecimal("10.00"))
                .status(Payment.Status.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .idempotencyKey(null)
                .build();

        payments.save(p);

        // run async pipeline
        executor.processPaymentAsync(p);

        // wait up to 5s for processing to update status (poll)
        boolean updated = false;
        for (int i=0; i<25; i++) {
            var refreshed = payments.findById(p.getId()).orElse(null);
            if (refreshed != null && refreshed.getStatus() != Payment.Status.PENDING) {
                updated = true;
                break;
            }
            TimeUnit.MILLISECONDS.sleep(200);
        }

        assertTrue(updated, "Payment was not processed in time");
        var finalP = payments.findById(p.getId()).orElseThrow();
        assertTrue(finalP.getStatus() == Payment.Status.APPROVED || finalP.getStatus() == Payment.Status.DECLINED);
    }
}
