package com.aalonso.carregistry.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
@Slf4j
public class Utils {
    @PostConstruct
    public void init() {
        log.info("Utils are operational...");
    }

    public <T> void ifEmptyOrElse(List<T> list, Runnable emptyAction, Consumer<? super List<T>> action) {
        if (list.isEmpty()) {
            emptyAction.run();
        } else {
            action.accept(list);
        }
    }
}
