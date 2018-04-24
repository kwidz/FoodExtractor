package ExtracteurIngredients;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class IGACrawler extends IngredientCrawler {


    public IGACrawler(Trie _vegetables){
        vegetables=_vegetables;

        //Get the html page
        createDB();
        Document doc = null;
        try {
            //doc = Jsoup.connect("https://www.iga.net/fr/epicerie_en_ligne/viande").get();
            doc = Jsoup.connect("https://www.iga.net/fr/epicerie_en_ligne/fruits_et_legumes?page=1&pageSize=60000")
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
            String price="";

                Elements productDetails = product.getAllElements();

                //displays product name, brand, price...
                for (Element e : productDetails){
                    if(e.hasClass("item-product__brand")){
                        System.out.println("##############################");
                        System.out.println("Brand :" +  e.text());
                        brand=e.text();
                    }
                    if(e.is("H3")){
                        System.out.println("name : "+e.text());
                        name=e.text();

                        type=findVegetableType(name);//=_vegetables.bestMatch(e.text(),100);

                        System.out.println("Type : "+type);

                    }
                    if(e.hasClass("item-product__price")){
                        System.out.println("Price :" +  e.text());
                        System.out.println("##############################");
                        price=e.text();
                    }
                    if(e.hasClass("item-product__price--sale")){
                        System.out.println("Price (sale) :" +  e.text());
                        price=e.text();

                    }

                }
                insertDB(name,brand,type,price);

        }
        System.out.println(divs.size());


    }



}
