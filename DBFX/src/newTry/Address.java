package newTry;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
import newTry.Address.AddressModel;
import newTry.Car.CarModel;

public class Address extends Application {

	private TableView<AddressModel> tableview;
    private ObservableList<AddressModel> data;
    Connection con;

	public static void main(String[] args) {
		launch(args);
	}

	public void buildData() {
		
		tableview.getColumns().clear(); // Clear existing columns before adding new ones

		 TableColumn<AddressModel, String> IDCol = new TableColumn<>("ID");
         IDCol.setCellValueFactory(new Callback<CellDataFeatures<AddressModel, String>, ObservableValue<String>>() {
             public ObservableValue<String> call(CellDataFeatures<AddressModel, String> param) {
                 return param.getValue().getId();
             }
         });

         TableColumn<AddressModel, String> buidlingCol = new TableColumn<>("Buidling");
         buidlingCol.setCellValueFactory(new Callback<CellDataFeatures<AddressModel, String>, ObservableValue<String>>() {
             public ObservableValue<String> call(CellDataFeatures<AddressModel, String> param) {
                 return param.getValue().getBuidling();
             }
         });

         TableColumn<AddressModel, String> streetCol = new TableColumn<>("Street");
         streetCol.setCellValueFactory(new Callback<CellDataFeatures<AddressModel, String>, ObservableValue<String>>() {
             public ObservableValue<String> call(CellDataFeatures<AddressModel, String> param) {
                 return param.getValue().getStreet();
             }
         });

         TableColumn<AddressModel, String> cityCol = new TableColumn<>("City");
         cityCol.setCellValueFactory(new Callback<CellDataFeatures<AddressModel, String>, ObservableValue<String>>() {
             public ObservableValue<String> call(CellDataFeatures<AddressModel, String> param) {
                 return param.getValue().getCity();
             }
         });

         TableColumn<AddressModel, String> countryCol = new TableColumn<>("Country");
         countryCol.setCellValueFactory(new Callback<CellDataFeatures<AddressModel, String>, ObservableValue<String>>() {
             public ObservableValue<String> call(CellDataFeatures<AddressModel, String> param) {
                 return param.getValue().getCountry();
             }
         });

         tableview.getColumns().addAll(IDCol, buidlingCol, streetCol, cityCol, countryCol);
       //  System.out.print(("dfd");
         System.out.println("dfdf");
         data = FXCollections.observableArrayList();
         try {
              con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select * from address");

             while (rs.next()) {
                 data.add(new AddressModel(rs.getString("id"), rs.getString("buidling"),
                         rs.getString("street"), rs.getString("city"),rs.getString("country")));
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
		Label label = new Label("Address Table");
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
	       ComboBox<String> IDField = new ComboBox<>();
	         
	         
	         List<String> addresses = fetchAddress() ;
	         IDField.getItems().addAll(addresses);
	         
	         TextField buidlingField = new TextField();
	         TextField streetField = new TextField();
	         TextField cityField = new TextField();
	         TextField countryField = new TextField();
	      
	         // Create labels
	         Label idLabel = new Label("ID:");
	         Label buidlingLabel = new Label("Buidling:");
	         Label streetLabel = new Label("Street:");
	         Label cityLabel = new Label("City:");
	         Label countryLabel = new Label("Country:");
	         // Set font and style for labels
	         idLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	         buidlingLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	         streetLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	         cityLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	         countryLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
	        
	      // Layout setup
	         GridPane gridPane = new GridPane();
	    
	         gridPane.setHgap(10);
	         gridPane.setVgap(10);

	         gridPane.addRow(0, idLabel, IDField);
	         gridPane.addRow(1, buidlingLabel, buidlingField);
	         gridPane.addRow(2, streetLabel, streetField);
	         gridPane.addRow(3, cityLabel, cityField);
	         gridPane.addRow(4, countryLabel, countryField);

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
			
			 String id = IDField.getValue();
             String  buidling = buidlingField.getText();
             String street = streetField.getText();
             String city = cityField.getText();
             String country = countryField.getText();
             searchAddress(id, buidling, street, city,country);
		});
		return gridPane;
	}
	 private void searchAddress(String id, String buidling , String street, String  city,String   country) {
	        try {
	            Statement stmt = con.createStatement();
	            String query = "SELECT * FROM address WHERE 1=1";

	            if (id != null && !id.isEmpty()) {
	                query += " AND id = '" + id + "'";
	            }
	           
	            if (!buidling.isEmpty()) {
	                query += " AND buidling = '" + buidling + "'";
	            }
	            if (!street.isEmpty()) {
	                query += " AND street = '" + street + "'";
	            }
	            if (!city.isEmpty()) {
	                query += " AND city = '" + city + "'";
	            }
	            if (!country.isEmpty()) {
	                query += " AND country = '" + country + "'";
	            }
	            ResultSet rs = stmt.executeQuery(query);
	            ObservableList<AddressModel> searchResult = FXCollections.observableArrayList();
	            while (rs.next()) {
	                searchResult.add(new AddressModel(rs.getString("id"), rs.getString("buidling"),
	                        rs.getString("street"), rs.getString("city"),rs.getString("country")));
	            }

	            tableview.setItems(searchResult); // Set the search results to the TableView

	            rs.close();
	            stmt.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error searching address");
	        }
	    }

	public GridPane Insert() {
		
         
         TextField IDField = new TextField();
         TextField buidlingField = new TextField();
         TextField streetField = new TextField();
         TextField cityField = new TextField();
         TextField countryField = new TextField();
   
         // Create labels
         Label idLabel = new Label("ID:");
         Label buidlingLabel = new Label("Buidling:");
         Label streetLabel = new Label("Street:");
         Label cityLabel = new Label("City:");
         Label countryLabel = new Label("Country:");
         // Set font and style for labels
         idLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
         buidlingLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
         streetLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
         cityLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
         countryLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

    	
      // Layout setup
         GridPane gridPane = new GridPane();
      //   gridPane.setAlignment(Pos.CENTER); // Set alignment to center or Pos.CENTER
      //   
         gridPane.setHgap(10);
         gridPane.setVgap(10);

         gridPane.addRow(0, idLabel, IDField);
         gridPane.addRow(1, buidlingLabel, buidlingField);
         gridPane.addRow(2, streetLabel, streetField);
         gridPane.addRow(3, cityLabel, cityField);
         
         gridPane.addRow(4, countryLabel, countryField);
    	

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
			 String id = IDField.getText();
             String  buidling = buidlingField.getText();
             String street = streetField.getText();
             String city = cityField.getText();
             String country = countryField.getText();
             insertAddress(id, buidling, street, city,country);
        });
		
		
		
		 
		return gridPane;
	}
	private void insertAddress(String id, String buidling , String street, String  city,String   country) {
        try {
            Statement stmt = con.createStatement();
            String query = "INSERT INTO address (id, buidling, street, city ,country) VALUES ('" +
            		id + "', '" + buidling + "', '" + street + "', '" + city +"', ' "+country+ "')";
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
            	
            	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                
                successAlert.setContentText("Address inserted successfully.");

                successAlert.showAndWait();
            	
                System.out.println("Address inserted successfully.");
                // Refresh TableView or rebuild data after insertion
                buildData();
            } else {
            	
            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                
                errorAlert.setContentText("Address insertion failed.");

                errorAlert.showAndWait();
                System.out.println("Address insertion failed.");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error inserting Address");
        }
    }
	public GridPane Update() {
		
 ComboBox<String> IDField = new ComboBox<>();
         
         
         List<String> addresses = fetchAddress() ;
         IDField.getItems().addAll(addresses);
         
         TextField buidlingField = new TextField();
         TextField streetField = new TextField();
         TextField cityField = new TextField();
         TextField countryField = new TextField();
         
      // Set alignment of text fields to the right
       //  buidlingField.setAlignment(Pos.CENTER_RIGHT);
       //  streetField.setAlignment(Pos.CENTER_RIGHT);
       //  cityField.setAlignment(Pos.CENTER_RIGHT);
      //   countryField.setAlignment(Pos.CENTER_RIGHT);
        
         // Create labels
         Label idLabel = new Label("ID:");
         Label buidlingLabel = new Label("Buidling:");
         Label streetLabel = new Label("Street:");
         Label cityLabel = new Label("City:");
         Label countryLabel = new Label("Country:");
         // Set font and style for labels
         idLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
         buidlingLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
         streetLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
         cityLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
         countryLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

    	
      // Layout setup
         GridPane gridPane = new GridPane();
    //     gridPane.setAlignment(Pos.CENTER); // Set alignment to center or Pos.CENTER
         
         gridPane.setHgap(10);
         gridPane.setVgap(10);

         gridPane.addRow(0, idLabel, IDField);
         gridPane.addRow(1, buidlingLabel, buidlingField);
         gridPane.addRow(2, streetLabel, streetField);
         gridPane.addRow(3, cityLabel, cityField);
         
         gridPane.addRow(4, countryLabel, countryField);
    	

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
			    String id = IDField.getValue();
			    String buidling = buidlingField.getText();
			    String street = streetField.getText();
			    String city = cityField.getText();
			    String country = countryField.getText();

			    try {
			        Class.forName("com.mysql.cj.jdbc.Driver");
			        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
			        Statement stmt = con.createStatement();

			        String sql = "UPDATE address";

			        if (buidling.isEmpty() && street.isEmpty() && city.isEmpty() && country.isEmpty()) {
			            sql = "SELECT * FROM address";
			            System.out.println("No fields provided for update.");
			        } else {
			            sql += " SET ";
			            if (!buidling.isEmpty()) {
			                sql += "buidling = '" + buidling + "', ";
			            }
			            if (!street.isEmpty()) {
			                sql += "street = '" + street + "', ";
			            }
			            if (!city.isEmpty()) {
			                sql += "city = '" + city + "', ";
			            }
			            if (!country.isEmpty()) {
			                sql += "country = '" + country + "', ";
			            }
			            if (id.isEmpty()) {
			                Alert alert = new Alert(Alert.AlertType.WARNING);
			                alert.setTitle("Warning");
			                alert.setContentText("Please provide the address ID.");
			                alert.showAndWait();
			                sql = "SELECT * FROM address";
			            } else {
			                // Remove the trailing comma if it exists in the query
			                if (sql.endsWith(", ")) {
			                    sql = sql.substring(0, sql.length() - 2); // Remove the last two characters (", ")
			                }
			                sql += " WHERE id = " + id;
			                System.out.println(sql);
			                stmt.executeUpdate(sql);
			                
			                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
			                successAlert.setTitle("Success");
			                
			                successAlert.setContentText("Address updated successfully!.");

			                successAlert.showAndWait();
			                System.out.println("Address updated successfully!");
			            }
			        }

			        stmt.close();
			        con.close();
			    } catch (Exception e1) {
			        e1.printStackTrace();
			    }

			    buildData(); // Update UI after database changes
			});
			return gridPane;

		}
	
	
   
	public GridPane Delete() {
		
        ComboBox<String> idField = new ComboBox<>();

        
        List<String>idAdd =addressID();
        idField.getItems().addAll(idAdd);
        
        
        Label nameLabel = new Label("ID:");
       
        // Set font and style for labels
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
       
     // Layout setup
        GridPane gridPane = new GridPane();
     //   gridPane.setAlignment(Pos.CENTER); // Set alignment to center or Pos.CENTER
        
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, nameLabel, idField);
      

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
			  String id = idField.getValue();
			  deleteAddress(id);
		});
		return gridPane;
	}
	 private void deleteAddress(String id) {
	        try {
	            Statement stmt = con.createStatement();

	           
	           
	                // Now that associated records in car_part are deleted, delete the car
	                String deleteAddressQuery = "DELETE FROM address WHERE id = '" + id + "'";
	                int rowsAffectedAddress = stmt.executeUpdate(deleteAddressQuery);

	                if (rowsAffectedAddress > 0) {
	                	
	                	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
	                    successAlert.setTitle("Success");
	                    
	                    successAlert.setContentText("Address deleted successfully.");

	                    successAlert.showAndWait();
	                    System.out.println("Address deleted successfully.");
	                    // Refresh TableView or rebuild data after deletion
	                    buildData();
	                } else {
	                	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	                    errorAlert.setTitle("Error");
	                    
	                    errorAlert.setContentText("Address not found or couldn't be deleted.");

	                    errorAlert.showAndWait();
	                    System.out.println("Address not found or couldn't be deleted.");
	                }
	            

	            stmt.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error deleting Address");
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

    private List<String> addressID() {
        List<String> idList = new ArrayList<>();

        try {
            // Establish connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = con.createStatement();

            // Query to retrieve car names
            ResultSet rs = stmt.executeQuery("SELECT id FROM address");

            // Iterate through the result set and add car names to the list
            while (rs.next()) {
                String addID = rs.getString("id");
                idList.add(addID);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching car names");
        }
        return idList;
    }

    public class AddressModel {
        
	    private final SimpleStringProperty id;
	    private final SimpleStringProperty buidling; // Corrected column name
	    private final SimpleStringProperty street;
	    private final SimpleStringProperty city;
	    private final SimpleStringProperty country;
	    
	    public AddressModel(String id, String buidling, String street, String city, String country) {
	        this.id = new SimpleStringProperty(id);
	        this.buidling = new SimpleStringProperty(buidling); // Updated variable name
	        this.street = new SimpleStringProperty(street);
	        this.city = new SimpleStringProperty(city);
	        this.country = new SimpleStringProperty(country);
	    }
	public SimpleStringProperty getId() {
		return id;
	}
	public SimpleStringProperty getBuidling() {
        return buidling; // Updated getter method to return the correct property
    }
	public SimpleStringProperty getStreet() {
		return street;
	}
	public SimpleStringProperty getCity() {
		return city;
	}
	public SimpleStringProperty getCountry() {
		return country;
	}
    
    
    

}
private List<String> fetchAddress() {
    List<String> manufactureList = new ArrayList<>();
    try {
        // Establish connection
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
        Statement stmt = con.createStatement();

        // Query to retrieve customer data
        ResultSet rs = stmt.executeQuery("SELECT id FROM address"); 

        // Iterate through the result set and add customer names to the list
        while (rs.next()) {
            String customerName = rs.getString("id"); 
            manufactureList.add(customerName);
        }

        
        rs.close();
        stmt.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error fetching address data");
    }
    return manufactureList;
}  
	
	
	
	 
	
}
