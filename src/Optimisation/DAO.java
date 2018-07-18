package Optimisation;


import ExtracteurIngredients.Ingredient;
import MachineLearning.IngredientReader;
import RecipeExtractor.Recipe;

import java.sql.*;
import java.util.ArrayList;

/*
This class is designed in order to make all access to data base.

Including :
creating the database,
Inserting components
Inserting recipes
Linking recipes with components !

 */
public class DAO {
    IngredientReader classificator;
    //instanciate DAO object
    public DAO(){
        classificator = new IngredientReader("/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Meat.txt","/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Modifiers.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Vegetables.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Forbiden.txt");

    }
    public void insertDB(String name, String brand, String type, String price){

    }

    public void createDB(){
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE COMPONENTS " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " BRAND           TEXT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL, " +
                    " TYPE        CHAR(50), " +
                    " PRICE         REAL);";

            stmt.executeUpdate(sql);
            sql = "CREATE TABLE Recipe(" +
                    "        ID     INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "        NAME Text NOT NULL)" +
                    ";";

            String sql2 =   "CREATE TABLE Composition(" +
                    "        id             INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "        IngredientType Text NOT NULL ," +
                    "        Quantity       Text NOT NULL ," +
                    "        id_Recipe      Int NOT NULL" +
                    ",FOREIGN KEY (id_Recipe) REFERENCES Recipe(id));";

            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    //Inserting a Component
    public void insertComponent(Ingredient component){
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String pattern = "\'";
            String name = component.getName().replaceAll(pattern," ");
            String brand = component.getBrand().replaceAll(pattern," ");
            String sql = "INSERT INTO COMPONENTS (BRAND,NAME,TYPE,PRICE) " +
                    "VALUES ('"+brand+"','"+name+"', '"+component.getType().replaceAll(pattern," ")+"', '"+component.getPrice()+"');";

            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        //System.out.println("Records created successfully");
    }

    //Inserting a Recipe
    public void insertRecipe(Recipe r){
        Connection c = null;
        PreparedStatement stmt = null;
        int key = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");
            String sql = "INSERT INTO RECIPE (NAME) VALUES (?);";
            stmt = c.prepareStatement(sql);
            String pattern = "\'";
            String name = r.getName().replaceAll(pattern," ");
            stmt.setString(1,name);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            keys.next();
            key = keys.getInt(1);
            keys.close();
            stmt.close();
            String sql2 = "INSERT INTO COMPOSITION (IngredientType ,Quantity, id_Recipe) VALUES (?,?,?);";
            PreparedStatement pstmt = c.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
            for (String ingredient:r.getComposition()
                    ) {
                pstmt.setString(1,classificator.classify(ingredient).replaceAll(pattern," "));
                pstmt.setInt(2,0);
                pstmt.setInt(3,key);
                pstmt.executeUpdate();
            }

            pstmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        //System.out.println("Records created successfully");
    }

    //Inserting some Components
    public void insertComponents(ArrayList<Ingredient> components){

        for (Ingredient i: components
             ) {

            insertComponent(i);

        }

    }

    //Inserting some Recipes
    public void insertRecipes(ArrayList<Recipe> r){
        for (Recipe recipe:r
             ) {
            insertRecipe(recipe);

        }
    }


}
