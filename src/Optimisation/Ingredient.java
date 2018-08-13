package Optimisation;

public class Ingredient {
    private final String type;
    private final float price;

    @Override
    public String toString() {
        return "Ingredient{" +
                "type='" + type + '\'' +
                ", price=" + price +
                ", id=" + id +
                '}';
    }

    public int getId() {
        return id;
    }

    private final int id;

    public String getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public Ingredient(final String type, final float price, int id) {
        this.type = type;
        this.price = price;
        this.id = id;
    }
}
