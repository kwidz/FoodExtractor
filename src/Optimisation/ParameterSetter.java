package Optimisation;

import java.io.IOException;
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

    public void ParameterFile(){

        ArrayList<Ingredient> components = dao.selectAllING();
        ArrayList<Recipe> recipes = dao.selectAllREC();
        ArrayList<Composition> compositions = dao.selectAllComposition();
        System.out.println(components);
        String REC = "<";
        String ING = "<";
        String costs = "<";
        String CONT = "<";

        for (Ingredient c:components
             ) {

            ING=ING.concat('\"'+String.valueOf(c.getId())+"\" ");
            costs=costs.concat(String.valueOf(c.getPrice())+" ");

        }
        ING=ING.concat(">");
        costs=costs.concat(">");
        for (Recipe r:recipes
             ) {
            REC=REC.concat('"'+String.valueOf(r.getId())+"\" ");
        }
        REC=REC.concat(">");

        boolean findComponent;
        for (Ingredient i:components
             ) {
            for (Recipe r:recipes
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
        Path copied = Paths.get("/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/Optimisation/Repas.cdat");
        Path originalPath = Paths.get("/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/Optimisation/ParametersSample");
        try {
            Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CONT = CONT.replaceAll("(\n)$",">");
        writeParameterInfile("<REC>",REC);
        writeParameterInfile("<ING>",ING);
        writeParameterInfile("<costs>",costs);
        writeParameterInfile("<CONT>",CONT);




    }
    private void writeParameterInfile(final String toFind, final String toWrite){
        try {
            Path path = Paths.get("/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/Optimisation/Repas.cdat");

            Stream<String> lines = Files.lines(path);
            List<String> replaced = lines.map(line -> line.replaceAll(toFind, toWrite)).collect(Collectors.toList());
            Files.write(path, replaced);
            lines.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
