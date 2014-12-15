package se.FSN.foodsocialnetwork;

/**
 * Created by JulioLopez on 15/12/14.
 */
public class Ingredient {

    private String title;
    private String quantity;
    private String inputType;

    public Ingredient() {
    }

    public Ingredient(String title, String quantity, String type) {
        this.setTitle(title);
        this.setQuantity(quantity);
        this.setInputType(type);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
}
