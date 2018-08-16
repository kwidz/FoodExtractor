package org.liara.recipeoptimizer.data;

import java.util.ArrayList;

public class Recipe {
    ArrayList<String> composition;

    public ArrayList<String> getComposition() {
        return composition;
    }

    public String getName() {
        return name;
    }

    String name;

    public Recipe(String name, ArrayList<String> composition){
        this.name=name;
        this.composition = composition;
    }
    public String toString(){
        return name+" : \n"+composition+"\n";
    }

}
