package edu.ucsal.fiadopay.filters;

import edu.ucsal.fiadopay.service.idempotency.IdempotencyService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    private final IdempotencyService idempotencyService;

    public IdempotencyFilter(IdempotencyService idempotencyService) {
        this.idempotencyService = idempotencyService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // filtra apenas criação de pagamento; adapte a rota se necessário
        return !("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().contains("/payments"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String key = request.getHeader("Idempotency-Key");
        if (key != null && idempotencyService.exists(key)) {
            Optional<String> cached = idempotencyService.getResponse(key);
            if (cached.isPresent()) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(cached.get());
                return;
            }
        }

        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, wrappedResponse);

        // ao finalizar, se header presente, salvar resposta
        if (key != null) {
            byte[] content = wrappedResponse.getContentAsByteArray();
            String responseBody = content.length > 0 ? new String(content, wrappedResponse.getCharacterEncoding() != null ? wrappedResponse.getCharacterEncoding() : StandardCharsets.UTF_8.name()) : "";
            idempotencyService.saveResponse(key, request.getMethod(), responseBody);
        }

        wrappedResponse.copyBodyToResponse();
    }
}
