package org.liara.recipeoptimizer.data;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

public class RecipeParameter {
    @NonNegative
    private final int id;

    @NonNull
    private final String name, url;

    @Override
    public String toString() {
        return "Recipes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='"+url+'\''+
                '}';
    }

    public @NonNegative int getId() {
        return id;
    }

    public RecipeParameter(@NonNegative final int id, @NonNull final String name, @NonNull final String url) {

        this.id = id;
        this.url = url;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
