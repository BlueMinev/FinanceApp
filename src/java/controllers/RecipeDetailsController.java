package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.Recipe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsController {

    @FXML
    private TextField recipeNameField;

    @FXML
    private ImageView recipeImageView;

    @FXML
    private TextArea ingredientsTextArea;

    @FXML
    private TextArea instructionsTextArea;

    private RecipeController recipeController;
    private File imageFile;

    public void setRecipeController(RecipeController recipeController) {
        this.recipeController = recipeController;
    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Recipe Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        imageFile = fileChooser.showOpenDialog(recipeImageView.getScene().getWindow());
        if (imageFile != null) {
            Image image = new Image(imageFile.toURI().toString());
            recipeImageView.setImage(image);
        }
    }

    @FXML
    private void saveRecipe() {
        String recipeName = recipeNameField.getText();
        List<String> ingredients = new ArrayList<>();
        for (String ingredient : ingredientsTextArea.getText().split("\n")) {
            ingredients.add(ingredient.trim());
        }
        String instructions = instructionsTextArea.getText();
        Image image = recipeImageView.getImage();

        Recipe recipe = new Recipe(recipeName, null, ingredients, instructions, image);
        recipeController.addRecipeToList(recipe);
        clearFields();
    }

    private void clearFields() {
        recipeNameField.clear();
        recipeImageView.setImage(null);
        ingredientsTextArea.clear();
        instructionsTextArea.clear();
        imageFile = null;
    }
}