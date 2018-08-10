package Optimisation;

import ExtracteurIngredients.IGACrawler;
import RecipeExtractor.Recipe;
import RecipeExtractor.RicardoCrawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        final Connection connection =DriverManager.getConnection("jdbc:sqlite:test.db");

        final DAO dao = new DAO(connection);
        final ParameterSetter p = new ParameterSetter(dao);
        p.setREC();

        //dao.createDB();
        //System.out.println("test");
        //RicardoCrawler recipecrawler= new RicardoCrawler();
        //IGACrawler iga = new IGACrawler();
        //dao.insertComponents(iga.getAllGroceryComponents());
        //dao.insertRecipes(recipecrawler.getAllRecipes());
        connection.close();


    }
}
