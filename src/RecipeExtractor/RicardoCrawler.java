package RecipeExtractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class RicardoCrawler {


    public RicardoCrawler(){

        Document doc = null;
        try {
            //doc = Jsoup.connect("https://www.iga.net/fr/epicerie_en_ligne/viande").get();
            doc = Jsoup.connect("https://www.ricardocuisine.com/themes/30-minutes/page/1")
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0")
                    .referrer("http://www.google.com")
                    .maxBodySize(0)
                    .get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //get all recipe of the page
        Elements content = doc.getElementsByClass("picture");
        System.out.println(content.get(0));


    }

}
