package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeController {

    @FXML
    private ListView<Recipe> recipeListView;

    private List<Recipe> recipes = new ArrayList<>();

    public void initialize() {
        Image image1 = new Image("path/to/image1.jpg");
        Image image2 = new Image("path/to/image2.jpg");
        recipes.add(new Recipe("Recipe 1", "Description 1", Arrays.asList("Ingredient 1", "Ingredient 2"), "Instructions 1", image1));
        recipes.add(new Recipe("Recipe 2", "Description 2", Arrays.asList("Ingredient 3", "Ingredient 4"), "Instructions 2", image2));
        recipeListView.getItems().addAll(recipes);
    }

    @FXML
    private void addRecipe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RecipeDetailsView.fxml"));
            Parent root = loader.load();
            RecipeDetailsController controller = loader.getController();
            controller.setRecipeController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Recipe");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeRecipe() {
        Recipe selectedRecipe = recipeListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            recipes.remove(selectedRecipe);
            recipeListView.getItems().remove(selectedRecipe);
        }
    }

    public void addRecipeToList(Recipe recipe) {
        recipes.add(recipe);
        recipeListView.getItems().add(recipe);
    }
}