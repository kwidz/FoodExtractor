package org.liara.recipeoptimizer.http;

import jCMPL.Cmpl;
import jCMPL.CmplException;
import jCMPL.CmplSolElement;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.recipeoptimizer.crawler.IGACrawler;
import org.liara.recipeoptimizer.crawler.RicardoCrawler;
import org.liara.recipeoptimizer.data.Ingredient;
import org.liara.recipeoptimizer.data.Recipe;
import org.liara.recipeoptimizer.database.DAO;
import org.liara.recipeoptimizer.database.ParameterSetter;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class Controller {
    @GetMapping("/crawliga")
    public @NonNull String crawlIGA () {
        final @NonNull Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            final DAO dao = new DAO(connection);
            dao.createDB();
            RicardoCrawler recipecrawler= new RicardoCrawler();
            IGACrawler iga = new IGACrawler();
            dao.insertComponents(iga.getAllGroceryComponents());
            dao.insertRecipes(recipecrawler.getAllRecipes());

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return "success";


    }

    @GetMapping("/makeconfig")
    public @NonNull String makeConfig() {
        final @NonNull Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            final DAO dao = new DAO(connection);
            final ParameterSetter p = new ParameterSetter(dao);
            p.parameterFile();
            connection.close();
        } catch (SQLException e) {
            return e.getSQLState();
        }

        return "success";
    }


    @GetMapping("/optimize")
    public @NonNull Map<String, Object> optimize(){

        makeConfig();
        try {
            final @NonNull Connection connection;
            final DAO dao;
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            dao = new DAO(connection);

            Cmpl m = new Cmpl("Recipe.cmpl");
            m.connect("http://127.0.0.1:4000");
            m.debug(true);
            m.knock();
            m.solve();
            if (m.solverStatus() == Cmpl.SOLVER_OK) {

                //standard report
                m.solutionReport();
                ArrayList<String> selectedRecipes = new ArrayList<>();
                for (CmplSolElement s:m.solution().variables()) {
                    if(s.activity().toString().equals("1")){
                        selectedRecipes.add(s.name());
                    }
                }
                //price
                System.out.println("Objective value     :" + m.solution().value() + " " + m.objectiveSense());



                //save solution in several file formats ...
                m.saveSolution();
                m.saveSolutionAscii();
                m.saveSolutionCsv();

                //return String.valueOf(m.solution().value());
                Map<String, Object> recipesIGA = new HashMap<>();
                Map<String, Object> recipesMetro = new HashMap<>();
                Pattern pattern = Pattern.compile("([\\+-]?\\d+)([eE][\\+-]?\\d+)?");
                System.out.println(selectedRecipes.get(0).replace("X[","").replace("]",""));
                recipesIGA.put("monday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(0).replace("X[","").replace("]",""))));
                recipesIGA.put("tuesday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(1).replace("X[","").replace("]",""))));
                recipesIGA.put("wednesday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(2).replace("X[","").replace("]",""))));
                recipesIGA.put("thursday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(3).replace("X[","").replace("]",""))));
                recipesIGA.put("friday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(4).replace("X[","").replace("]",""))));


                recipesMetro.put("monday",new Recipe("boeuf épicé au légumes frais",null, "https://kwidz.fr"));
                recipesMetro.put("tuesday",new Recipe("nouilles de lanzhou",null, "https://kwidz.fr"));
                recipesMetro.put("wednesday",new Recipe("poutine",null, "https://kwidz.fr"));
                recipesMetro.put("thursday",new Recipe("fish and chips",null, "https://kwidz.fr"));
                recipesMetro.put("friday",new Recipe("salade au porc éffiloché ",null, "https://kwidz.fr"));

                double prixIGA=m.solution().value();
                double prixMetro=56.62f;


                Map<String, Object> week = new HashMap<>();
                week.put("weekIGA", recipesIGA);
                week.put("weekMetro", recipesMetro);
                week.put("prixIGA", prixIGA);
                week.put("prixMetro", prixMetro);
                return week;

            } else {
                System.out.println("Solver failed " + m.solver() + " " + m.solverMessage());
            }




        } catch (CmplException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @GetMapping("/getRecipe")
    public Map<String, Object> getRecipe(){
        Map<String, Object> recipesIGA = new HashMap<>();
        Map<String, Object> recipesMetro = new HashMap<>();

        recipesIGA.put("monday",new Recipe("choux chinois au boeuf",null,"https://kwidz.fr"));
        recipesIGA.put("tuesday",new Recipe("poulet curry",null,"https://kwidz.fr"));
        recipesIGA.put("wednesday",new Recipe("posson panné sauce tomate",null,"https://kwidz.fr"));
        recipesIGA.put("thursday",new Recipe("bulgur végétarien",null,"https://kwidz.fr"));
        recipesIGA.put("friday",new Recipe("roti de filet de porc et patates",null,"https://kwidz.fr"));


        recipesMetro.put("monday",new Recipe("boeuf épicé au légumes frais",null, "https://kwidz.fr"));
        recipesMetro.put("tuesday",new Recipe("nouilles de lanzhou",null, "https://kwidz.fr"));
        recipesMetro.put("wednesday",new Recipe("poutine",null, "https://kwidz.fr"));
        recipesMetro.put("thursday",new Recipe("fish and chips",null, "https://kwidz.fr"));
        recipesMetro.put("friday",new Recipe("salade au porc éffiloché ",null, "https://kwidz.fr"));

        float prixIGA=68.33f;
        float prixMetro=56.62f;


        Map<String, Object> week = new HashMap<>();
        week.put("weekIGA", recipesIGA);
        week.put("weekMetro", recipesMetro);
        week.put("prixIGA", prixIGA);
        week.put("prixMetro", prixMetro);
        return week;
    }


    @GetMapping("/hello/{name}")
    public @NonNull String helloGuy (@PathVariable @NonNull final String name) {
        return "hello " + name;
    }

    @GetMapping("/ingredient")
    public @NonNull Ingredient helloGuy () {
        return new Ingredient("bullshit", "moncul", "pascher ltd.", 5000f);
    }
}
