package org.liara.recipeoptimizer.http;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.recipeoptimizer.crawler.IGACrawler;
import org.liara.recipeoptimizer.crawler.RicardoCrawler;
import org.liara.recipeoptimizer.data.Ingredient;
import org.liara.recipeoptimizer.database.DAO;
import org.liara.recipeoptimizer.database.ParameterSetter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    @GetMapping("/hello/{name}")
    public @NonNull String helloGuy (@PathVariable @NonNull final String name) {
        return "hello " + name;
    }

    @GetMapping("/ingredient")
    public @NonNull Ingredient helloGuy () {
        return new Ingredient("bullshit", "moncul", "pascher ltd.", 5000f);
    }
}
