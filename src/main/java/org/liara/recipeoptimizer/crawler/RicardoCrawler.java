package org.liara.recipeoptimizer.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.liara.recipeoptimizer.data.Recipe;
import org.liara.recipeoptimizer.machinelearning.IngredientReader;

import java.io.IOException;
import java.util.ArrayList;

public class RicardoCrawler {

    ArrayList<String> allIngredients = new ArrayList<String>();
    ArrayList<Recipe> allRecipes = new ArrayList<Recipe>();
    IngredientReader classificator;
    public RicardoCrawler() {
        classificator = new IngredientReader("Meat.txt","Modifiers.txt", "Vegetables.txt", "Forbiden.txt");
        crawl();

    }

    public static @org.checkerframework.checker.nullness.qual.NonNull ArrayList<String> crawlGroceryList(ArrayList<String> linkList){
        //formIngredients
        ArrayList<String> allIngredients=new ArrayList<>();
        for(String url : linkList){
            Document doc = null;
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0")
                        .referrer("http://www.google.com")
                        .maxBodySize(0)
                        .get();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements components = doc.getElementById("formIngredients").getElementsByTag("li");
            for (Element e:components) {
                allIngredients.add(e.text());

            }
            allIngredients.add("\n");
        }
        return allIngredients;

    }

    private void crawl(){
        int i = 0;
        while(true) {
            i++;
            String url = "https://www.ricardocuisine.com/themes/30-minutes/page/"+i;
            Document doc = null;

            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0")
                        .referrer("http://www.google.com")
                        .maxBodySize(0)
                        .get();

            } catch (IOException e) {
                e.printStackTrace();
            }

            //get all recipe of the page
            Elements content = doc.getElementsByClass("picture");
            if(content.size()==0)
                break;
            for (Element rec : content
                    ) {


                //System.out.println("\n#################################\n");
                String name = rec.attr("title");
                //System.out.println(rec.attr("title")+ "\n");
                Element link = rec.select("a").first();
                //Link to access one recipe of the page
                String absHref = link.attr("abs:href"); // "http://jsoup.org/"

                Document recipe = null;
                try {
                    recipe = Jsoup.connect(absHref)
                            .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0")
                            .referrer("http://www.google.com")
                            .maxBodySize(0)
                            .get();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // get all components of the recipe !
                Elements ListComponents = recipe.getElementsByClass("form-ingredients").get(0).getElementsByTag("li");
                ArrayList<String> composition = new ArrayList<String>();
                for (Element e : ListComponents
                        ) {
                    //System.out.println(e.text());
                    allIngredients.add(e.text());
                    composition.add(e.text());
                }
                System.out.println("crawling : "+absHref);
                Recipe r = new Recipe(name,composition,absHref);
                allRecipes.add(r);
            }
        }
        System.out.println("Recipe crawler done ! ");

    }

    //return all ingredients for machine learning classificator
    public ArrayList<String> getAllIngredients(){
        return allIngredients;
    }
    public ArrayList<Recipe> getAllRecipes(){return  allRecipes;}
}
