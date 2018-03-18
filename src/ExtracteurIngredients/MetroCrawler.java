package ExtracteurIngredients;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MetroCrawler extends Crawler {

    public MetroCrawler(Trie _vegetables) {
        vegetables = _vegetables;

        //Get the html page
        createDB();
        int i=1;
        while (true) {
            Document doc = null;
            try {
                //doc = Jsoup.connect("https://www.iga.net/fr/epicerie_en_ligne/viande").get();
                doc = Jsoup.connect("https://www.metro.ca/epicerie-en-ligne/allees/fruits-et-legumes-page-"+i+"?sortOrder=name-asc")
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
            System.out.println(content.get(0));
            if (content.get(0).attr("data-total-results").equals("0"))
                break;


            //Get all products in the page
            Elements divs = content.get(0).getElementsByClass("product-tile");

            for (Element product : divs) {
                String brand = "";
                String type = "";
                String name = "";
                String price = "";

                Elements productDetails = product.getAllElements();

                //displays product name, brand, price...
                for (Element e : productDetails) {
                    if (e.hasClass("pt--brand")) {

                        System.out.println("Brand :" + e.text());
                        brand = e.text();
                    }
                    if (e.hasClass("pt--title")) {
                        System.out.println("name : " + e.text());
                        name = e.text();

                        type = findVegetableType(name);//=_vegetables.bestMatch(e.text(),100);

                        System.out.println("Type : " + type);

                    }
                /*if (e.hasClass("pi--price-regular")) {

                    price = e.text().split("\\$")[0];
                    System.out.println("Price (regular):" + price);


                }*/
                    if (e.hasClass("pi--prices--first-line")) {

                        price = e.text().split("\\$")[0];
                        System.out.println("Price:" + price);

                    }

                }
                insertDB(name, brand, type, price);

            }
        }
        //System.out.println(divs.size());


    }
}


