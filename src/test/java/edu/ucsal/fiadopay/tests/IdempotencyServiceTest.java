package edu.ucsal.fiadopay.tests;

import edu.ucsal.fiadopay.domain.idempotency.IdempotencyKeyEntity;
import edu.ucsal.fiadopay.domain.idempotency.IdempotencyRepository;
import edu.ucsal.fiadopay.service.idempotency.IdempotencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IdempotencyServiceTest {

    @Autowired
    private IdempotencyService idempotencyService;

    @Autowired
    private IdempotencyRepository repository;

    @Test
    void saveAndRetrieveResponse() {
        String key = "test-key-001";
        String body = "{\"ok\":true}";

        // initially none
        assertFalse(idempotencyService.exists(key));
        repository.save(new IdempotencyKeyEntity(key, "POST", body));

        assertTrue(idempotencyService.exists(key));
        var resp = idempotencyService.getResponse(key);
        assertTrue(resp.isPresent());
        assertEquals(body, resp.get());
    }
}
