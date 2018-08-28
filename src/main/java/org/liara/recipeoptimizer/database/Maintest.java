package org.liara.recipeoptimizer.database;

import jCMPL.*;

public class Maintest {
    public static void main(String[] args){
        try {
            Cmpl m = new Cmpl("/tmp/Recipe.cmpl");
            m.connect("http://192.168.1.203:4000");
            System.out.println("Working Directory = " +
                    System.getProperty("user.dir"));
            m.debug(true);
            m.solve();


        } catch (CmplException e) {
            e.printStackTrace();
        }

    }
}
