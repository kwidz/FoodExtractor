package org.liara.recipeoptimizer.data;

public class Composition {
    private final int idRecipe;
    private final String ingredientType;



    public Composition(int idRecipe, String ingredientType) {

        this.idRecipe = idRecipe;
        this.ingredientType = ingredientType;
    }

    public String getIngredientType() {
        return this.ingredientType;
    }

    public int getIdRecipe() {

        return idRecipe;
    }
}
