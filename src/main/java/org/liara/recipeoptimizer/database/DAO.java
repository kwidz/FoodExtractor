package org.liara.recipeoptimizer.database;

import org.checkerframework.checker.nullness.qual.NonNull;
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
        classificator = new IngredientReader("/home/Extractor/Meat.txt","/home/Extractor/Modifiers.txt", "/home/Extractor/Vegetables.txt", "/home/Extractor/Forbiden.txt");
        this.connection=connection;

    }
    public void insertDB(String name, String brand, String type, String price){

    }

    public void createDB(){

        Statement stmt = null;

        try {


            System.out.println("Opened database successfully");

            stmt = connection.createStatement();
            String safedelete="DROP TABLE IF EXISTS Composition;" +
                    "DROP TABLE IF EXISTS COMPONENTSIGA;" +
                    "DROP TABLE IF EXISTS COMPONENTSMETRO;" +
                    "DROP TABLE IF EXISTS Recipe;";
            stmt.executeUpdate(safedelete);

            String sql = "" +
                    "CREATE TABLE COMPONENTSIGA " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " BRAND           TEXT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL, " +
                    " TYPE        CHAR(50), " +
                    " PRICE         REAL);";

            stmt.executeUpdate(sql);
            sql = "" +
                    "CREATE TABLE COMPONENTSMETRO " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " BRAND           TEXT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL, " +
                    " TYPE        CHAR(50), " +
                    " PRICE         REAL);";

            stmt.executeUpdate(sql);
            sql = "" +
                    "CREATE TABLE Recipe(" +
                    "        ID     INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "        NAME Text NOT NULL," +
                    "URL Text NOT NULL" +
                    ")" +
                    ";";

            String sql2 =   "" +
                    "CREATE TABLE Composition(" +
                    "        id             INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "        IngredientType Text NOT NULL ," +
                    "        Quantity       Text NOT NULL ," +
                    "        id_Recipe      Int NOT NULL" +
                    "," +
                    "" +
                    "FOREIGN KEY (id_Recipe) " +
                    "REFERENCES Recipe(id)" +
                    "ON DELETE CASCADE" +
                    ");";

            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            stmt.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public void clearDB(){

        Statement stmt = null;

        try {


            System.out.println("Opened database successfully");

            stmt = connection.createStatement();
            String safedelete="" +
                    "DROP TABLE IF EXISTS COMPONENTSIGA;" +
                    "DROP TABLE IF EXISTS COMPONENTSMETRO;" +
                    "DROP TABLE IF EXISTS COMPONENTS;";
            stmt.executeUpdate(safedelete);

            String sql = "" +
                    "CREATE TABLE COMPONENTSIGA " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " BRAND           TEXT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL, " +
                    " TYPE        CHAR(50), " +
                    " PRICE         REAL);";

            stmt.executeUpdate(sql);
            sql = "" +
                    "CREATE TABLE COMPONENTSMETRO " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " BRAND           TEXT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL, " +
                    " TYPE        CHAR(50), " +
                    " PRICE         REAL);";

            stmt.executeUpdate(sql);
            stmt.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    //Inserting a Component
    public void insertComponent(Ingredient component, Epicerie ep){

        try {
            connection.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            Statement stmt = connection.createStatement();

            String name = component.getName().replaceAll("\'"," ");
            String brand = component.getBrand().replaceAll("\'"," ");
            if(ep==Epicerie.IGA)
                stmt.executeUpdate(String.join(
                    "",
                    "INSERT INTO COMPONENTSIGA (BRAND,NAME,TYPE,PRICE) ",
                    "VALUES ('", brand, "','", name, "', '", component.getType().replaceAll("\'"," "),
                    "', '", String.valueOf(component.getPrice()), "');"
                ));
            if(ep==Epicerie.METRO)
                stmt.executeUpdate(String.join(
                        "",
                        "INSERT INTO COMPONENTSMETRO (BRAND,NAME,TYPE,PRICE) ",
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

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO RECIPE (NAME,URL) VALUES (?,?);");
            String pattern = "\'";
            String name = r.getName().replaceAll(pattern," ");
            stmt.setString(1,name);
            stmt.setString(2,r.getUrl());
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
    public void insertComponents(ArrayList<Ingredient> components, Epicerie e){

        for (Ingredient i: components
             ) {

            insertComponent(i,e);

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
                recipes.add(new RecipeParameter(rs.getInt("id"),rs.getString("name"),rs.getString("url")));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }
    public ArrayList<IngredientParameter> selectAllING(Epicerie ep){

        ArrayList<IngredientParameter> allING=new ArrayList<>();
        try {
             Statement stmt  = connection.createStatement();
             ResultSet rs;
             if (ep==Epicerie.IGA)
                     rs= stmt.executeQuery("SELECT AVG(PRICE), TYPE\n" +
                     "    FROM COMPONENTSIGA\n" +
                     "GROUP BY TYPE;");
             else
                     rs= stmt.executeQuery("SELECT AVG(PRICE), TYPE\n" +
                     "    FROM COMPONENTSMETRO\n" +
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

    public @NonNull RecipeParameter getRecipeById(int id){
        String sql2 = "Select ID, NAME, URL from Recipe where ID = ?";
        PreparedStatement pstmt = null;
        ResultSet rs=null;
        try {
            pstmt = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1,id);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    RecipeParameter r=null;
        try {
            r = new RecipeParameter(rs.getInt("ID"),rs.getString("NAME"),rs.getString("URL"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;


    }





}
