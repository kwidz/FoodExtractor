package RecipeExtractor;

import ExtracteurIngredients.Ingredient;

import java.util.ArrayList;

public class Recipe {
    ArrayList<String> composition;
    String name;

    public Recipe(String name, ArrayList<String> composition){
        this.name=name;
        this.composition = composition;
    }
    public String toString(){
        return name+" : \n"+composition+"\n";
    }

}
