package se.FSN.foodsocialnetwork;

import java.util.ArrayList;

/**
 * This class will be used to provide Recipe Objects to the ListView after parsing the Json.
 */
public class Recipe {
    private String ID;
    private String title;
    private String creator;
    private String imageUrl;
    private int time;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> tools;
    private String description;
    private ArrayList<String> categories;

    public Recipe() {
    }


    public Recipe(String ID, String name, String creator, String image, int time, ArrayList<Ingredient> ingredients, ArrayList<String> tools, String description, ArrayList<String> categories) {
        this.setID(ID);
        this.setTitle(name);
        this.setCreator(creator);
        this.setImageUrl(image);
        this.setTime(time);
        this.setIngredients(ingredients);
        this.setTools(tools);
        this.setDescription(description);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<String> getTools() {
        return tools;
    }

    public void setTools(ArrayList<String> tools) {
        this.tools = tools;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
