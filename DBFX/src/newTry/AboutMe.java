package newTry;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AboutMe extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a label to display information about yourself
        Label aboutMeLabel = new Label("This database software was developed by Sarah Sulibe  "
        		+ "on 12/25/2023. And My Uni Number Is 202109705 ");
        aboutMeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 30));
        // Create a button to display the information
        Button showAboutMeButton = new Button("Show About Me");
        showAboutMeButton.setOnAction(e -> {
            // Show the information when the button is clicked
            if (!primaryStage.getScene().getRoot().getChildrenUnmodifiable().contains(aboutMeLabel)) {
                ((VBox) primaryStage.getScene().getRoot()).getChildren().add(aboutMeLabel);
            }
        });

        // Create a layout to organize the button and label
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().add(showAboutMeButton);
        root.setStyle("-fx-background-color:#8FBC8F;");
        // Set up the scene and stage
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("About Me");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
