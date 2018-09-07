package org.liara.recipeoptimizer.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.liara.recipeoptimizer.data.Ingredient;
import org.liara.recipeoptimizer.math.Trie;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetroCrawler extends IngredientCrawler {

    public MetroCrawler() {

        String Vegetables = "https://www.metro.ca/epicerie-en-ligne/allees/fruits-et-legumes-page-";
        String Meats = "https://www.metro.ca/epicerie-en-ligne/allees/viandes-et-volailles-page-";
        String Fish = "https://www.metro.ca/epicerie-en-ligne/allees/poissons-et-fruits-de-mer-page-";
        String Other = "https://www.metro.ca/epicerie-en-ligne/allees/epicerie-page-";
        crawl(Vegetables );
        crawl(Meats );
        crawl(Other );
        crawl(Fish);
    }
    public void crawl(String toCrawl){




        //Get the html page
        //createDB();
        int i=1;
        System.out.println("crawling : "+toCrawl);
        while (true) {
            Document doc = null;
            try {
                //doc = Jsoup.connect("https://www.iga.net/fr/epicerie_en_ligne/viande").get();
                doc = Jsoup.connect(toCrawl+i+"?sortOrder=name-asc")
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0")
                        .referrer("http://www.google.com")
                        .maxBodySize(0)
                        .get();
                i++;

            } catch (IOException e) {
                e.printStackTrace();
            }


            //get just the interesting place of the page
            Elements content = doc.getElementsByClass("products-listing");
            //System.out.println(content.get(0));
            if (content.get(0).attr("data-total-results").equals("0"))
                break;


            //Get all products in the page
            Elements divs = content.get(0).getElementsByClass("product-tile");

            for (Element product : divs) {
                String brand = "";
                String name = "";
                float price =0f;
                Pattern pattern = Pattern.compile("\\d+(,\\d+)?");

                Elements productDetails = product.getAllElements();

                //displays product name, brand, price...
                for (Element e : productDetails) {
                    if (e.hasClass("pt--brand")) {

                        //System.out.println("Brand :" + e.text());
                        brand = e.text();
                    }
                    if (e.hasClass("pt--title")) {
                        //System.out.println("name : " + e.text());
                        name = e.text();

                        //type = findVegetableType(name);//=_vegetables.bestMatch(e.text(),100);


                    }

                    if (e.hasClass("pi--prices--first-line")) {
                        final Matcher matcher = pattern.matcher(e.text());
                        if(matcher.find())
                            price = Float.parseFloat(matcher.group(0).replace(',','.'));
                        //System.out.println("Price:" + price + "origin : "+e.text());

                    }
                    else if(e.hasClass("pi--price-regular"))
                {
                    final Matcher matcher = pattern.matcher(e.text());
                    if(matcher.find())
                        price = Float.parseFloat(matcher.group(0).replace(',','.'));
                    //System.out.println("Price (regular):" + price+ "origin : "+e.text());
                }

                }
                Ingredient ing = new Ingredient(name,new String("0"), brand, price);
                ing.setType(classificator.classify(ing));
                allGroceryComponents.add(ing);
                //insertDB(name, brand, type, price);

            }
        }


        //System.out.println(divs.size());




    }

    public static void main (String[] args){
        MetroCrawler m = new MetroCrawler();

    }

}


