package edu.ucsal.fiadopay.config.executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean(name = "paymentExecutor")
    public ExecutorService paymentExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}
