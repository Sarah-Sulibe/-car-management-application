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
import javafx.scene.Parent;
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
import newTry.Car.CarModel;

public class Car extends Application {

	 private TableView<CarModel> tableview;
	    private ObservableList<CarModel> data;
	    Connection con;

	public static void main(String[] args) {
		launch(args);
	}

	public void buildData() {
		tableview.getColumns().clear(); // Clear existing columns before adding new ones

	    TableColumn<CarModel, String> nameCol = new TableColumn<>("Name");
	    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

	    TableColumn<CarModel, String> modelCol = new TableColumn<>("Model");
	    modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

	    TableColumn<CarModel, String> yearCol = new TableColumn<>("Year");
	    yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

	    TableColumn<CarModel, String> madeCol = new TableColumn<>("Made");
	    madeCol.setCellValueFactory(new PropertyValueFactory<>("made"));

	    tableview.getColumns().addAll(nameCol, modelCol, yearCol, madeCol);

        data = FXCollections.observableArrayList();
        try {
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from car");

            while (rs.next()) {
                data.add(new CarModel(rs.getString("name"), rs.getString("model"),
                        rs.getString("year"), rs.getString("made")));
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
		Label label = new Label("Car Table");
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
		
		
		  TextField nameField = new TextField();
	        TextField modelField = new TextField();
	        TextField yearField = new TextField();
	        ComboBox<String> madeField = new ComboBox<>();

	        
	        List<String> manufactures =fetchMnuName();
	        madeField.getItems().addAll(manufactures);
	        
	        
	        Label nameLabel = new Label("Name:");
	        Label modelLabel = new Label("Model:");
	        Label yearLabel = new Label("Year:");
	        Label madeLabel = new Label("Made:");

	        // Set font and style for labels
	        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	        modelLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	        yearLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	        madeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

	        // Layout setup
	        GridPane gridPane = new GridPane();
	        gridPane.setHgap(10);
	        gridPane.setVgap(10);

	        gridPane.addRow(0, nameLabel, nameField);
	        gridPane.addRow(1, modelLabel, modelField);
	        gridPane.addRow(2, yearLabel, yearField);
	        gridPane.addRow(3, madeLabel, madeField);

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
			

			 String name = nameField.getText();
	            String model = modelField.getText();
	            String year = yearField.getText();
	            String made = madeField.getValue();
	            searchCar(name, model, year, made);
		});
		return gridPane;
	}
	private void searchCar(String name, String model, String year, String made) {
	    try {
	        Statement stmt = con.createStatement();
	        String query = "SELECT * FROM car WHERE 1=1";

	        if (!name.isEmpty()) {
	            query += " AND name = '" + name + "'";
	        }
	        if (!model.isEmpty()) {
	            query += " AND model = '" + model + "'";
	        }
	        if (!year.isEmpty()) {
	            query += " AND year = '" + year + "'";
	        }
	        
	        if (made != null && !made.isEmpty()) {
	            query += " AND made = '" + made + "'";
	        }

	        ResultSet rs = stmt.executeQuery(query);
	        ObservableList<CarModel> searchResult = FXCollections.observableArrayList();
	        while (rs.next()) {
	            searchResult.add(new CarModel(rs.getString("name"), rs.getString("model"),
	                    rs.getString("year"), rs.getString("made")));
	        }

	        if (name.isEmpty() && model.isEmpty() && year.isEmpty() && made == null) {
	            // If all search criteria are empty, load all data
	            buildData();
	        } else {
	            tableview.setItems(searchResult); // Set the search results to the TableView
	        }

	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Error searching cars");
	    }
	}


	public GridPane Insert() {
		 TextField nameField = new TextField();
	        TextField modelField = new TextField();
	        TextField yearField = new TextField();
	        ComboBox<String> madeField = new ComboBox<>();

	        
	        List<String> manufactures =fetchMnuName();
	        madeField.getItems().addAll(manufactures);
	        
	        
	        Label nameLabel = new Label("Name:");
	        Label modelLabel = new Label("Model:");
	        Label yearLabel = new Label("Year:");
	        Label madeLabel = new Label("Made:");

	        // Set font and style for labels
	        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	        modelLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	        yearLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	        madeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

	        // Layout setup
	        GridPane gridPane = new GridPane();
	        gridPane.setHgap(10);
	        gridPane.setVgap(10);

	        gridPane.addRow(0, nameLabel, nameField);
	        gridPane.addRow(1, modelLabel, modelField);
	        gridPane.addRow(2, yearLabel, yearField);
	        gridPane.addRow(3, madeLabel, madeField);

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
            String name = nameField.getText();
            String model = modelField.getText();
            String year = yearField.getText();
            String made = madeField.getValue();
            insertCar(name, model, year, made);
        });
		
		
		
		 
		return gridPane;
	}
	private void insertCar(String name, String model, String year, String made) {
        try {
            Statement stmt = con.createStatement();
            String query = "INSERT INTO car (name, model, year, made) VALUES ('" +
                            name + "', '" + model + "', '" + year + "', '" + made + "')";
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
            	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                
                successAlert.setContentText("Car inserted successfully.");

                successAlert.showAndWait();
                System.out.println("Car inserted successfully.");
                // Refresh TableView or rebuild data after insertion
                buildData();
            } else {
            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                
                errorAlert.setContentText("Car insertion failed");

                errorAlert.showAndWait();
                System.out.println("Car insertion failed.");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error inserting car");
        }
    }
	public GridPane Update() {
		
        TextField modelField = new TextField();
        TextField yearField = new TextField();
        ComboBox<String> madeField = new ComboBox<>();

        
        List<String> manufactures =fetchMnuName();
        madeField.getItems().addAll(manufactures);
        
ComboBox<String> nameField = new ComboBox<>();

        
        List<String> carnames =carNames();
        nameField.getItems().addAll(carnames);
        
        
        
        
        
        Label nameLabel = new Label("Name:");
        Label modelLabel = new Label("Model:");
        Label yearLabel = new Label("Year:");
        Label madeLabel = new Label("Made:");

        // Set font and style for labels
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        modelLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        yearLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        madeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, nameLabel, nameField);
        gridPane.addRow(1, modelLabel, modelField);
        gridPane.addRow(2, yearLabel, yearField);
        gridPane.addRow(3, madeLabel, madeField);

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
				 String name = nameField.getValue();
		            String model = modelField.getText();
		            String year = yearField.getText();
		            String made = "";
		            if (madeField.getValue() == null) {
		            	
		            	madeField.getPromptText();
		            	
		            }
		            else {
		            	made = madeField.getSelectionModel().getSelectedItem().toString();}
		            updateCar(name, model, year, made);
			});
			return gridPane;
		}
	
	private void updateCar(String name, String model, String year, String made) {
		String sql = "UPDATE car";
		try {
		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");

		Statement stmt = con.createStatement();
		
		
		if (name.isEmpty() && model.isEmpty() && year.isEmpty() && made.isEmpty()) {
			sql = "select * from car";
		} else {
			boolean yearBoolean = true;
			for (int i = 0; i < year.length(); i++) {
				if(!(Character.isDigit(year.charAt(i)))) {
					yearBoolean = false;
				}
			}
			if(!yearBoolean) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setContentText("The Process does not work Because The year must be numeric");
				alert.showAndWait();
			}
			else {
			sql += " SET ";
			if (!(model.isEmpty())) {
				sql += "model = " + model + " , ";
			}
			if (!(year.isEmpty())) {
				sql += "year = '" + year + "' , ";
			}
			if (!(made.isEmpty())) {
				sql += "made = '" + made + "' , ";
			}
			if(name.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setContentText("The Process does not work Because You did not enter the name to change.");
				alert.showAndWait();
				sql = "select * from car";
			}
		String[] splitArray = sql.split(" ");
		for (String string : splitArray) {
			System.out.println(string);
		}
		System.out.println("------------------");
		String[] newSQL = new String[splitArray.length - 1];
		if (splitArray[splitArray.length - 1].equalsIgnoreCase(",")) {
			for (int i = 0; i < newSQL.length; i++) {
				newSQL[i] = splitArray[i];
			}
			sql = "";

			for (String string : newSQL) {
				sql += string + " ";
			}
			if (!(name.isEmpty())) {
				sql += "Where name = '" + name + "';";
			}
			stmt.executeUpdate(sql);
			Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            
            successAlert.setContentText("Car updated successfully!");

            successAlert.showAndWait();
			buildData();
		}
		System.out.println(sql);
		
		}}}catch (Exception e1) {
			e1.printStackTrace();
		}
	
    }
	public GridPane Delete() {
		
        ComboBox<String> nameField = new ComboBox<>();

        
        List<String> carnames =carNames();
        nameField.getItems().addAll(carnames);
        
        
        Label nameLabel = new Label("Name:");
       
        // Set font and style for labels
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
       

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, nameLabel, nameField);
      

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
			  String name = nameField.getValue();
	            deleteCar(name);
		});
		return gridPane;
	}
	private void deleteCar(String name) {
        try {
            Statement stmt = con.createStatement();

            // Delete associated records in the car_part table first
            String deletePartsQuery = "DELETE FROM car_part WHERE car = '" + name + "'";
            int rowsAffectedParts = stmt.executeUpdate(deletePartsQuery);

            if (rowsAffectedParts >= 0) {
                // Now that associated records in car_part are deleted, delete the car
                String deleteCarQuery = "DELETE FROM car WHERE name = '" + name + "'";
                int rowsAffectedCar = stmt.executeUpdate(deleteCarQuery);

                if (rowsAffectedCar > 0) {
                	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    
                    successAlert.setContentText("Car deleted successfully.");

                    successAlert.showAndWait();
                   
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
                System.out.println("Error deleting associated car parts.");
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting car");
        }
    }

	
	private List<String> fetchMnuName() {
        List<String> manufactureList = new ArrayList<>();

        try {
            // Establish connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = con.createStatement();

            // Query to retrieve manufacturer data
            ResultSet rs = stmt.executeQuery("SELECT name FROM manufacture");

            // Iterate through the result set and add manufacturer names to the list
            while (rs.next()) {
                String manufacturerName = rs.getString("name");
                manufactureList.add(manufacturerName);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching manufacturer data");
        }
        return manufactureList;
    }

    private List<String> carNames() {
        List<String> carNameList = new ArrayList<>();

        try {
            // Establish connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = con.createStatement();

            // Query to retrieve car names
            ResultSet rs = stmt.executeQuery("SELECT name FROM car");

            // Iterate through the result set and add car names to the list
            while (rs.next()) {
                String carName = rs.getString("name");
                carNameList.add(carName);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching car names");
        }
        return carNameList;
    }

	public class CarModel {
        private final SimpleStringProperty name;
        private final SimpleStringProperty model;
        private final SimpleStringProperty year;
        private final SimpleStringProperty made;

        public CarModel(String name, String model, String year, String made) {
            this.name = new SimpleStringProperty(name);
            this.model = new SimpleStringProperty(model);
            this.year = new SimpleStringProperty(year);
            this.made = new SimpleStringProperty(made);
        }

        public String getName() {
            return name.get();
        }

        public String getModel() {
            return model.get();
        }

        public String getYear() {
            return year.get();
        }

        public String getMade() {
            return made.get();
        }
    }
    
	
	
	
	
	 
	
}
