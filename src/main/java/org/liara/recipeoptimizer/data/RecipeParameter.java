package org.liara.recipeoptimizer.data;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.MinLen;

public class RecipeParameter {
    @NonNegative
    private final int id;

    @NonNull
    private final String name, url;
    private final String type;

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='"+url+'\''+
                ", type='"+type+'\''+
                '}';
    }

    public @NonNegative int getId() {
        return id;
    }

    public RecipeParameter(
        @NonNegative final int id,
        @NonNull final String name,
        @NonNull final String url,
        @NonNull final String type
    ) {

        this.id = id;
        this.url = url;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public boolean equals(Object o ){
        if (o == this) {
            return true;
        }
        if (!(o instanceof RecipeParameter)) {
            return false;
        }
        return this.id==((RecipeParameter) o).getId();

    }

}
