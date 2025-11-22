package edu.ucsal.fiadopay.domain.idempotency;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKeyEntity {

    @Id
    @Column(name = "key_id", length = 255)
    private String keyId;

    @Column(name = "method", length = 16)
    private String method;

    @Lob
    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    public IdempotencyKeyEntity() {}

    public IdempotencyKeyEntity(String keyId, String method, String responseBody) {
        this.keyId = keyId;
        this.method = method;
        this.responseBody = responseBody;
        this.createdAt = Instant.now();
    }

    // getters and setters
    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
