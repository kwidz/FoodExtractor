package ExtracteurIngredients;

public class Ingredient {


    String name, quantite, type, brand, price;


    public Ingredient(String name, String quantite, String brand, String price){
        this.name = name;
        this.quantite= quantite;

        this.price=price;
        this.brand=brand;
    }

    public void setType(String classify) {
        this.type=classify;
    }
    public String getName(){
        return name;
    }
    public String toString(){
        return "nom : "+this.name+" type : "+this.type+" \n";
    }
}
