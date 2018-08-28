package org.liara.recipeoptimizer.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.liara.recipeoptimizer.data.Ingredient;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IGACrawler extends IngredientCrawler {


    public IGACrawler(){

        String Vegetables = "https://www.iga.net/fr/epicerie_en_ligne/fruits_et_legumes?page=1&pageSize=60000";
        String Meats = "https://www.iga.net/fr/epicerie_en_ligne/viande?page=1&pageSize=60000";
        String Fish = "https://www.iga.net/fr/epicerie_en_ligne/fruits_de_mer?page=1&pageSize=60000";
        String Other = "https://www.iga.net/fr/epicerie_en_ligne/parcourir/%c3%89picerie/N%c3%a9cessaire%20%c3%a0%20p%c3%a2tisserie?pageSize=2000";
        crawl(Vegetables );
        crawl(Meats );
        crawl(Other );
        crawl(Fish);


    }
    private void crawl(String toCrawl){
        //createDB();
        Document doc = null;
        try {
            //doc = Jsoup.connect("https://www.iga.net/fr/epicerie_en_ligne/viande").get();
            doc = Jsoup.connect(toCrawl)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0")
                    .referrer("http://www.google.com")
                    .maxBodySize(0)
                    .get();

        } catch (IOException e) {
            e.printStackTrace();
        }


        //get just the interesting place of the page
        Element content =  doc.getElementById("body_0_main_0_ProductSearch_GroceryBrowsing_TemplateResult_SearchResultListView_MansoryPanel");
        //there is some glitches in the IGA's html page
        if(content==null)
            content =  doc.getElementById("body_0_main_1_ProductSearch_GroceryBrowsing_TemplateResult_SearchResultListView_MansoryPanel");

        //Get all products in the page
        Elements divs = content.getElementsByClass("item-product");

        for(Element product : divs){
            String brand="";
            String type="";
            String name="";
            float price=0f;
            Pattern pattern = Pattern.compile("\\d+(,\\d+)?");

            Elements productDetails = product.getAllElements();

            //displays product name, brand, price...
            for (Element e : productDetails){
                if(e.hasClass("item-product__brand")){
                   // System.out.println("##############################");
                    //System.out.println("Brand :" +  e.text());
                    brand=e.text();
                }
                if(e.is("H3")){
                    //System.out.println("name : "+e.text());
                    name=e.text();

                }
                if(e.hasClass("item-product__price")||e.hasClass("item-product__price--sale")){
                    //System.out.println("Price :" +  e.text());
                    final Matcher matcher = pattern.matcher(e.text());
                    if(matcher.find())
                        price=Float.parseFloat(matcher.group(0).replace(',','.'));
                    else{
                        System.out.println(e.text());
                    }

                }



            }
            Ingredient i = new Ingredient(name,new String( "0"),brand,price);
            i.setType(classificator.classify(i));
            allGroceryComponents.add(i);
            //System.out.println(i);

        }
       // System.out.println(divs.size());

    }




}
