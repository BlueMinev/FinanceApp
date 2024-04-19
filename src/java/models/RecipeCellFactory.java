package models;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class RecipeCellFactory extends ListCell<Recipe> {
    private final ImageView imageView = new ImageView();
    private final Text nameText = new Text();
    private final Text descriptionText = new Text();
    private final HBox hbox = new HBox(imageView, new HBox(nameText, descriptionText));

    public RecipeCellFactory() {
        super();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
        nameText.getStyleClass().add("recipe-name");
        descriptionText.getStyleClass().add("recipe-description");
        hbox.setSpacing(10);
    }

    @Override
    protected void updateItem(Recipe recipe, boolean empty) {
        super.updateItem(recipe, empty);

        if (empty || recipe == null) {
            setText(null);
            setGraphic(null);
        } else {
            imageView.setImage(recipe.getImage());
            nameText.setText(recipe.getName());
            descriptionText.setText(recipe.getDescription());
            setGraphic(hbox);
        }
    }

    public static Callback<ListView<Recipe>, ListCell<Recipe>> forListView() {
        return lv -> new RecipeCellFactory();
    }
}