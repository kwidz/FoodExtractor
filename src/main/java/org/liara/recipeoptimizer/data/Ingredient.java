package org.liara.recipeoptimizer.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "price", "brand" })
public class Ingredient {


    private String name, quantite, type, brand;
    private float price;


    public Ingredient(String name, String quantite, String brand, float price){
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

    public String getQuantite() {
        return quantite;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public float getPrice() {
        return price;
    }

    public String toString(){
        return "nom : "+this.name+" type : "+this.type+" \n";
    }
}
