package se.FSN.foodsocialnetwork;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import se.FSN.foodsocialnetwork.utils.UsefulFunctions;

/**
 * Class use to handle the ingredients.
 * It contains String title;String quantity;
 * String inputType; Boolean isOptional;
 */
public class Ingredient implements Serializable {

    private String title;
    private String quantity;
    private String inputType;
    private boolean isOptional;

    public Ingredient() {
    }

    /**
     * @param title      String containing the name of the ingredient.
     * @param quantity   String containing a number.
     * @param type       String Containing the type
     * @param isOptional Boolean that show if is optional(true) or is mandatory(False)
     */
    public Ingredient(String title, String quantity, String type, boolean isOptional) {
        this.setTitle(title);
        this.setQuantity(quantity);
        this.setInputType(type);
        this.setOptional(isOptional);
        ;
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

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        String title = new String();
        try {
            title = URLEncoder.encode(this.getTitle(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        text.append("{");
        text.append(UsefulFunctions.INGREDIENTNAME_KEY + ":" + '"' + title + '"' + ",");
        text.append(UsefulFunctions.OPTIONAL_KEY + ":" + this.isOptional() + ",");
        text.append(UsefulFunctions.AMOUNT_KEY + ":" + this.getQuantity() + ",");
        text.append(UsefulFunctions.AMOUNTTYPE_KEY + ":" + '"' + this.getInputType() + '"');
        text.append("}");
        return text.toString();
    }
}
