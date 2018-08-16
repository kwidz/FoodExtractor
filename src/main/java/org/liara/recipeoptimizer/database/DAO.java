package org.liara.recipeoptimizer.database;

import org.liara.recipeoptimizer.data.*;
import org.liara.recipeoptimizer.machinelearning.IngredientReader;

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
    private final IngredientReader classificator;
    private final Connection connection;
    //instanciate DAO object
    public DAO(final Connection connection){
        classificator = new IngredientReader("/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Meat.txt","/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Modifiers.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Vegetables.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Forbiden.txt");
        this.connection=connection;

    }
    public void insertDB(String name, String brand, String type, String price){

    }

    public void createDB(){

        Statement stmt = null;

        try {


            System.out.println("Opened database successfully");

            stmt = connection.createStatement();
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

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    //Inserting a Component
    public void insertComponent(Ingredient component){

        try {
            connection.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            Statement stmt = connection.createStatement();

            String name = component.getName().replaceAll("\'"," ");
            String brand = component.getBrand().replaceAll("\'"," ");
            stmt.executeUpdate(String.join(
                "",
                "INSERT INTO COMPONENTS (BRAND,NAME,TYPE,PRICE) ",
                "VALUES ('", brand, "','", name, "', '", component.getType().replaceAll("\'"," "),
                "', '", String.valueOf(component.getPrice()), "');"
            ));


            stmt.close();
            connection.commit();

        } catch ( final Exception e ) {
            throw new Error(e);

        }
        //System.out.println("Records created successfully");
    }

    //Inserting a Recipe
    public void insertRecipe(Recipe r){


        int key = 0;
        try {
            connection.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO RECIPE (NAME) VALUES (?);");
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
            PreparedStatement pstmt = connection.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
            for (String ingredient:r.getComposition()
                    ) {
                pstmt.setString(1,classificator.classify(ingredient).replaceAll(pattern," "));
                pstmt.setInt(2,0);
                pstmt.setInt(3,key);
                pstmt.executeUpdate();
            }

            pstmt.close();
            connection.commit();

        } catch ( final Exception e ) {
            throw new Error(e);
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

    public ArrayList<RecipeParameter> selectAllREC(){

    ArrayList<RecipeParameter> recipes = new ArrayList<>();
        try {
                Statement stmt  = connection.createStatement();
                ResultSet rs    = stmt.executeQuery("SELECT * FROM RECIPE");

            // loop through the result set
            while (rs.next()) {
                recipes.add(new RecipeParameter(rs.getInt("id"),rs.getString("name")));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }
    public ArrayList<IngredientParameter> selectAllING(){

        ArrayList<IngredientParameter> allING=new ArrayList<>();
        try {
             Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery("SELECT AVG(PRICE), TYPE\n" +
                     "    FROM COMPONENTS\n" +
                     "GROUP BY TYPE;");

            // loop through the result set
            int id=0;
            while (rs.next()) {
                allING.add(new IngredientParameter(rs.getString(2),rs.getFloat(1), ++id));


            }
        } catch ( final SQLException e) {
            throw new Error(e);
        }

        return allING;
    }

    public ArrayList<Composition> selectAllComposition(){
        ArrayList<Composition> compos = new ArrayList<>();

        try {
            Statement stmt  = connection.createStatement();
            ResultSet rs    = stmt.executeQuery("SELECT  id, IngredientType, id_Recipe FROM Composition");

            // loop through the result set
            while (rs.next()) {

                compos.add(new Composition(rs.getInt("id_Recipe"),rs.getString("IngredientType")));
            }
        } catch ( final SQLException e) {
            throw new Error(e);
        }
        return compos;
    }





}
