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

import newTry.Order.OrderModel;

public class Order extends Application {
	 private TableView<OrderModel> tableview;
	    private ObservableList<OrderModel> data;
	    Connection con;
	public static void main(String[] args) {
		launch(args);
	}

	public void buildData() {
		tableview.getColumns().clear(); // Clear existing columns before adding new ones

		 // Define table columns with CellValueFactory for all columns
        TableColumn<OrderModel, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new Callback<CellDataFeatures<OrderModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<OrderModel, String> param) {
                return param.getValue().getId();
            }
        });

        TableColumn<OrderModel, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new Callback<CellDataFeatures<OrderModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<OrderModel, String> param) {
                return param.getValue().getDate();
            }
        });

        
        
        
        TableColumn<OrderModel, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new Callback<CellDataFeatures<OrderModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<OrderModel, String> param) {
                return param.getValue().getCustomer();
            }
        });
        
        TableColumn<OrderModel, String> carCol = new TableColumn<>("Car");
        carCol.setCellValueFactory(new Callback<CellDataFeatures<OrderModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<OrderModel, String> param) {
                return param.getValue().getCar();
            }
        });

      

        tableview.getColumns().addAll(idCol, dateCol, customerCol, carCol);
        
        
        data = FXCollections.observableArrayList();
        try {
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from orders");

            while (rs.next()) {
                data.add(new OrderModel(rs.getString("id"), rs.getString("date"),
                        rs.getString("customer"), rs.getString("car")));
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
		Label label = new Label("Orders Table");
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
        TextField idField = new TextField();
        TextField DateField = new TextField();
        ComboBox<String> CustomerField = new ComboBox<>();
        ComboBox<String> CarField = new ComboBox<>();
        
        
        List<String> customers = fetchCustomerData();
        CustomerField.getItems().addAll(customers);
        
        
        List<String> cars = fetchCarData();
        CarField.getItems().addAll(cars);
	        
        // Create labels
        Label idLabel = new Label("ID:");
        Label dateLabel = new Label("Date:");
        Label customerLabel = new Label("Customer:");
        Label carLabel = new Label("Car:");

        // Set font and style for labels
        idLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        dateLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        customerLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        carLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, idLabel, idField);
        gridPane.addRow(1, dateLabel, DateField);
        gridPane.addRow(2, customerLabel, CustomerField);
        gridPane.addRow(3, carLabel, CarField);

        VBox leftVBox = new VBox(gridPane);
        leftVBox.setPadding(new Insets(20));
        leftVBox.setSpacing(20);
        
		Button buttonSelect = new Button("Select Data");
		buttonSelect.setStyle("-fx-background-radius: 20; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
		
		
		
		HBox hb = new HBox();
		hb.getChildren().addAll(buttonSelect);
		hb.setSpacing(20);
		gridPane.add(hb, 0, 10);

		buttonSelect.setOnAction(e -> {
			
			 String id = idField.getText();
             String date = DateField.getText();
             String customer = CustomerField.getValue();
             String car = CarField.getValue();
             searchOrder(id, date, customer, car);
		});
		return gridPane;
	}
	private void searchOrder(String id, String date, String customer, String car) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM orders WHERE 1=1";

            if (!id.isEmpty()) {
                query += " AND id = '" + id + "'";
            }
            if (!date.isEmpty()) {
                query += " AND date = '" + date + "'";
            }
            if (customer != null && !customer.isEmpty()) {
                query += " AND customer = '" + customer + "'";
            }
            if (car != null && !car.isEmpty()) {
                query += " AND car = '" + car + "'";
            }

            

            ResultSet rs = stmt.executeQuery(query);
            ObservableList<OrderModel> searchResult = FXCollections.observableArrayList();
            while (rs.next()) {
                searchResult.add(new OrderModel(rs.getString("id"), rs.getString("date"),
                        rs.getString("customer"), rs.getString("car")));
            }

            tableview.setItems(searchResult); // Set the search results to the TableView

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error searching Orders");
        }
    }




	public GridPane Insert() {
		// Create input fields and buttons
        TextField idField = new TextField();
        TextField DateField = new TextField();
        ComboBox<String> CustomerField = new ComboBox<>();
        ComboBox<String> CarField = new ComboBox<>();
        
        
        List<String> customers = fetchCustomerData();
        CustomerField.getItems().addAll(customers);
        
        
        List<String> cars = fetchCarData();
        CarField.getItems().addAll(cars);
	        
        // Create labels
        Label idLabel = new Label("ID:");
        Label dateLabel = new Label("Date:");
        Label customerLabel = new Label("Customer:");
        Label carLabel = new Label("Car:");

        // Set font and style for labels
        idLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        dateLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        customerLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        carLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, idLabel, idField);
        gridPane.addRow(1, dateLabel, DateField);
        gridPane.addRow(2, customerLabel, CustomerField);
        gridPane.addRow(3, carLabel, CarField);

        VBox leftVBox = new VBox(gridPane);
        leftVBox.setPadding(new Insets(20));
        leftVBox.setSpacing(20);
        
		Button buttonInsert = new Button("Insert Data");
		buttonInsert.setStyle("-fx-background-radius: 20; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");

		HBox hb = new HBox();
		hb.getChildren().addAll(buttonInsert);
		hb.setSpacing(20);
		gridPane.add(hb, 0, 10);
		buttonInsert.setOnAction(e -> {
			 String id = idField.getText();
             String date = DateField.getText();
             String customer = CustomerField.getValue();
             String car = CarField.getValue();
             insertOrder(id, date, customer, car);
        });
		
		
		
		 
		return gridPane;
	}
	 private void insertOrder(String id, String date, String customer, String car) {
	        try {
	            Statement stmt = con.createStatement();
	            String query = "INSERT INTO orders (id, date, customer, car) VALUES ('" +
	                    id + "', '" + date + "', '" + customer + "', '" + car + "')";
	            int rowsAffected = stmt.executeUpdate(query);
	            if (rowsAffected > 0) {
	            	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
	                successAlert.setTitle("Success");
	                
	                successAlert.setContentText("The order has been successfully inserted.");

	                successAlert.showAndWait(); // Show the success alert
	                buildData(); // Refresh TableView or rebuild data after insertion
	            } else {
	            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	                errorAlert.setTitle("Error");
	                
	                errorAlert.setContentText("The  Order insertion failed.");

	                errorAlert.showAndWait();
	            }
	            stmt.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error inserting Order");
	        }
	    }

	public GridPane Update() {
		 ComboBox<String> idField = new ComboBox<>();

	        
	        List<String> orderId =fetchOrderId();
	        idField.getItems().addAll(orderId);
	        
		// Create input fields and buttons
       
        TextField DateField = new TextField();
        ComboBox<String> CustomerField = new ComboBox<>();
        ComboBox<String> CarField = new ComboBox<>();
        
        
        List<String> customers = fetchCustomerData();
        CustomerField.getItems().addAll(customers);
        
        
        List<String> cars = fetchCarData();
        CarField.getItems().addAll(cars);
	        
        // Create labels
        Label idLabel = new Label("ID:");
        Label dateLabel = new Label("Date:");
        Label customerLabel = new Label("Customer:");
        Label carLabel = new Label("Car:");

        // Set font and style for labels
        idLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        dateLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        customerLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        carLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, idLabel, idField);
        gridPane.addRow(1, dateLabel, DateField);
        gridPane.addRow(2, customerLabel, CustomerField);
        gridPane.addRow(3, carLabel, CarField);

        VBox leftVBox = new VBox(gridPane);
        leftVBox.setPadding(new Insets(20));
        leftVBox.setSpacing(20);
        
			Button buttonUpdate = new Button("Update Data");
			buttonUpdate.setStyle("-fx-background-radius: 20; -fx-background-color:#4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
			
			HBox hb = new HBox();
			hb.getChildren().addAll(buttonUpdate);
			hb.setSpacing(20);
			gridPane.add(hb, 0,10);
			buttonUpdate.setOnAction(e -> {
				 String id = idField.getValue();
	             String date = DateField.getText();
	           //  String customer = CustomerField.getValue();
	             String customer = "";
		            if (CustomerField.getValue() == null) {
		            	
		            	CustomerField.getPromptText();
		            	
		            }
		            else {
		            	customer = CustomerField.getSelectionModel().getSelectedItem().toString();}
	             
	           //  String car = CarField.getValue();
	             String car = "";
		            if (CarField.getValue() == null) {
		            	
		            	CarField.getPromptText();
		            	
		            }
		            else {
		            	car = CarField.getSelectionModel().getSelectedItem().toString();}
	             try {
				        Class.forName("com.mysql.cj.jdbc.Driver");
				        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
				        Statement stmt = con.createStatement();

				        String sql = "UPDATE orders";

				        if (date.isEmpty() && customer.isEmpty() && car.isEmpty() ) {
				            sql = "SELECT * FROM orders";
				            System.out.println("No fields provided for update.");
				        } else {
				            sql += " SET ";
				            if (!date.isEmpty()) {
				                sql += "date = '" + date + "', ";
				            }
				            if (!customer.isEmpty()) {
				                sql += "customer = '" + customer + "', ";
				            }
				            if (!car.isEmpty()) {
				                sql += "car = '" + car + "', ";
				            }
				           
				            if (id.isEmpty()) {
				                Alert alert = new Alert(Alert.AlertType.WARNING);
				                alert.setTitle("Warning");
				                alert.setContentText("Please provide the order ID.");
				                alert.showAndWait();
				                sql = "SELECT * FROM orders";
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
				                
				                successAlert.setContentText("Order updated successfully!");

				                successAlert.showAndWait();
				            //    System.out.println("Address updated successfully!");
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
	
	private void updateOrder(String id, String date, String customer, String car) {
        try {
            Statement stmt = con.createStatement();
            String query = "UPDATE orders SET date = '" + date + "', customer = '" + customer +
                    "', car = '" + car + "' WHERE id = '" + id + "'";
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
            	
            	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                
                successAlert.setContentText("The Order updated successfully.");

                successAlert.showAndWait(); // Show the success alert
                buildData();
            	
        
                // Refresh TableView or rebuild data after update
            } else {
            	
            	
            	
            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                
                errorAlert.setContentText("Order not found or couldn't be updated.");

                errorAlert.showAndWait();
               
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating order");
        }
    }

    
	public GridPane Delete() {
		
        ComboBox<String> idField = new ComboBox<>();

        
        List<String> orderId =fetchOrderId();
        idField.getItems().addAll(orderId);
        
        
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
		Button buttonDelete = new Button("Delete Data");
		buttonDelete.setStyle("-fx-background-radius: 20; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
		HBox hb = new HBox();
		hb.getChildren().addAll(buttonDelete);
		hb.setSpacing(20);
		gridPane.add(hb, 0,10);

		buttonDelete.setOnAction(e -> {
			  String name = idField.getValue();
			  deleteOrder(name);
		});
		return gridPane;
	}
	private void deleteOrder(String id) {
        try {
            Statement stmt = con.createStatement();
            String deleteOrderQuery = "DELETE FROM orders WHERE id = '" + id + "'";
            int rowsAffected = stmt.executeUpdate(deleteOrderQuery);

            if (rowsAffected > 0) {
                // Create a success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                
                successAlert.setContentText("The order has been successfully deleted.");

                successAlert.showAndWait(); // Show the success alert

                buildData(); // Refresh TableView or rebuild data after deletion
            } else {
                // Create an error alert
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                
                errorAlert.setContentText("The order was not found or couldn't be deleted.");

                errorAlert.showAndWait(); // Show the error alert
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting Order");
        }
    }

	
	 public class OrderModel {
	        private final SimpleStringProperty id;
	        private final SimpleStringProperty date;
	        private final SimpleStringProperty customer;
	        private final SimpleStringProperty car;
	        
	        
	        
	        
	        
	        
			public OrderModel(String id, String date, String customer,
					String car) {
				super();
				this.id = new SimpleStringProperty(id);
				this.date = new SimpleStringProperty(date);
				this.customer = new SimpleStringProperty(customer);
				this.car = new SimpleStringProperty(car);
			}






			public SimpleStringProperty getId() {
				return id;
			}






			public SimpleStringProperty getDate() {
				return date;
			}






			public SimpleStringProperty getCustomer() {
				return customer;
			}






			public SimpleStringProperty getCar() {
				return car;
			}
			
	      
	    }
	    
	
	    
	    private List<String> fetchCustomerData() {
	        List<String> customerList = new ArrayList<>();
	        try {
	            // Establish connection
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
	            Statement stmt = con.createStatement();

	            // Query to retrieve customer data
	            ResultSet rs = stmt.executeQuery("SELECT id FROM customer"); 

	            // Iterate through the result set and add customer names to the list
	            while (rs.next()) {
	                String customerName = rs.getString("id"); 
	                customerList.add(customerName);
	            }

	            
	            rs.close();
	            stmt.close();
	            con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error fetching customer data");
	        }
	        return customerList;
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

	    private List<String> fetchOrderId() {
	        List<String> idList = new ArrayList<>();
	        try {
	          
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
	            Statement stmt = con.createStatement();

	            
	            ResultSet rs = stmt.executeQuery("SELECT id FROM orders"); 
	        
	            while (rs.next()) {
	                String carModel = rs.getString("id"); 
	                idList.add(carModel);
	            }

	          
	            rs.close();
	            stmt.close();
	            con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error fetching car data");
	        }
	        return idList;
	    }
	 
	
}
