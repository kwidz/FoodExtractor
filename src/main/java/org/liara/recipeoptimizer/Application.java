package org.liara.recipeoptimizer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main (@NonNull final String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
    }
}
