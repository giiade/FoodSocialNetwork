package se.FSN.foodsocialnetwork;

import java.util.ArrayList;

/**
 * This class will be used to provide Recipe Objects to the ListView after parsing the Json.
 */
public class Recipe {
    private String title;
    private String imageurl;
    private int time;
    private ArrayList<String> ingredients;
    private ArrayList<String> tools;
    private String description;
    private ArrayList<String> categories;

    public Recipe(String name, String image, int time, ArrayList<String> ingredients, ArrayList<String> tools, String description, ArrayList<String> categories) {
        this.setTitle(name);
        this.setImageurl(image);
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
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
}
