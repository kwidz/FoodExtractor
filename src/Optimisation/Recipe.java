package Optimisation;

public class Recipe {
    private final int id;
    private final String name;

    @Override
    public String toString() {
        return "Recipes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public Recipe(int id, String name) {

        this.id = id;
        this.name = name;
    }
}
