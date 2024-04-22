package newTry;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Maine extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f4f4f4;");
layout.setAlignment(Pos.CENTER);
        Button orderbt = new Button("Orders Table");
        Button carbt = new Button("Cars Table");
        Button addressbt = new Button("Addresses Table");
        Button carPartbt = new Button("Car Parts Table");
        Button customerbt = new Button("Customers Table");
        Button devicebt = new Button("Devices Table");
        Button manubt = new Button("Manufacturers Table");
        
        Button about = new Button("About Me ");
        applyButtonStyle(orderbt);
        applyButtonStyle(carbt);
        applyButtonStyle(addressbt);
        applyButtonStyle(carPartbt);
        applyButtonStyle(customerbt);
        applyButtonStyle(devicebt);
        applyButtonStyle(manubt);
        applyButtonStyle(about);
        orderbt.setOnAction(e -> openTable(new Order(), "Orders Table"));
        carbt.setOnAction(e -> openTable(new Car(), "Cars Table"));
        addressbt.setOnAction(e -> openTable(new Address(), "Addresses Table"));
        carPartbt.setOnAction(e -> openTable(new Car_Part(), "Car Parts Table"));
        customerbt.setOnAction(e -> openTable(new Customer(), "Customers Table"));
        devicebt.setOnAction(e -> openTable(new Device(), "Devices Table"));
        manubt.setOnAction(e -> openTable(new Manufacture(), "Manufacturers Table"));
        about.setOnAction(e -> openTable(new AboutMe(), "About Me"));
        
        layout.getChildren().addAll(orderbt, carbt, addressbt, carPartbt, customerbt, devicebt, manubt,about);

        Scene scene = new Scene(layout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Database Tables");
        primaryStage.show();
    }

    private void openTable(Application app, String tabName) {
        Stage stage = new Stage();
        try {
            app.start(stage);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void applyButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-height: 40px; -fx-pref-width: 200px; -fx-border-radius: 10px;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
