package ExtracteurIngredients;


import MachineLearning.IngredientReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class IngredientCrawler {
    Trie vegetables;
    IngredientReader classificator;
    ArrayList<Ingredient> allGroceryComponents;


    public IngredientCrawler(){
        classificator = new IngredientReader("/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Meat.txt","/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Modifiers.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Vegetables.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Forbiden.txt");
        allGroceryComponents = new ArrayList<>();
    }
    //TODO Remake with the ingredient type
    public void insertDB(String name, String brand, String type, String price){
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String pattern = "\'";
            name = name.replaceAll(pattern," ");
            brand = brand.replaceAll(pattern," ");
            String sql = "INSERT INTO FOOD (BRAND,NAME,TYPE,PRICE) " +
                    "VALUES ('"+brand+"','"+name+"', '"+type+"', '"+price+"');";
            System.out.println(sql);
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public void createDB(){
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE FOOD " +
                    "(ID INT PRIMARY KEY," +
                    " BRAND           TEXT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL, " +
                    " TYPE        CHAR(50), " +
                    " PRICE         REAL)";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
    /*protected String findVegetableType(String name) {
        String type="";
        String[] result = name.split("\\s");
        for (String word:result) {
            if(word.length()>2) {
                if (vegetables.contains(word)) {
                    type = word;
                    break;
                } else {
                    String singular = word.replaceAll("s$", "");
                    if(word == singular)
                        singular = word.replaceAll("x$", "");
                    if(singular.length()>2)
                        if (vegetables.contains(singular)) {
                            type = singular;
                            break;
                        }
                }

            }


        }
        return type;
    }*/

    protected void findVegetableType(Ingredient i){
        i.setType(classificator.classify(i));
    }

    public String toString(){
        return allGroceryComponents.toString();
    }
}
