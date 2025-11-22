package edu.ucsal.fiadopay.jobs;

import edu.ucsal.fiadopay.annotations.ScheduledJob;
import edu.ucsal.fiadopay.service.idempotency.IdempotencyService;
import org.springframework.stereotype.Component;

@ScheduledJob(intervalSeconds = 3600) // uma vez por hora
@Component
public class IdempotencyCleanupJob implements Runnable {

    private final IdempotencyService idempotencyService;

    public IdempotencyCleanupJob(IdempotencyService idempotencyService) {
        this.idempotencyService = idempotencyService;
    }

    @Override
    public void run() {
        // limpa chaves com mais de 24h
        idempotencyService.cleanupOlderThanSeconds(24 * 3600);
        System.out.println("[IdempotencyCleanupJob] Limpeza executada.");
    }
}
