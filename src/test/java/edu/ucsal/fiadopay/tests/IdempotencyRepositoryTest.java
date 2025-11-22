package edu.ucsal.fiadopay.tests;

import edu.ucsal.fiadopay.domain.idempotency.IdempotencyKeyEntity;
import edu.ucsal.fiadopay.domain.idempotency.IdempotencyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IdempotencyRepositoryTest {

    @Autowired
    private IdempotencyRepository repo;

    @Test
    void saveAndFind() {
        IdempotencyKeyEntity e = new IdempotencyKeyEntity("key-xyz", "POST", "{\"ok\":true}");
        repo.save(e);

        var found = repo.findById("key-xyz");
        assertTrue(found.isPresent());
        assertEquals("POST", found.get().getMethod());
        assertEquals("{\"ok\":true}", found.get().getResponseBody());
    }
}