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

import newTry.Manufacture.ManuModel;

public class Manufacture extends Application {

	private TableView<ManuModel> tableview;
    private ObservableList<ManuModel> data;
    Connection con;

	public static void main(String[] args) {
		launch(args);
	}

	public void buildData() {
		tableview.getColumns().clear(); // Clear existing columns before adding new ones

		  // Define table columns with CellValueFactory for all columns
        TableColumn<ManuModel, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new Callback<CellDataFeatures<ManuModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ManuModel, String> param) {
                return param.getValue().getName();
            }
        });

        TableColumn<ManuModel, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new Callback<CellDataFeatures<ManuModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ManuModel, String> param) {
                return param.getValue().getType();
            }
        });

        
        
        
        TableColumn<ManuModel, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new Callback<CellDataFeatures<ManuModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ManuModel, String> param) {
                return param.getValue().getCity();
            }
        });
        
        TableColumn<ManuModel, String> countryCol = new TableColumn<>("country");
        countryCol.setCellValueFactory(new Callback<CellDataFeatures<ManuModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ManuModel, String> param) {
                return param.getValue().getCountry();
            }
        });

      

       

        tableview.getColumns().addAll(nameCol, typeCol, cityCol, countryCol);

        data = FXCollections.observableArrayList();
        try {
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from manufacture");

            while (rs.next()) {
                data.add(new ManuModel(rs.getString("name"), rs.getString("type"),
                        rs.getString("city"), rs.getString("country")));
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
		Label label = new Label("Manufacture Table");
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
		
		
		 // Create input fields and buttons
        TextField nameField = new TextField();
        TextField typeField = new TextField();
        TextField cityField = new TextField();
        TextField countryField = new TextField();
	        
        // Create labels
        Label nameLabel = new Label("Name:");
        Label typeLabel = new Label("Type:");
        Label cityLabel = new Label("City:");
        Label countryLabel = new Label("Country:");

        // Set font and style for labels
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        typeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        cityLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        countryLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, nameLabel, nameField);
        gridPane.addRow(1, typeLabel, typeField);
        gridPane.addRow(2, cityLabel, cityField);
        gridPane.addRow(3, countryLabel, countryField);

        VBox leftVBox = new VBox(gridPane);
        leftVBox.setAlignment(Pos.CENTER);
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
             String type = typeField.getText();
             String city = cityField.getText();
             String country = countryField.getText();
             searchManu(name, type, city, country);
		});
		return gridPane;
	}
	 private void searchManu(String name, String type, String city, String country) {
	        try {
	            Statement stmt = con.createStatement();
	            String query = "SELECT * FROM manufacture WHERE 1=1";

	            if (!name.isEmpty()) {
	                query += " AND name = '" + name + "'";
	            }
	            if (!type.isEmpty()) {
	                query += " AND type = '" + type + "'";
	            }
	            if (!city.isEmpty()) {
	                query += " AND city = '" + city + "'";
	            }
	            if (!country.isEmpty()) {
	                query += " AND country = '" + country + "'";
	            }

	            ResultSet rs = stmt.executeQuery(query);
	            ObservableList<ManuModel> searchResult = FXCollections.observableArrayList();
	            while (rs.next()) {
	                searchResult.add(new ManuModel(rs.getString("name"), rs.getString("type"),
	                        rs.getString("city"), rs.getString("country")));
	            }

	            tableview.setItems(searchResult); // Set the search results to the TableView

	            rs.close();
	            stmt.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error searching Manufacture");
	        }
	    }


	public GridPane Insert() {
		 // Create input fields and buttons
        TextField nameField = new TextField();
        TextField typeField = new TextField();
        TextField cityField = new TextField();
        TextField countryField = new TextField();
	        
        // Create labels
        Label nameLabel = new Label("Name:");
        Label typeLabel = new Label("Type:");
        Label cityLabel = new Label("City:");
        Label countryLabel = new Label("Country:");

        // Set font and style for labels
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        typeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        cityLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        countryLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, nameLabel, nameField);
        gridPane.addRow(1, typeLabel, typeField);
        gridPane.addRow(2, cityLabel, cityField);
        gridPane.addRow(3, countryLabel, countryField);

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
             String type = typeField.getText();
             String city = cityField.getText();
             String country = countryField.getText();
             insertManu(name, type, city, country);
        });
		
		
		
		 
		return gridPane;
	}
	private void insertManu(String name, String type, String city, String country) {
        try {
            Statement stmt = con.createStatement();
            String query = "INSERT INTO manufacture (name, type, city, country) VALUES ('" +
                            name + "', '" + type + "', '" + city + "', '" + country + "')";
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
            	
            	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                
                successAlert.setContentText("Manufacture inserted successfully.");

                successAlert.showAndWait();
            	
            	
                buildData();
            } else {
            	
            	
            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                
                errorAlert.setContentText("Manufacture insertion failed.");

                errorAlert.showAndWait();
     
               
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error inserting Manufacture");
        }
    }
	
	public GridPane Update() {
ComboBox<String> nameField = new ComboBox<>();

        
        List<String> Manunames =fetchMnuName();
        nameField.getItems().addAll(Manunames);
        
		 // Create input fields and buttons
       
        TextField typeField = new TextField();
        TextField cityField = new TextField();
        TextField countryField = new TextField();
	        
        // Create labels
        Label nameLabel = new Label("Name:");
        Label typeLabel = new Label("Type:");
        Label cityLabel = new Label("City:");
        Label countryLabel = new Label("Country:");

        // Set font and style for labels
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        typeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        cityLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        countryLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, nameLabel, nameField);
        gridPane.addRow(1, typeLabel, typeField);
        gridPane.addRow(2, cityLabel, cityField);
        gridPane.addRow(3, countryLabel, countryField);

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
				//  String name = nameField.getValue();
				  

		             String name = "";
			            if (nameField.getValue() == null) {
			            	
			            	nameField.getPromptText();
			            	
			            }
			            else {
			            	name = nameField.getSelectionModel().getSelectedItem().toString();}
		             String type = typeField.getText();
		             String city = cityField.getText();
		             String country = countryField.getText();
		             String sql = "UPDATE manufacture";
		 			try {
		 			Class.forName("com.mysql.jdbc.Driver");

		 			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");

		 			Statement stmt = con.createStatement();
		 			
		 			
		 			if (name.isEmpty() && type.isEmpty() && city.isEmpty() && country.isEmpty()) {
		 				sql = "select * from manufacture";
		 			} 
		 				
		 				
		 				else {
		 				sql += " SET ";
		 				if (!(type.isEmpty())) {
		 					sql += "type = '" + type + "', ";
		 				}
		 				if (!(city.isEmpty())) {
		 					sql += "city = '" + city + "', ";
		 				}
		 				if (!(country.isEmpty())) {
		 					sql += "country = '" + country + "', ";
		 				}
		 				if(name.isEmpty()) {
		 					Alert alert = new Alert(Alert.AlertType.WARNING);
		 					alert.setTitle("Warning");
		 					alert.setContentText("The Process does not work Because You did not enter the name to change.");
		 					alert.showAndWait();
		 					sql = "select * from manufacture";
		 				}
		 				 if (sql.endsWith(", ")) {
		     		            sql = sql.substring(0, sql.length() - 2); // Remove the trailing comma and space
		     		        }

		     		        if (!name.isEmpty()) {
		     		            sql += " WHERE name = '" + name + "';";
		     		        }
		     		       System.out.println(sql);
		     		        stmt.executeUpdate(sql);
		     		       Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
		     	            successAlert.setTitle("Success");
		     	            
		     	            successAlert.setContentText("Manufacture updated successfully!");

		     	            successAlert.showAndWait();
		 			}
	     		    stmt.close();
	     		    con.close();
		 			
		 			} catch (Exception e1) {
		     		    e1.printStackTrace();
		     		}
		             buildData();
				});
				return gridPane;
			}
	

	public GridPane Delete() {
		
        ComboBox<String> nameField = new ComboBox<>();

        
        List<String> Manunames =fetchMnuName();
        nameField.getItems().addAll(Manunames);
        
        
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
			  deleteManu(name);
		});
		return gridPane;
	}
	 private void deleteManu(String name) {
	        try {
	            Statement stmt = con.createStatement();

	           

	           
	                // Now that associated records in car_part are deleted, delete the car
	                String deleteManuQuery = "DELETE FROM manufacture WHERE name = '" + name + "'";
	                int rowsAffectedManufacture = stmt.executeUpdate(deleteManuQuery);

	                if (rowsAffectedManufacture > 0) {
	                	
	                	
	                	
	                	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
	                    successAlert.setTitle("Success");
	                    
	                    successAlert.setContentText("Manufacture deleted successfully.");

	                    successAlert.showAndWait();
	           
	                    // Refresh TableView or rebuild data after deletion
	                    buildData();
	                } else {
	                	
	                	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	                    errorAlert.setTitle("Error");
	                    
	                    errorAlert.setContentText("Manufacture not found or couldn't be deleted.");

	                    errorAlert.showAndWait();
	         
	                    System.out.println("Manufacture not found or couldn't be deleted.");
	                }
	            
	            stmt.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error deleting Manufacture");
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

	public class ManuModel {
        private final SimpleStringProperty name;
        private final SimpleStringProperty type;
        private final SimpleStringProperty city;
        private final SimpleStringProperty country;
        
        
        
        
        
        
		public ManuModel(String name, String type, String city,
				String country) {
			super();
			this.name = new SimpleStringProperty(name);
			this.type = new SimpleStringProperty(type);
			this.city = new SimpleStringProperty(city);
			this.country = new SimpleStringProperty(country);
		}
		public SimpleStringProperty getName() {
			return name;
		}
		public SimpleStringProperty getType() {
			return type;
		}
		public SimpleStringProperty getCity() {
			return city;
		}
		public SimpleStringProperty getCountry() {
			return country;
		}

      
    }
	
	
	
	
	 
	
}
