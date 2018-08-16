package org.liara.recipeoptimizer.http;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.recipeoptimizer.data.Ingredient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/hello")
    public @NonNull String helloWorld () {
        return "hello buddy";
    }

    @GetMapping("/hello/{name}")
    public @NonNull String helloGuy (@PathVariable @NonNull final String name) {
        return "hello " + name;
    }

    @GetMapping("/ingredient")
    public @NonNull Ingredient helloGuy () {
        return new Ingredient("bullshit", "moncul", "pascher ltd.", 5000f);
    }
}
