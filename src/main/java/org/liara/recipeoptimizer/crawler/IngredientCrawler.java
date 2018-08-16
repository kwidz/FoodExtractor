package org.liara.recipeoptimizer.crawler;

import org.liara.recipeoptimizer.data.Ingredient;
import org.liara.recipeoptimizer.machinelearning.IngredientReader;
import org.liara.recipeoptimizer.math.Trie;

import java.util.ArrayList;

public abstract class IngredientCrawler {
    Trie vegetables;
    IngredientReader classificator;

    public ArrayList<Ingredient> getAllGroceryComponents() {
        return allGroceryComponents;
    }

    ArrayList<Ingredient> allGroceryComponents;


    public IngredientCrawler(){
        classificator = new IngredientReader("/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Meat.txt","/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Modifiers.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Vegetables.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Forbiden.txt");
        allGroceryComponents = new ArrayList<>();
    }

    /*protected String findVegetableType(String name) {
        String type="";
        String[] result = name.split("\\s");
        for (String word:result) {
            if(word.length()>2) {
                if (vegetables.contains(word)) {
                    type = word;
                    break;
                } else {
                    String singular = word.replaceAll("s$", "");
                    if(word == singular)
                        singular = word.replaceAll("x$", "");
                    if(singular.length()>2)
                        if (vegetables.contains(singular)) {
                            type = singular;
                            break;
                        }
                }

            }


        }
        return type;
    }*/

    protected void findVegetableType(Ingredient i){
        i.setType(classificator.classify(i));
    }

    public String toString(){
        return allGroceryComponents.toString();
    }
}
