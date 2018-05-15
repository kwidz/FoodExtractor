package RecipeExtractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RicardoCrawler {

    ArrayList<String> allIngredients = new ArrayList<String>();
    public RicardoCrawler() {
        int i = 0;
        while(true) {
            i++;
            String url = "https://www.ricardocuisine.com/themes/30-minutes/page/"+i;
            Document doc = null;

            try {
                //doc = Jsoup.connect("https://www.iga.net/fr/epicerie_en_ligne/viande").get();
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
                //System.out.println(rec.attr("title")+ "\n");
                Element link = rec.select("a").first();
                //Link to access one recipe of the page
                String absHref = link.attr("abs:href"); // "http://jsoup.org/"

                Document recipe = null;
                try {
                    //doc = Jsoup.connect("https://www.iga.net/fr/epicerie_en_ligne/viande").get();
                    recipe = Jsoup.connect(absHref)
                            .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0")
                            .referrer("http://www.google.com")
                            .maxBodySize(0)
                            .get();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements ListComponents = recipe.getElementsByClass("form-ingredients").get(0).getElementsByTag("li");
                for (Element e : ListComponents
                        ) {
                    //System.out.println(e.text());
                    allIngredients.add(e.text());

                }
            }
        }
        System.out.println("Recipe crawler done ! ");

    }

    public ArrayList<String> getAllIngredients(){
        return allIngredients;
    }

}
