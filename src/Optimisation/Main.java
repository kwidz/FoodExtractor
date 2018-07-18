package Optimisation;

import ExtracteurIngredients.IGACrawler;
import RecipeExtractor.Recipe;
import RecipeExtractor.RicardoCrawler;

public class Main {
    public static void main(String[] args){
        DAO dao = new DAO();
        dao.createDB();
        RicardoCrawler recipecrawler= new RicardoCrawler();
        IGACrawler iga = new IGACrawler();
        dao.insertComponents(iga.getAllGroceryComponents());
        dao.insertRecipes(recipecrawler.getAllRecipes());
    }
}
