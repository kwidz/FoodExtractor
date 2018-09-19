package org.liara.recipeoptimizer.machinelearning;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.recipeoptimizer.data.RecipeParameter;
import org.liara.recipeoptimizer.database.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class RecipeReader {
    private ArrayList<RecipeParameter> allRecipes;
    private DAO dao;
    public RecipeReader(){
        try {
        final @NonNull Connection connection;

            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            dao = new DAO(connection);
            allRecipes = dao.selectAllREC();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void classify(){

        for (RecipeParameter r:allRecipes
             ) {
            System.out.println(r);

            if (r.getType()==null) {

                if (r.getName().toUpperCase().contains("POULET")
                        || r.getName().toUpperCase().contains("DINDE")) {
                    System.out.println("\nauto classify poulet\n");
                    try {
                        dao.updateRecipeType(r, 2);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (r.getName().toUpperCase().contains("BOEUF")) {
                    System.out.println("\nauto classify boeuf\n");
                    try {
                        dao.updateRecipeType(r, 1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (r.getName().toUpperCase().contains("PORC")) {
                    System.out.println("\nauto classify porc\n");
                    try {
                        dao.updateRecipeType(r, 3);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (r.getName().toUpperCase().contains("POISSON") ||
                        r.getName().toUpperCase().contains("SAUMON")
                        || r.getName().toUpperCase().contains("MORUE")
                        || r.getName().toUpperCase().contains("TRUITE")
                        || r.getName().toUpperCase().contains("COLIN")
                        || r.getName().toUpperCase().contains("THON")) {
                    System.out.println("\nauto classify poisson\n");
                    try {
                        dao.updateRecipeType(r, 4);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("nom de la recette : " + r.getName() + "" +
                            "\n classifier en Boeuf (1)" +
                            "\n classifier en Poulet (2)" +
                            "\n classifier en Porc (3)" +
                            "\n classifier en Poisson (4)" +
                            "\n classifier en Végé (5)");
                    Scanner s = new Scanner(System.in);
                    int value = s.nextInt();
                    try {
                        dao.updateRecipeType(r, value);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
    public static void main(String[] args){
        RecipeReader r = new RecipeReader();
        r.classify();
    }
}
