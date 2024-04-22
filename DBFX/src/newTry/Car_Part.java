package newTry;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import newTry.Car_Part.Car_PartModel;

public class Car_Part extends Application {

	 private TableView<Car_PartModel> tableview;
	    private ObservableList<Car_PartModel> data;
	    Connection con;

	public static void main(String[] args) {
		launch(args);
	}

	public void buildData() {
		tableview.getColumns().clear(); // Clear existing columns before adding new ones

		 // Define table columns with CellValueFactory for all columns
        TableColumn<Car_PartModel, String> CarCol = new TableColumn<>("Car");
        CarCol.setCellValueFactory(new Callback<CellDataFeatures<Car_PartModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Car_PartModel, String> param) {
                return param.getValue().getCar();
            }
        });

        TableColumn<Car_PartModel, String> partCol = new TableColumn<>("Part");
        partCol.setCellValueFactory(new Callback<CellDataFeatures<Car_PartModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Car_PartModel, String> param) {
                return param.getValue().getPart();
            }
        });
         
         tableview.getColumns().addAll(CarCol, partCol);

         data = FXCollections.observableArrayList();
         try {
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM car_part");

             while (rs.next()) {
                 data.add(new Car_PartModel(rs.getString("car"), rs.getString("part")));
             }

             tableview.setItems(data);
         } catch (Exception e) {
             e.printStackTrace();
             System.out.println("Error on Building Data");
         }
     }

	@Override
	public void start(Stage primaryStage) throws Exception {
		tableview = new TableView();
		buildData();
		
		
		
		
		HBox hbox = new HBox();
		Label label = new Label("Car_Part Table");
		label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));

		Button Search = new Button("Search");
		Search.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		Search.setStyle("-fx-background-radius: 20");

		Button Insert = new Button("Insert");
		Insert.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		Insert.setStyle("-fx-background-radius: 20");

		Button Update = new Button("Update");
		Update.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		Update.setStyle("-fx-background-radius: 20");

		Button Delete = new Button("Delete");
		Delete.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		Delete.setStyle("-fx-background-radius: 20");
		hbox.getChildren().addAll(Search, Insert, Update, Delete);
		hbox.setSpacing(20);
		hbox.setAlignment(Pos.CENTER);

		
		

		
		// The VBox containing the entire UI elements
        VBox root = new VBox();
        root.getChildren().addAll(label, hbox, tableview);
        root.setSpacing(50);
        tableview.setMaxWidth(390);
        tableview.setMaxHeight(200);

        // Adding event handlers to buttons to change the displayed UI
        Search.setOnAction(e -> {
            root.getChildren().clear();
            root.getChildren().addAll(label, hbox, tableview, Select());
        });
        Insert.setOnAction(e -> {
            root.getChildren().clear();
            root.getChildren().addAll(label, hbox, tableview, Insert());
        });
        Update.setOnAction(e -> {
            root.getChildren().clear();
            root.getChildren().addAll(label, hbox, tableview, Update());
        });
        Delete.setOnAction(e -> {
            root.getChildren().clear();
            root.getChildren().addAll(label, hbox, tableview, Delete());
        });
        root.setStyle("-fx-background-color:#8FBC8F;");
        root.setAlignment(Pos.CENTER);

        // Create the Scene with the root node
        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
		

	public GridPane Select() {
		
		
		 
        ComboBox<String> carField = new ComboBox<>();
        ComboBox<String> partField = new ComboBox<>();
        
        
        
        List<String> cars = fetchCarData();
        carField.getItems().addAll(cars);
        
        
        
        List<String> devices = fetchDeviceData();
        partField.getItems().addAll(devices);
        
        
	        

        // Create labels
        Label carLabel = new Label("Car:");
        Label partLabel = new Label("Part:");
        
        
        
        
        // Set font and style for labels
        carLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        partLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
       
        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, carLabel, carField);
        gridPane.addRow(1, partLabel, partField);
       

        VBox leftVBox = new VBox(gridPane);
        leftVBox.setPadding(new Insets(20));
        leftVBox.setSpacing(20);


		Button buttonSelect = new Button("Select Data");
		buttonSelect.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		buttonSelect.setStyle("-fx-background-radius: 20");
		buttonSelect.setStyle("-fx-background-radius: 20; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
		HBox hb = new HBox();
		hb.getChildren().addAll(buttonSelect);
		hb.setSpacing(20);
		gridPane.add(hb, 0, 10);

		buttonSelect.setOnAction(e -> {
			

			 String car = carField.getValue();
             String part = partField.getValue();
            
             searchCar(car, part);
		});
		return gridPane;
	}
	private void searchCar(String car, String part) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM car_part WHERE 1=1";
            
            if (car != null && !car.isEmpty()) {
                query += " AND car = '" + car + "'";
            }
            if (part != null && !part.isEmpty()) {
                query += " AND part = '" + part + "'";
            }
            

            ResultSet rs = stmt.executeQuery(query);
            ObservableList<Car_PartModel> searchResult = FXCollections.observableArrayList();
            while (rs.next()) {
                searchResult.add(new Car_PartModel(rs.getString("car"), rs.getString("part")));
            }

            tableview.setItems(searchResult); // Set the search results to the TableView

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error searching cars");
        }
    }

	public GridPane Insert() {
		ComboBox<String> carField = new ComboBox<>();
        ComboBox<String> partField = new ComboBox<>();
        
        
        
        List<String> cars = fetchCarData();
        carField.getItems().addAll(cars);
        
        
        
        List<String> devices = fetchDeviceData();
        partField.getItems().addAll(devices);
        
        
	        

        // Create labels
        Label carLabel = new Label("Car:");
        Label partLabel = new Label("Part:");
        
        
        
        
        // Set font and style for labels
        carLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        partLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
       
        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, carLabel, carField);
        gridPane.addRow(1, partLabel, partField);
       

        VBox leftVBox = new VBox(gridPane);
        leftVBox.setPadding(new Insets(20));
        leftVBox.setSpacing(20);

		Button buttonInsert = new Button("Insert Data");
		buttonInsert.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		buttonInsert.setStyle("-fx-background-radius: 20");
		buttonInsert.setStyle("-fx-background-radius: 20; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
		HBox hb = new HBox();
		hb.getChildren().addAll(buttonInsert);
		hb.setSpacing(20);
		gridPane.add(hb, 0, 10);
		buttonInsert.setOnAction(e -> {
			 String car = carField.getValue();
             String part = partField.getValue();
             insertCar(car, part);
        });
		
		
		
		 
		return gridPane;
	}
	private void insertCar(String car, String part) {
        try {
            Statement stmt = con.createStatement();
            String query = "INSERT INTO car_part (car, part) VALUES ('" +
                    car + "', '" + part + "')";
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
            	
            	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                
                successAlert.setContentText("Car part inserted successfully.");

                successAlert.showAndWait();
                System.out.println("Car part inserted successfully.");
                buildData(); // Refresh TableView or rebuild data after insertion
            } else {
            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                
                errorAlert.setContentText("Car part insertion failed.");

                errorAlert.showAndWait();
                System.out.println("Car part insertion failed.");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error inserting car part");
        }
    }
	public GridPane Update() {
		
		ComboBox<String> carField = new ComboBox<>();
        ComboBox<String> partField = new ComboBox<>();
        
        
        
        List<String> cars = fetchCarData();
        carField.getItems().addAll(cars);
        
        
        
        List<String> devices = fetchDeviceData();
        partField.getItems().addAll(devices);
        
        
	        

        // Create labels
        Label carLabel = new Label("Car:");
        Label partLabel = new Label("Part:");
        
        
        
        
        // Set font and style for labels
        carLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        partLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
       
        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, carLabel, carField);
        gridPane.addRow(1, partLabel, partField);
       

        VBox leftVBox = new VBox(gridPane);
        leftVBox.setPadding(new Insets(20));
        leftVBox.setSpacing(20);
        
			Button buttonUpdate = new Button("Update Data");
			buttonUpdate.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
			buttonUpdate.setStyle("-fx-background-radius: 20");
			buttonUpdate.setStyle("-fx-background-radius: 20; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
			HBox hb = new HBox();
			hb.getChildren().addAll(buttonUpdate);
			hb.setSpacing(20);
			gridPane.add(hb, 0,10);
			buttonUpdate.setOnAction(e -> {
				 String car = carField.getValue();
	             String part = partField.getValue();
	            
	             updateCar(car, part);
			});
			return gridPane;
		}
	
	private void updateCar(String car, String part) {
        try {
            Statement stmt = con.createStatement();
            String query = "UPDATE car_part SET part = '" + part + "' WHERE car = '" + car + "'";
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
            	
            	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                
                successAlert.setContentText("Car part updated successfully.");

                successAlert.showAndWait();
                System.out.println("Car part updated successfully.");
                buildData(); // Refresh TableView or rebuild data after update
            } else {
            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                
                errorAlert.setContentText("Car part not found or couldn't be updated.");

                errorAlert.showAndWait();
                System.out.println("Car part not found or couldn't be updated.");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating car part");
        }
    }
   
	public GridPane Delete() {
		
		ComboBox<String> carField = new ComboBox<>();
        ComboBox<String> partField = new ComboBox<>();
        
        
        
        List<String> cars = fetchCarData();
        carField.getItems().addAll(cars);
        
        
        
        List<String> devices = fetchDeviceData();
        partField.getItems().addAll(devices);
        
        
	        

        // Create labels
        Label carLabel = new Label("Car:");
        Label partLabel = new Label("Part:");
        
        
        
        
        // Set font and style for labels
        carLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        partLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
       
        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, carLabel, carField);
        gridPane.addRow(1, partLabel, partField);
       

        VBox leftVBox = new VBox(gridPane);
        leftVBox.setPadding(new Insets(20));
        leftVBox.setSpacing(20);
        
		Button buttonSelect = new Button("Delete Data");
		buttonSelect.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		buttonSelect.setStyle("-fx-background-radius: 20");
		buttonSelect.setStyle("-fx-background-radius: 20; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
		HBox hb = new HBox();
		hb.getChildren().addAll(buttonSelect);
		hb.setSpacing(20);
		gridPane.add(hb, 0,10);

		buttonSelect.setOnAction(e -> {
			 String car = carField.getValue();
             String part = partField.getValue();
             deleteCar(car,part);
		});
		return gridPane;
	}
	 private void deleteCar(String car,String part) {
	        try {
	            Statement stmt = con.createStatement();

	            // Delete associated records in the car_part table first
	            String deletePartsQuery = "DELETE FROM car_part WHERE car = '" + car + "'";
	            int rowsAffectedParts = stmt.executeUpdate(deletePartsQuery);

	            if (rowsAffectedParts >= 0) {
	                // Now that associated records in car_part are deleted, delete the car
	                String deleteCarQuery = "DELETE FROM car_part WHERE part = '" + part + "'";
	                int rowsAffectedCar = stmt.executeUpdate(deleteCarQuery);

	                if (rowsAffectedCar > 0) {
	                	
	                	
	                	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
	                    successAlert.setTitle("Success");
	                    
	                    successAlert.setContentText("Car deleted successfully.");

	                    successAlert.showAndWait();
	                    System.out.println("Car deleted successfully.");
	                    // Refresh TableView or rebuild data after deletion
	                    buildData();
	                } else {
	                	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	                    errorAlert.setTitle("Error");
	                    
	                    errorAlert.setContentText("Car not found or couldn't be deleted.");

	                    errorAlert.showAndWait();
	                    System.out.println("Car not found or couldn't be deleted.");
	                }
	            } else {
	            	
	            	
	            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	                errorAlert.setTitle("Error");
	                
	                errorAlert.setContentText("Error deleting associated car parts.");

	                errorAlert.showAndWait();
	                System.out.println("Error deleting associated car parts.");
	            }

	            stmt.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error deleting car");
	        }
	    }
	
	 public class Car_PartModel {
	        private final SimpleStringProperty car;
	        private final SimpleStringProperty part;
	       
	        public Car_PartModel(String car, String part) {
	            this.car = new SimpleStringProperty(car);
	            this.part = new SimpleStringProperty(part);
	        }

			public SimpleStringProperty getCar() {
				return car;
			}

			public SimpleStringProperty getPart() {
				return part;
			}

	     
	    }
	    
	    
	    
	    private List<String> fetchCarData() {
	        List<String> carList = new ArrayList<>();
	        try {
	          
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
	            Statement stmt = con.createStatement();

	            
	            ResultSet rs = stmt.executeQuery("SELECT name FROM car"); 
	        
	            while (rs.next()) {
	                String carModel = rs.getString("name"); 
	                carList.add(carModel);
	            }

	          
	            rs.close();
	            stmt.close();
	            con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error fetching car data");
	        }
	        return carList;
	    }
	    
	    
	    
	    
	    private List<String> fetchDeviceData() {
	        List<String> carList = new ArrayList<>();
	        try {
	          
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
	            Statement stmt = con.createStatement();

	            
	            ResultSet rs = stmt.executeQuery("SELECT no FROM device"); 
	        
	            while (rs.next()) {
	                String carModel = rs.getString("no"); 
	                carList.add(carModel);
	            }

	          
	            rs.close();
	            stmt.close();
	            con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error fetching device data");
	        }
	        return carList;
	    }
	
	
	 
	
}
