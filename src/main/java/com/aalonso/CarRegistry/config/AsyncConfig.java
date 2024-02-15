package com.aalonso.CarRegistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Implementación de Executor utiliza un pool de hilos para ejecutar tareas.
     * En lugar de crear un nuevo hilo para cada tarea, reutiliza los hilos existentes del pool.
     * Esto proporciona un mejor rendimiento ya que no se crean y destruyen hilos continuamente.
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("CarRegistryThread-");
        executor.initialize();
        return executor;
    }
}
