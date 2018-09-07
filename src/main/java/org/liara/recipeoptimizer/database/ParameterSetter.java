package org.liara.recipeoptimizer.database;

import org.liara.recipeoptimizer.data.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParameterSetter {


    private final DAO dao;

    public ParameterSetter(final DAO dao){
        this.dao = dao;
    }

    public Parameters parameterFile(Epicerie epicerie){

        ArrayList<IngredientParameter> components = dao.selectAllING(epicerie);
        ArrayList<RecipeParameter> recipes = dao.selectAllREC();
        ArrayList<Composition> compositions = dao.selectAllComposition();
        System.out.println(components);
        String REC = "<";
        String ING = "<";
        String costs = "<";
        String CONT = "<";

        for (IngredientParameter c:components
             ) {

            ING=ING.concat('\"'+String.valueOf(c.getId())+"\" ");
            costs=costs.concat(String.valueOf(c.getPrice())+" ");

        }
        ING=ING.concat(">");
        costs=costs.concat(">");
        for (RecipeParameter r:recipes
             ) {
            REC=REC.concat('"'+String.valueOf(r.getId())+"\" ");
        }
        REC=REC.concat(">");

        boolean findComponent;
        for (IngredientParameter i:components
             ) {
            for (RecipeParameter r:recipes
                 ) {
                findComponent = false;
                for (Composition c:compositions
                     ) {
                    if (c.getIdRecipe()==r.getId()) {
                        if (c.getIngredientType().equals(i.getType())) {

                            findComponent = true;
                        }
                    }
                }
                if(!findComponent){
                    CONT = CONT.concat("0 ");
                }
                else
                    CONT = CONT.concat("1 ");
            }
            CONT = CONT.concat("\n");
        }
        Path copied = null;
        try {
            copied = Paths.get("Recipe.cdat");
            Path originalPath = Paths.get("ParametersSample");
            Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CONT = CONT.replaceAll("(\n)$",">");
        writeParameterInfile("<REC>",REC);
        writeParameterInfile("<ING>",ING);
        writeParameterInfile("<costs>",costs);
        writeParameterInfile("<CONT>",CONT);
        return new Parameters(REC,ING,costs,CONT);



    }
    private void writeParameterInfile(final String toFind, final String toWrite){
        try {
            Stream<String> lines = Files.lines(Paths.get("Recipe.cdat"));
            List<String> replaced = lines.map(line -> line.replaceAll(toFind, toWrite)).collect(Collectors.toList());
            Files.write(Paths.get("Recipe.cdat"), replaced);
            lines.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
