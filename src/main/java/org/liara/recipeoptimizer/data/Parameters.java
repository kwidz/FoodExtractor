package org.liara.recipeoptimizer.data;

import java.awt.*;
import java.util.ArrayList;

public class Parameters {
    private ArrayList<Integer> REC =new ArrayList<>();
    private  ArrayList<Integer> ING=new ArrayList<>();
    private  ArrayList<Float> costs=new ArrayList<>();
    private  ArrayList<ArrayList> CONT=new ArrayList<>();

    public Parameters(String rec, String ing, String costs, String cont) {
        rec=rec.replace("<","");
        rec=rec.replace(">","");
        rec=rec.replace("\"","");
        for (String word:rec.split(" ")
             ) {
            REC.add(Integer.parseInt(word));

        }
        ing=ing.replace("<","");
        ing=ing.replace(">","");
        ing=ing.replace("\"","");
        for (String word:ing.split(" ")
        ) {
            ING.add(Integer.parseInt(word));

        }
        costs=costs.replace("<","");
        costs=costs.replace(">","");

        for (String word:costs.split(" ")
        ) {
            this.costs.add(Float.parseFloat(word));

        }

        cont=cont.replace("<","");
        cont=cont.replace(">","");

        for (String line:cont.split("\n")
        ) {
            ArrayList<Integer> a = new ArrayList<>();
            for (String word:line.split(" ")
                 ) {

                a.add(Integer.parseInt(word));
            }
            System.out.println(a);
            CONT.add(a);

        }
    }
    public ArrayList<Integer> getREC() {
        return REC;
    }

    public ArrayList<Integer> getING() {
        return ING;
    }

    public ArrayList<Float> getCosts() {
        return costs;
    }

    public ArrayList<ArrayList> getCONT() {
        return CONT;
    }
}
