package edu.ucsal.fiadopay.tests;

import edu.ucsal.fiadopay.domain.Payment;
import edu.ucsal.fiadopay.processors.PixProcessor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PixProcessorTest {

    @Test
    void pixProcessorShouldApprove() {
        PixProcessor proc = new PixProcessor();

        Payment p = Payment.builder()
                .id("pay_" + UUID.randomUUID().toString().substring(0,8))
                .merchantId(1L)
                .method("PIX")
                .amount(new BigDecimal("50.00"))
                .currency("BRL")
                .installments(1)
                .monthlyInterest(null)
                .totalWithInterest(new BigDecimal("50.00"))
                .status(Payment.Status.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        proc.process(p);

        assertEquals(Payment.Status.APPROVED, p.getStatus());
    }
}
