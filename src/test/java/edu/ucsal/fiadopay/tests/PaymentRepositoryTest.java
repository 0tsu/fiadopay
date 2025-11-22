package edu.ucsal.fiadopay.tests;

import edu.ucsal.fiadopay.domain.Payment;
import edu.ucsal.fiadopay.repo.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository payments;

    @Test
    void saveAndLookupByIdempotencyAndMerchant() {
        Payment p = Payment.builder()
                .id("pay_" + UUID.randomUUID().toString().substring(0,8))
                .merchantId(1L)
                .method("PIX")
                .amount(new BigDecimal("100.00"))
                .currency("BRL")
                .installments(1)
                .monthlyInterest(null)
                .totalWithInterest(new BigDecimal("100.00"))
                .status(Payment.Status.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .idempotencyKey("idem-123")
                .build();

        payments.save(p);

        var opt = payments.findByIdempotencyKeyAndMerchantId("idem-123", 1L);
        assertTrue(opt.isPresent());
        assertEquals(p.getId(), opt.get().getId());
    }
}
