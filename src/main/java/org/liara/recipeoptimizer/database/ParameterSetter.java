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

    public Parameters parameterFile(Epicerie epicerie, List<RecipeParameter> forbiddenRecipes, List<String> freeComponents, int[] balance){

        ArrayList<IngredientParameter> components = dao.selectAllING(epicerie);
        ArrayList<RecipeParameter> recipes = dao.selectAllREC();
        ArrayList<Composition> compositions = dao.selectAllComposition();
        System.out.println(components);
        String REC = "<";
        String ING = "<";
        String costs = "<";
        String CONT = "<";

        String nutrChick = "<";
        String nutrBoeuf= "<";
        String nutrPorc= "<";
        String nutrFish= "<";
        String nutrVegan= "<";
        int balanceVegan = balance[0];
        int balanceBoeuf = balance[1];
        int balancePorc = balance[2];
        int balanceChick = balance[3];
        int balanceFish = balance[4];

        for (IngredientParameter c:components
             ) {

            ING=ING.concat('\"'+String.valueOf(c.getId())+"\" ");
            if(freeComponents.contains(c.getType())){
                costs=costs.concat(String.valueOf(0f+" "));
            }else
                costs=costs.concat(String.valueOf(c.getPrice())+" ");

        }
        ING=ING.concat(">");
        costs=costs.concat(">");
        //make parameters for each recipes
        for (RecipeParameter r:recipes
             ) {
            if(!forbiddenRecipes.contains(r)) {

                REC = REC.concat('"' + String.valueOf(r.getId()) + "\" ");
                //setup parametres for nutrFish parameter in optimization module
                if (r.getType().equals("poisson")) {
                    nutrFish = nutrFish.concat("1 ");
                } else {
                    nutrFish = nutrFish.concat("0 ");
                }
                //setup parametres for nutrChicken parameter in optimization module
                if (r.getType().equals("poulet")) {
                    nutrChick = nutrChick.concat("1 ");
                } else {
                    nutrChick = nutrChick.concat("0 ");
                }
                //setup parametres for nutrBoeuf parameter in optimization module
                if (r.getType().equals("boeuf")) {
                    nutrBoeuf = nutrBoeuf.concat("1 ");
                } else {
                    nutrBoeuf = nutrBoeuf.concat("0 ");
                }
                //setup parametres for nutrPorc parameter in optimization module
                if (r.getType().equals("porc")) {
                    nutrPorc = nutrPorc.concat("1 ");
                } else {
                    nutrPorc = nutrPorc.concat("0 ");
                }
                //setup parametres for nutrVegan parameter in optimization module
                if (r.getType().equals("vegan")) {
                    nutrVegan = nutrVegan.concat("1 ");
                } else {
                    nutrVegan = nutrVegan.concat("0 ");
                }
            }


        }
        REC=REC.concat(">");
        nutrBoeuf=nutrBoeuf.concat(">");
        nutrChick=nutrChick.concat(">");
        nutrFish=nutrFish.concat(">");
        nutrPorc=nutrPorc.concat(">");
        nutrVegan=nutrVegan.concat(">");

        boolean findComponent;
        for (IngredientParameter i:components
             ) {
            for (RecipeParameter r:recipes
                 ) {
                if (!forbiddenRecipes.contains(r)) {
                    findComponent = false;
                    for (Composition c : compositions
                    ) {
                        if (c.getIdRecipe() == r.getId()) {
                            if (c.getIngredientType().equals(i.getType())) {

                                findComponent = true;
                            }
                        }
                    }
                    if (!findComponent) {
                        CONT = CONT.concat("0 ");
                    } else
                        CONT = CONT.concat("1 ");
                }
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
        writeParameterInfile("<NUTRCHICK>",nutrChick);
        writeParameterInfile("<NUTRBOEUF>",nutrBoeuf);
        writeParameterInfile("<NUTRPORC>",nutrPorc);
        writeParameterInfile("<NUTRFISH>",nutrFish);
        writeParameterInfile("<NUTRVEGAN>",nutrVegan);
        writeParameterInfile("BALVEGAN", String.valueOf(balanceVegan));
        writeParameterInfile("BALBOEUF", String.valueOf(balanceBoeuf));
        writeParameterInfile("BALCHICK", String.valueOf(balanceChick));
        writeParameterInfile("BALFISH", String.valueOf(balanceFish));
        writeParameterInfile("BALPORC", String.valueOf(balancePorc));
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
