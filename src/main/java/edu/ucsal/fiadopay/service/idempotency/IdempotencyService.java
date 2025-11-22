package edu.ucsal.fiadopay.service.idempotency;

import edu.ucsal.fiadopay.domain.idempotency.IdempotencyKeyEntity;
import edu.ucsal.fiadopay.domain.idempotency.IdempotencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class IdempotencyService {

    private final IdempotencyRepository repository;

    public IdempotencyService(IdempotencyRepository repository) {
        this.repository = repository;
    }

    public boolean exists(String key) {
        if (key == null) return false;
        return repository.existsById(key);
    }

    public Optional<String> getResponse(String key) {
        if (key == null) return Optional.empty();
        return repository.findById(key).map(IdempotencyKeyEntity::getResponseBody);
    }

    @Transactional
    public void saveResponse(String key, String method, String responseBody) {
        if (key == null) return;
        IdempotencyKeyEntity e = new IdempotencyKeyEntity(key, method, responseBody);
        e.setCreatedAt(Instant.now());
        repository.save(e);
    }

    public void cleanupOlderThanSeconds(long seconds) {
        Instant cutoff = Instant.now().minusSeconds(seconds);
        repository.findAll().stream()
                .filter(e -> e.getCreatedAt().isBefore(cutoff))
                .map(IdempotencyKeyEntity::getKeyId)
                .forEach(repository::deleteById);
    }
}
