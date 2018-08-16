package org.liara.recipeoptimizer.data;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

public class RecipeParameter {
    @NonNegative
    private final int id;

    @NonNull
    private final String name;

    @Override
    public String toString() {
        return "Recipes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public @NonNegative int getId() {
        return id;
    }

    public RecipeParameter(@NonNegative final int id, @NonNull final String name) {

        this.id = id;
        this.name = name;
    }
}
