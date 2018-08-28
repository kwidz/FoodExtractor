package org.liara.recipeoptimizer.http;

import jCMPL.Cmpl;
import jCMPL.CmplException;
import jCMPL.CmplParameter;
import jCMPL.CmplSet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.recipeoptimizer.crawler.IGACrawler;
import org.liara.recipeoptimizer.crawler.RicardoCrawler;
import org.liara.recipeoptimizer.data.Ingredient;
import org.liara.recipeoptimizer.data.Parameters;
import org.liara.recipeoptimizer.data.Recipe;
import org.liara.recipeoptimizer.database.DAO;
import org.liara.recipeoptimizer.database.ParameterSetter;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {
    @GetMapping("/crawliga")
    public @NonNull String crawlIGA () {
        final @NonNull Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:/home/Extractor/test.db");
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
            connection = DriverManager.getConnection("jdbc:sqlite:/home/Extractor/test.db");
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
    public @NonNull String optimize(){

        try {
            Cmpl m = new Cmpl("/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngredients/src/main/resources/Repas.cmpl");
            m.connect("http://127.0.0.1:4000");
            CmplSet REC = new CmplSet("REC");
            CmplSet ING = new CmplSet("ING");
            CmplParameter costs = new CmplParameter("costs",ING);
            CmplParameter CONT = new CmplParameter("CONT",ING,REC);
            final @NonNull Connection connection;
            final @NonNull Parameters param;
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:/home/Extractor/test.db");
                final DAO dao = new DAO(connection);
                final ParameterSetter p = new ParameterSetter(dao);
                param = p.parameterFile();
                connection.close();
            } catch (SQLException e) {
                return e.getSQLState();
            }
            int[] rec={1,2,3};
            int[] ing={1,2,3,4,5,6};
            float[] co={5.2f,2.3f,5.2f,6f,7f,8f};
            REC.setValues(rec);
            ING.setValues(ing);
            costs.setValues(co);
            System.out.println(param.getCONT());
            int[][]combi = {{0,0,1},{0,1,0},{0,1,0},{1,0,1},{1,0,0},{1,1,1}};
            CONT.setValues(combi);


            m.solve();
            return String.valueOf(m.solution().value());
        } catch (CmplException e) {
            e.printStackTrace();
        }

        return "";
    }
    @GetMapping("/optimize2")
    public @NonNull String optimize2(){

        try {
            Cmpl m = new Cmpl("/tmp/Recipe.cmpl");
            m.connect("http://127.0.0.1:4000");
            m.debug(true);
            m.knock();
            m.solve();
            if (m.solverStatus() == Cmpl.SOLVER_OK) {

                //standard report
                m.solutionReport();

                //example to obtain the solution manually
                System.out.println("Objective name      :" + m.objectiveName());
                System.out.println("Nr. of Variables    :" + m.nrOfVariables());
                System.out.println("Nr. of Constraints  :" + m.nrOfConstraints());
                System.out.println("Nr. of Solutions    :" + m.nrOfSolutions());
                System.out.println("Display variables   :" + m.varDisplayOptions());
                System.out.println("Display constraints :" + m.varDisplayOptions());

                System.out.println("Objective value     :" + m.solution().value() + " " + m.objectiveSense());



                //save solution in several file formats ...
                m.saveSolution();
                m.saveSolutionAscii();
                m.saveSolutionCsv();

            } else {
                System.out.println("Solver failed " + m.solver() + " " + m.solverMessage());
            }


            return String.valueOf(m.solution().value());
        } catch (CmplException e) {
            e.printStackTrace();
        }

        return "fuck !";
    }

    @GetMapping("/test")
    public @NonNull String test(){
        Cmpl m;
        try {
            m = new Cmpl("/tmp/assignement.cmpl");
            CmplSet locations = new CmplSet("locations");
            locations.setValues(1, 4);
            CmplSet machines = new CmplSet("machines");
            machines.setValues(1, 3);
            CmplSet combinations = new CmplSet("A", 2);
            int[][] combiVals = {{1, 1}, {1, 2}, {1, 3}, {1, 4}, {2, 1}, {2, 3},
                    {2, 4}, {3, 1}, {3, 2}, {3, 3}, {3, 4}};
            combinations.setValues(combiVals);
            CmplParameter costs = new CmplParameter("c", combinations);
            int[] costVals = {13, 16, 12, 11, 15, 13, 20, 5, 7, 10, 6};
            costs.setValues(costVals);
            m.setSets(machines, locations, combinations);
            m.setParameters(costs);
            m.connect("http://127.0.0.1:4000");
            m.solve();
            if (m.solverStatus() == Cmpl.SOLVER_OK) {

                //standard report
                m.solutionReport();

                //example to obtain the solution manually
                System.out.println("Objective name      :" + m.objectiveName());
                System.out.println("Nr. of Variables    :" + m.nrOfVariables());
                System.out.println("Nr. of Constraints  :" + m.nrOfConstraints());
                System.out.println("Nr. of Solutions    :" + m.nrOfSolutions());
                System.out.println("Display variables   :" + m.varDisplayOptions());
                System.out.println("Display constraints :" + m.varDisplayOptions());

                System.out.println("Objective value     :" + m.solution().value() + " " + m.objectiveSense());



                //save solution in several file formats ...
                m.saveSolution();
                m.saveSolutionAscii();
                m.saveSolutionCsv();

            } else {
                System.out.println("Solver failed " + m.solver() + " " + m.solverMessage());
            }



        } catch (CmplException e) {
            e.printStackTrace();
        }
        return "test";


    }
    @GetMapping("/getRecipe")
    public Map<String, Object> getRecipe(){
        ArrayList<Recipe> recipesIGA = new ArrayList<>();
        ArrayList<Recipe> recipesMetro = new ArrayList<>();

        recipesIGA.add(new Recipe("choux chinois au boeuf",null,"https://kwidz.fr"));
        recipesIGA.add(new Recipe("poulet curry",null,"https://kwidz.fr"));
        recipesIGA.add(new Recipe("posson panné sauce tomate",null,"https://kwidz.fr"));
        recipesIGA.add(new Recipe("bulgur végétarien",null,"https://kwidz.fr"));
        recipesIGA.add(new Recipe("roti de filet de porc et patates",null,"https://kwidz.fr"));


        recipesMetro.add(new Recipe("boeuf épicé au légumes frais",null, "https://kwidz.fr"));
        recipesMetro.add(new Recipe("nouilles de lanzhou",null, "https://kwidz.fr"));
        recipesMetro.add(new Recipe("poutine",null, "https://kwidz.fr"));
        recipesMetro.add(new Recipe("fish and chips",null, "https://kwidz.fr"));
        recipesMetro.add(new Recipe("salade au porc éffiloché ",null, "https://kwidz.fr"));

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
