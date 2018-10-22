package org.liara.recipeoptimizer.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.recipeoptimizer.data.RecipeParameter;

import java.util.*;

public class RequestWrapper {
    private final @NonNull List<@NonNull RecipeParameter> forbiddenRecipes;
    private final @NonNull List<@NonNull String> freeComponents;
    private @NonNull int[] balance= new int[]{1, 1, 1, 1, 1};

    public RequestWrapper(
            @JsonProperty("recipes") final Collection<RecipeParameter> forbiddenRecipes,
            @JsonProperty("types") final Collection<String> freeComponents,
            @JsonProperty("balance") int[] balance) {
        if(balance != null)
            this.balance = balance;

        if(forbiddenRecipes!=null)
            this.forbiddenRecipes = new ArrayList<>(forbiddenRecipes);
        else
            this.forbiddenRecipes = new ArrayList<>();
        if(freeComponents!=null)
            this.freeComponents = new ArrayList<>(freeComponents);
        else
            this.freeComponents = new ArrayList<>();

    }

    public int[] getBalance() {
        return balance;
    }

    public @NonNull List<@NonNull RecipeParameter> getForbiddenRecipes() {
        return Collections.unmodifiableList(forbiddenRecipes);
    }

    public @NonNull List<@NonNull String> getFreeComponents() {
        return freeComponents;
    }

    @Override
    public String toString() {
        return "RequestWrapper{" +
                "forbiddenRecipes=" + forbiddenRecipes +
                ", freeComponents=" + freeComponents +
                ", balance=" + Arrays.toString(balance) +
                '}';
    }
}
