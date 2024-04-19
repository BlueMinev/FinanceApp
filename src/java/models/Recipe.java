package models;

import javafx.scene.image.Image;

import java.util.List;

public class Recipe {
    private String name;
    private String description;
    private List<String> ingredients;
    private String instructions;
    private Image image;

    public Recipe() {
        // Default constructor
    }

    public Recipe(String name, String description, List<String> ingredients, String instructions, Image image) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.image = image;
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name;
    }
}