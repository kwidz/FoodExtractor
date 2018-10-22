package org.liara.recipeoptimizer.http;

import jCMPL.Cmpl;
import jCMPL.CmplException;
import jCMPL.CmplSolElement;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.recipeoptimizer.crawler.IGACrawler;
import org.liara.recipeoptimizer.crawler.MetroCrawler;
import org.liara.recipeoptimizer.crawler.RicardoCrawler;
import org.liara.recipeoptimizer.data.Epicerie;
import org.liara.recipeoptimizer.data.Ingredient;
import org.liara.recipeoptimizer.data.Recipe;
import org.liara.recipeoptimizer.data.RecipeParameter;
import org.liara.recipeoptimizer.database.DAO;
import org.liara.recipeoptimizer.database.ParameterSetter;
import org.liara.recipeoptimizer.machinelearning.IngredientReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@EnableCaching

public class Controller {
    @GetMapping("/crawl")
    public @NonNull String crawl() {
        final @NonNull Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            final DAO dao = new DAO(connection);
            dao.clearDB();
            System.out.println("dbCleared");

            IGACrawler iga = new IGACrawler();
            System.out.println("iga crawled");
            MetroCrawler metro = new MetroCrawler();
            System.out.println("Metro crawled");
            dao.insertComponents(metro.getAllGroceryComponents(), Epicerie.METRO);
            System.out.println("Ingredients METRO inserted");
            dao.insertComponents(iga.getAllGroceryComponents(), Epicerie.IGA);
            System.out.println("Ingredients IGA inserted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "success";
    }


    public String makeConfig(Epicerie epicerie, List<RecipeParameter> forbiddenRecipes, List<String> freeComponents, int[] balance) {
        final @NonNull Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            final DAO dao = new DAO(connection);
            final ParameterSetter p = new ParameterSetter(dao);
            p.parameterFile(epicerie, forbiddenRecipes, freeComponents, balance);
            connection.close();
        } catch (SQLException e) {
            return e.getSQLState();
        }

        return "success";
    }

    /*@PostMapping("/optimzeBeta")
    public @NonNull Map<String, Object> optimizeBeta(@RequestBody(required=false) RequestWrapper r){
        Map<String, Object> week = new HashMap<>();
        if(r==null)
            r=new RequestWrapper(null,null, );
        try {
            final @NonNull Connection connection;
            final DAO dao;
            Map<String, Object> recipesIGA = new HashMap<>();
            Map<String, Object> recipesMetro = new HashMap<>();
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            dao = new DAO(connection);
            makeConfig(Epicerie.METRO, r.getForbiddenRecipes(), r.getFreeComponents(),r.getBalance());
            Cmpl m = new Cmpl("Recipe.cmpl");
            m.connect("http://kwidz.fr:4000");
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


                System.out.println(selectedRecipes.get(0).replace("X[","").replace("]",""));
                recipesIGA.put("monday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(0).replace("X[","").replace("]",""))));
                recipesIGA.put("tuesday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(1).replace("X[","").replace("]",""))));
                recipesIGA.put("wednesday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(2).replace("X[","").replace("]",""))));
                recipesIGA.put("thursday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(3).replace("X[","").replace("]",""))));
                recipesIGA.put("friday",dao.getRecipeById(Integer.parseInt(selectedRecipes.get(4).replace("X[","").replace("]",""))));

                double prixIGA=m.solution().value();


                week.put("weekIGA", recipesIGA);
                week.put("prixIGA", prixIGA);

            } else {
                System.out.println("Solver failed " + m.solver() + " " + m.solverMessage());
            }

        } catch (CmplException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return week;
    }
    */

    @PostMapping("/optimize")
    public @NonNull ResponseEntity<Map<String, Object>> optimize(@RequestBody(required = false) RequestWrapper r) {


        Map<String, Object> week = new HashMap<>();
        if (r == null)
            r = new RequestWrapper(null, null, null);

        try {
            final @NonNull Connection connection;
            final DAO dao;
            Map<String, Object> recipesIGA = new HashMap<>();
            Map<String, Object> recipesMetro = new HashMap<>();
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            dao = new DAO(connection);
            makeConfig(Epicerie.IGA, r.getForbiddenRecipes(), r.getFreeComponents(), r.getBalance());
            Cmpl m = new Cmpl("Recipe.cmpl");
            m.connect("http://kwidz.fr:4000");
            m.knock();
            m.solve();
            if (m.solverStatus() == Cmpl.SOLVER_OK) {

                //standard report
                m.solutionReport();
                ArrayList<String> selectedRecipes = new ArrayList<>();
                for (CmplSolElement s : m.solution().variables()) {
                    if (s.activity().toString().equals("1")) {
                        selectedRecipes.add(s.name());
                    }
                }
                //price
                System.out.println("Objective value     :" + m.solution().value() + " " + m.objectiveSense());


                System.out.println(selectedRecipes.get(0).replace("X[", "").replace("]", ""));
                recipesIGA.put("monday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(0).replace("X[", "").replace("]", ""))));
                recipesIGA.put("tuesday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(1).replace("X[", "").replace("]", ""))));
                recipesIGA.put("wednesday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(2).replace("X[", "").replace("]", ""))));
                recipesIGA.put("thursday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(3).replace("X[", "").replace("]", ""))));
                recipesIGA.put("friday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(4).replace("X[", "").replace("]", ""))));

                double prixIGA = m.solution().value();


                week.put("weekIGA", recipesIGA);
                week.put("prixIGA", prixIGA);

            } else {
                System.out.println("Solver failed " + m.solver() + " " + m.solverMessage());
            }
            makeConfig(Epicerie.METRO, r.getForbiddenRecipes(), r.getFreeComponents(), r.getBalance());
            m = new Cmpl("Recipe.cmpl");
            m.connect("http://kwidz.fr:4000");
            m.knock();
            m.solve();
            if (m.solverStatus() == Cmpl.SOLVER_OK) {

                //standard report
                m.solutionReport();
                ArrayList<String> selectedRecipes = new ArrayList<>();
                for (CmplSolElement s : m.solution().variables()) {
                    if (s.activity().toString().equals("1")) {
                        selectedRecipes.add(s.name());
                    }
                }
                //price
                System.out.println("Objective value     :" + m.solution().value() + " " + m.objectiveSense());

                //return String.valueOf(m.solution().value());


                System.out.println(selectedRecipes.get(0).replace("X[", "").replace("]", ""));
                recipesMetro.put("monday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(0).replace("X[", "").replace("]", ""))));
                recipesMetro.put("tuesday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(1).replace("X[", "").replace("]", ""))));
                recipesMetro.put("wednesday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(2).replace("X[", "").replace("]", ""))));
                recipesMetro.put("thursday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(3).replace("X[", "").replace("]", ""))));
                recipesMetro.put("friday", dao.getRecipeById(Integer.parseInt(selectedRecipes.get(4).replace("X[", "").replace("]", ""))));

                double prixMetro = m.solution().value();
                week.put("weekMetro", recipesMetro);
                week.put("prixMetro", prixMetro);


            }

        } catch (CmplException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(FileWriter fw = new FileWriter("logs.json", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
            out.println("date : "+ sdf.format(new Date().getTime()));
            out.println("Request : "+ r);
            out.println("Response : "+week);
            out.println("########################################");
            out.flush();
            System.out.println("test !!!");
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)).body(week);
    }


    @GetMapping("/getRecipe")
    public Map<String, Object> getRecipe() {
        Map<String, Object> recipesIGA = new HashMap<>();
        Map<String, Object> recipesMetro = new HashMap<>();

        recipesIGA.put("monday", new Recipe("choux chinois au boeuf", null, "https://kwidz.fr"));
        recipesIGA.put("tuesday", new Recipe("poulet curry", null, "https://kwidz.fr"));
        recipesIGA.put("wednesday", new Recipe("posson panné sauce tomate", null, "https://kwidz.fr"));
        recipesIGA.put("thursday", new Recipe("bulgur végétarien", null, "https://kwidz.fr"));
        recipesIGA.put("friday", new Recipe("roti de filet de porc et patates", null, "https://kwidz.fr"));


        recipesMetro.put("monday", new Recipe("boeuf épicé au légumes frais", null, "https://kwidz.fr"));
        recipesMetro.put("tuesday", new Recipe("nouilles de lanzhou", null, "https://kwidz.fr"));
        recipesMetro.put("wednesday", new Recipe("poutine", null, "https://kwidz.fr"));
        recipesMetro.put("thursday", new Recipe("fish and chips", null, "https://kwidz.fr"));
        recipesMetro.put("friday", new Recipe("salade au porc éffiloché ", null, "https://kwidz.fr"));

        float prixIGA = 68.33f;
        float prixMetro = 56.62f;


        Map<String, Object> week = new HashMap<>();
        week.put("weekIGA", recipesIGA);
        week.put("weekMetro", recipesMetro);
        week.put("prixIGA", prixIGA);
        week.put("prixMetro", prixMetro);
        return week;
    }


    @GetMapping("/getTypes")
    public Map<String, Object> getTypes() {
        IngredientReader i = new IngredientReader(
                "Meat.txt",
                "Modifiers.txt",
                "Vegetables.txt",
                "Forbiden.txt");
        List<String> alltypes = i.getMeatFishCheeseList();
        alltypes.addAll(i.getVegetables());
        List<String> modifiers = i.getModifiers();
        Map retVal = new HashMap();
        retVal.put("Ingredients", alltypes);
        retVal.put("modifiers", modifiers);
        return retVal;


    }
    @Cacheable("urls")
    @PostMapping("/getList")
    public @NonNull ArrayList<String> getList(@RequestBody ArrayList<String> urls){
        System.out.println(urls);
        return RicardoCrawler.crawlGroceryList(urls);

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
