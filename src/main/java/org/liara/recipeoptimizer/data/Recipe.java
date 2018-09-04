package org.liara.recipeoptimizer.data;

import java.util.ArrayList;

public class Recipe {
    ArrayList<String> composition;
    private String url;
    String name;

    public Recipe(String name, ArrayList<String> composition, String url){
        this.name=name;
        this.composition = composition;
        this.url = url;
    }

    public ArrayList<String> getComposition() {
        return composition;
    }

    public String getName() {
        return name;
    }




    public String getUrl() {
        return url;
    }


    public String toString(){
        return name+" : \n"+composition+"\n";
    }

}
