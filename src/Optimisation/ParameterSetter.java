package Optimisation;

public class ParameterSetter {


    private final DAO dao;

    public ParameterSetter(final DAO dao){
        this.dao = dao;
    }

    public String setREC(){

        dao.selectAllING();
        return "";

    }

}
