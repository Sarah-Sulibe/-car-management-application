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

import newTry.Customer.CustModel;

public class Customer extends Application {

	private TableView<CustModel> tableview;
    private ObservableList<CustModel> data;
    Connection con;
	public static void main(String[] args) {
		launch(args);
	}

	public void buildData() {
		tableview.getColumns().clear(); // Clear existing columns before adding new ones

		 // Define table columns with CellValueFactory for all columns
        TableColumn<CustModel, String> IDCol = new TableColumn<>("ID");
        IDCol.setCellValueFactory(new Callback<CellDataFeatures<CustModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<CustModel, String> param) {
                return param.getValue().getId();
            }
        });

        TableColumn<CustModel, String> buidlingCol = new TableColumn<>("F_Name");
        buidlingCol.setCellValueFactory(new Callback<CellDataFeatures<CustModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<CustModel, String> param) {
                return param.getValue().getF_name();
            }
        });

        TableColumn<CustModel, String> streetCol = new TableColumn<>("L_Name");
        streetCol.setCellValueFactory(new Callback<CellDataFeatures<CustModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<CustModel, String> param) {
                return param.getValue().getL_name();
            }
        });

        TableColumn<CustModel, String> cityCol = new TableColumn<>("Address");
        cityCol.setCellValueFactory(new Callback<CellDataFeatures<CustModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<CustModel, String> param) {
                return param.getValue().getAddress();
            }
        });

        TableColumn<CustModel, String> countryCol = new TableColumn<>("Job");
        countryCol.setCellValueFactory(new Callback<CellDataFeatures<CustModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<CustModel, String> param) {
                return param.getValue().getJob();
            }
        });

        tableview.getColumns().addAll(IDCol, buidlingCol, streetCol, cityCol, countryCol);


        data = FXCollections.observableArrayList();
        try {
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customer");

            while (rs.next()) {
                data.add(new CustModel(rs.getString("id"), rs.getString("f_name"),
                        rs.getString("l_name"), rs.getString("address"),rs.getString("job")));
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
		Label label = new Label("Customer Table");
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
        TextField IDField = new TextField();
        TextField f_nameField = new TextField();
        TextField l_nameField = new TextField();
      
        TextField jobField = new TextField();

        ComboBox<String> addressField = new ComboBox<>();
        
        
        List<String> addresses = fetchAddress() ;
        addressField.getItems().addAll(addresses);
	        
	        
        // Create labels
        Label idLabel = new Label("ID:");
        Label f_nameLabel = new Label("F_name:");
        Label l_nameLabel = new Label("L_name:");
        Label addressLabel = new Label("Address:");
        Label jobLabel = new Label("Job:");
        // Set font and style for labels
        idLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        f_nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        l_nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        addressLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        jobLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));


        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, idLabel, IDField);
        gridPane.addRow(1, f_nameLabel, f_nameField);
        gridPane.addRow(2, l_nameLabel, l_nameField);
        gridPane.addRow(3, addressLabel, addressField);
        
        gridPane.addRow(4, jobLabel, jobField);
        
        
        VBox leftVBox = new VBox(gridPane);
        leftVBox.setPadding(new Insets(20));
        leftVBox.setSpacing(20);


		Button buttonSelect = new Button("Select Data");
		buttonSelect.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		buttonSelect.setStyle("-fx-background-radius: 20");
		buttonSelect.setStyle("-fx-background-radius: 20; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
		HBox hb = new HBox();
		hb.getChildren().addAll(buttonSelect);
		hb.setSpacing(30);
		gridPane.add(hb, 0, 10);

		buttonSelect.setOnAction(e -> {
			
			 String id = IDField.getText();
             String  f_name = f_nameField.getText();
             String l_name = l_nameField.getText();
             String address = addressField.getValue();
             String job = jobField.getText();
             searchCust(id, f_name, l_name, address,job);
		});
		return gridPane;
	}
    private void searchCust(String id, String f_name , String l_name, String  address,String   job) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM customer WHERE 1=1";

            if (!id.isEmpty()) {
                query += " AND id = '" + id + "'";
            }
            if (!f_name.isEmpty()) {
                query += " AND f_name = '" + f_name + "'";
            }
            if (!l_name.isEmpty()) {
                query += " AND l_name = '" + l_name + "'";
            }
            if (address != null && !address.isEmpty()) {
                query += " AND address = '" + address + "'";
            }
           
            
            if (!job.isEmpty()) {
                query += " AND job = '" + job + "'";
            }
            ResultSet rs = stmt.executeQuery(query);
            ObservableList<CustModel> searchResult = FXCollections.observableArrayList();
            while (rs.next()) {
                searchResult.add(new CustModel(rs.getString("id"), rs.getString("f_name"),
                        rs.getString("l_name"), rs.getString("address"),rs.getString("job")));
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
		  // Create input fields and buttons
        TextField IDField = new TextField();
        TextField f_nameField = new TextField();
        TextField l_nameField = new TextField();
      
        TextField jobField = new TextField();

        ComboBox<String> addressField = new ComboBox<>();
        
        
        List<String> addresses = fetchAddress() ;
        addressField.getItems().addAll(addresses);
	        
	        
        // Create labels
        Label idLabel = new Label("ID:");
        Label f_nameLabel = new Label("F_name:");
        Label l_nameLabel = new Label("L_name:");
        Label addressLabel = new Label("Address:");
        Label jobLabel = new Label("Job:");
        // Set font and style for labels
        idLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        f_nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        l_nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        addressLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        jobLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));


        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, idLabel, IDField);
        gridPane.addRow(1, f_nameLabel, f_nameField);
        gridPane.addRow(2, l_nameLabel, l_nameField);
        gridPane.addRow(3, addressLabel, addressField);
        
        gridPane.addRow(4, jobLabel, jobField);
        
        
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
             String  f_name = f_nameField.getText();
             String l_name = l_nameField.getText();
             String address = addressField.getValue();
             String job = jobField.getText();
             insertCust(id, f_name, l_name, address,job);
        });
		
		
		
		 
		return gridPane;
	}
	private void insertCust(String id, String f_name , String l_name, String  address,String   job) {
        try {
            Statement stmt = con.createStatement();
            String query = "INSERT INTO customer (id, f_name, l_name, address ,job) VALUES ('" +
            		id + "', '" + f_name + "', '" + l_name + "', '" + address +"', ' "+job+ "')";
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
            	
            	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                
                successAlert.setContentText("Customer inserted successfully.");

                successAlert.showAndWait();
                System.out.println("Customer inserted successfully.");
                // Refresh TableView or rebuild data after insertion
                buildData();
            } else {
            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                
                errorAlert.setContentText("Customer insertion failed.");

                errorAlert.showAndWait();
                System.out.println("Customer insertion failed.");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error inserting Customer");
        }
    }
	
	public GridPane Update() {
		 ComboBox<String> IDField = new ComboBox<>();

	        
	        List<String> custID =fetchcustID();
	        IDField.getItems().addAll(custID);
	        
		 // Create input fields and buttons
       
        TextField f_nameField = new TextField();
        TextField l_nameField = new TextField();
      
        TextField jobField = new TextField();

        ComboBox<String> addressField = new ComboBox<>();
        
        
        List<String> addresses = fetchAddress() ;
        addressField.getItems().addAll(addresses);
	        
	        
        // Create labels
        Label idLabel = new Label("ID:");
        Label f_nameLabel = new Label("F_name:");
        Label l_nameLabel = new Label("L_name:");
        Label addressLabel = new Label("Address:");
        Label jobLabel = new Label("Job:");
        // Set font and style for labels
        idLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        f_nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        l_nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        addressLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        jobLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));


        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, idLabel, IDField);
        gridPane.addRow(1, f_nameLabel, f_nameField);
        gridPane.addRow(2, l_nameLabel, l_nameField);
        gridPane.addRow(3, addressLabel, addressField);
        
        gridPane.addRow(4, jobLabel, jobField);
        
        
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
	             String  f_name = f_nameField.getText();
	             String lName = l_nameField.getText();
	           //  String address = addressField.getValue();
	             
	             
	             String addressId = "";
		            if (addressField.getValue() == null) {
		            	
		            	addressField.getPromptText();
		            	
		            }
		            else {
		            	addressId = addressField.getSelectionModel().getSelectedItem().toString();}
		            
	             String job = jobField.getText();
	             try {
	     		    Class.forName("com.mysql.cj.jdbc.Driver");

	     		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
	     		    Statement stmt = con.createStatement();
	     		    String sql = "UPDATE customer";

	     		    if (f_name.isEmpty() && lName.isEmpty() && addressId.isEmpty() && job.isEmpty()) {
	     		        sql = "SELECT * FROM customer";
	     		    } else {
	     		        sql += " SET ";
	     		        if (!f_name.isEmpty()) {
	     		            sql += "f_name = '" + f_name + "', ";
	     		        }
	     		        if (!lName.isEmpty()) {
	     		            sql += "l_name = '" + lName + "', ";
	     		        }
	     		        if (!addressId.isEmpty()) {
	     		            sql += "address = " + addressId + ", ";
	     		        }
	     		        if (!job.isEmpty()) {
	     		            sql += "job = '" + job + "', ";
	     		        }
	     		        if (id.isEmpty()) {
	     		            Alert alert = new Alert(Alert.AlertType.WARNING);
	     		            alert.setTitle("Warning");
	     		            alert.setContentText(" provide the customer ID.");
	     		            alert.showAndWait();
	     		            sql = "SELECT * FROM customer";
	     		        }

	     		        if (sql.endsWith(", ")) {
	     		            sql = sql.substring(0, sql.length() - 2); // Remove the trailing comma and space
	     		        }

	     		        if (!id.isEmpty()) {
	     		            sql += " WHERE id = " + id;
	     		        }
	     		       System.out.println(sql);
	     		        stmt.executeUpdate(sql);
	     		        // Assuming buildData() method is used to update UI after database changes
	     		       Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
	     	            successAlert.setTitle("Success");
	     	            
	     	            successAlert.setContentText("Customer updated successfully!");

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
		
        ComboBox<String> idField = new ComboBox<>();

        
        List<String> custID =fetchcustID();
        idField.getItems().addAll(custID);
        
        
        Label nameLabel = new Label("ID:");
       
        // Set font and style for labels
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
       

        // Layout setup
        GridPane gridPane = new GridPane();
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
			  deleteCust(id);
		});
		return gridPane;
	}
	private void deleteCust(String id) {
        try {
            Statement stmt = con.createStatement();

           
           
                // Now that associated records in car_part are deleted, delete the car
                String deleteAddressQuery = "DELETE FROM customer WHERE id = '" + id + "'";
                int rowsAffectedAddress = stmt.executeUpdate(deleteAddressQuery);

                if (rowsAffectedAddress > 0) {
                	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    
                    successAlert.setContentText("Customer deleted successfully.");

                    successAlert.showAndWait();
                    System.out.println("Customer deleted successfully.");
                    // Refresh TableView or rebuild data after deletion
                    buildData();
                } else {
                	
                	
                	
                	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    
                    errorAlert.setContentText("Customer not found or couldn't be deleted.");

                    errorAlert.showAndWait();
                    System.out.println("Customer not found or couldn't be deleted.");
                }
            

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting Customer");
        }
    }
	
public class CustModel {
        
	    private final SimpleStringProperty id;
	    private final SimpleStringProperty f_name; // Corrected column name
	    private final SimpleStringProperty l_name;
	    private final SimpleStringProperty address;
	    private final SimpleStringProperty job;
	    
	    public CustModel(String id, String f_name, String l_name, String address, String job) {
	        this.id = new SimpleStringProperty(id);
	        this.f_name = new SimpleStringProperty(f_name); // Updated variable name
	        this.l_name = new SimpleStringProperty(l_name);
	        this.address = new SimpleStringProperty(address);
	        this.job = new SimpleStringProperty(job);
	    }

		public SimpleStringProperty getId() {
			return id;
		}

		public SimpleStringProperty getF_name() {
			return f_name;
		}

		public SimpleStringProperty getL_name() {
			return l_name;
		}

		public SimpleStringProperty getAddress() {
			return address;
		}

		public SimpleStringProperty getJob() {
			return job;
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
    
    private List<String> fetchcustID() {
        List<String> customerList = new ArrayList<>();
        try {
            // Establish connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = con.createStatement();

            // Query to retrieve customer data
            ResultSet rs = stmt.executeQuery("SELECT id FROM customer"); 

            // Iterate through the result set and add customer names to the list
            while (rs.next()) {
                String customerId = rs.getString("id"); 
                customerList.add(customerId);
            }

            
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching address data");
        }
        return customerList;
    }  
	 
	
}
