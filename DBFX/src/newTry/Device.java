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
import newTry.Car.CarModel;
import newTry.Device.DeviceModel;

public class Device extends Application {

	private TableView<DeviceModel> tableview;
    private ObservableList<DeviceModel> data;
    Connection con;

	public static void main(String[] args) {
		launch(args);
	}

	public void buildData() {
		tableview.getColumns().clear(); // Clear existing columns before adding new ones

		
       

     // Define table columns with CellValueFactory for all columns
        TableColumn<DeviceModel, String> numcol = new TableColumn<>("Number");
        numcol.setCellValueFactory(new Callback<CellDataFeatures<DeviceModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<DeviceModel, String> param) {
                return param.getValue().getNo();
            }
        });

        TableColumn<DeviceModel, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new Callback<CellDataFeatures<DeviceModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<DeviceModel, String> param) {
                return param.getValue().getName();
            }
        });

        TableColumn<DeviceModel, String> PriceCol = new TableColumn<>("Price");
        PriceCol.setCellValueFactory(new Callback<CellDataFeatures<DeviceModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<DeviceModel, String> param) {
                return param.getValue().getPrice();
            }
        });

        TableColumn<DeviceModel, String> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(new Callback<CellDataFeatures<DeviceModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<DeviceModel, String> param) {
                return param.getValue().getWeight();
            }
        });

        TableColumn<DeviceModel, String> madeCol = new TableColumn<>("Made");
        madeCol.setCellValueFactory(new Callback<CellDataFeatures<DeviceModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<DeviceModel, String> param) {
                return param.getValue().getMade();
            }
        });

        tableview.getColumns().addAll(numcol, nameCol, PriceCol, weightCol, madeCol);

   	
	    data = FXCollections.observableArrayList();
        try {
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from device");

            while (rs.next()) {
                data.add(new DeviceModel(rs.getString("no"), rs.getString("name"),
                        rs.getString("price"), rs.getString("weight"),rs.getString("made")));
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
		Label label = new Label("Device Table");
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
        TextField numField = new TextField();
        TextField nameField = new TextField();
        TextField PriceField = new TextField();
        TextField weightField = new TextField();
        ComboBox<String> madeField = new ComboBox<>();

        
        List<String> manufactures =fetchMnuName();
        madeField.getItems().addAll(manufactures);
        
        
	        
        
        // Create labels
        Label numLabel = new Label("Number:");
        Label nameLabel = new Label("Name:");
        Label priceLabel = new Label("Price:");
        Label weightLabel = new Label("Weight:");
        Label madeLabel = new Label("Made:");
        // Set font and style for labels
        numLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        priceLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        weightLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        madeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

   	
        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, numLabel, numField);
        gridPane.addRow(1, nameLabel, nameField);
        gridPane.addRow(2, priceLabel, PriceField);
        gridPane.addRow(3, weightLabel, weightField);
        
        gridPane.addRow(4, madeLabel, madeField);
   	

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
			
			String num = numField.getText();
            String  name = nameField.getText();
            String price = PriceField.getText();
            String weight = weightField.getText();
            String made = madeField.getValue();
            searchDev(num, name, price, weight,made);
		});
		return gridPane;
	}
	private void searchDev(String no, String name, String price, String weight, String made) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM device WHERE 1=1";

            if (!no.isEmpty()) {
                query += " AND no = '" + no + "'";
            }
            if (!name.isEmpty()) {
                query += " AND name = '" + name + "'";
            }
            if (!price.isEmpty()) {
                query += " AND price = '" + price + "'";
            }
            if (!weight.isEmpty()) {
                query += " AND weight = '" + weight + "'";
            }
            if (made != null && !made.isEmpty()) {
                query += " AND made = '" + made + "'";
            }
           
            ResultSet rs = stmt.executeQuery(query);
            ObservableList<DeviceModel> searchResult = FXCollections.observableArrayList();
            while (rs.next()) {
                searchResult.add(new DeviceModel(rs.getString("no"), rs.getString("name"),
                        rs.getString("price"), rs.getString("weight"),rs.getString("made")));
            }

            tableview.setItems(searchResult); // Set the search results to the TableView

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error searching device");
        }
    }


	public GridPane Insert() {
		TextField numField = new TextField();
        TextField nameField = new TextField();
        TextField PriceField = new TextField();
        TextField weightField = new TextField();
        ComboBox<String> madeField = new ComboBox<>();

        
        List<String> manufactures =fetchMnuName();
        madeField.getItems().addAll(manufactures);
        
        
	        
        
        // Create labels
        Label numLabel = new Label("Number:");
        Label nameLabel = new Label("Name:");
        Label priceLabel = new Label("Price:");
        Label weightLabel = new Label("Weight:");
        Label madeLabel = new Label("Made:");
        // Set font and style for labels
        numLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        priceLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        weightLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        madeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

   	
        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, numLabel, numField);
        gridPane.addRow(1, nameLabel, nameField);
        gridPane.addRow(2, priceLabel, PriceField);
        gridPane.addRow(3, weightLabel, weightField);
        
        gridPane.addRow(4, madeLabel, madeField);
   	

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
			 String num = numField.getText();
             String  name = nameField.getText();
             String price = PriceField.getText();
             String weight = weightField.getText();
             String made = madeField.getValue();
             insertDev(num, name, price, weight,made);
        });
		
		
		
		 
		return gridPane;
	}
	private void insertDev(String no, String name, String price, String weight, String made) {
        try {
            Statement stmt = con.createStatement();
            String query = "INSERT INTO device (no, name, price, weight ,made) VALUES ('" +
            		no + "', '" + name + "', '" + price + "', '" + weight +"', '"+made+ "')";
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
            	
            	
            	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                
                successAlert.setContentText("Device inserted successfully.");

                successAlert.showAndWait();
               
                System.out.println("device inserted successfully.");
                // Refresh TableView or rebuild data after insertion
                buildData();
            } else {
            	
            	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                
                errorAlert.setContentText("Device insertion failed.");

                errorAlert.showAndWait();
                System.out.println("device insertion failed.");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error inserting device");
        }
    }
	public GridPane Update() {
		
ComboBox<String> numField = new ComboBox<>();

        
        List<String> carnames =fetchnumber();
        numField.getItems().addAll(carnames);
        TextField nameField = new TextField();
        TextField PriceField = new TextField();
        TextField weightField = new TextField();
        ComboBox<String> madeField = new ComboBox<>();

        
        List<String> manufactures =fetchMnuName();
        madeField.getItems().addAll(manufactures);
        
        
	        
        
        // Create labels
        Label numLabel = new Label("Number:");
        Label nameLabel = new Label("Name:");
        Label priceLabel = new Label("Price:");
        Label weightLabel = new Label("Weight:");
        Label madeLabel = new Label("Made:");
        // Set font and style for labels
        numLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        nameLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        priceLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        weightLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
        madeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));

   	
        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, numLabel, numField);
        gridPane.addRow(1, nameLabel, nameField);
        gridPane.addRow(2, priceLabel, PriceField);
        gridPane.addRow(3, weightLabel, weightField);
        
        gridPane.addRow(4, madeLabel, madeField);
   	

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
				 String no = numField.getValue();
	             String  name = nameField.getText();
	             String price = PriceField.getText();
	             String weight = weightField.getText();
	             String made = "";
		            if (madeField.getValue() == null) {
		            	
		            	madeField.getPromptText();
		            	
		            }
		            else {
		            	made = madeField.getSelectionModel().getSelectedItem().toString();}
	             
		            try {
		    	        Class.forName("com.mysql.cj.jdbc.Driver");

		    	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
		    	        Statement stmt = con.createStatement();
		    	        String sql = "UPDATE device";

		    	        if (name.isEmpty() && price.isEmpty() && weight.isEmpty() && made.isEmpty()) {
		    	            sql = "SELECT * FROM device";
		    	        } else {
		    	            sql += " SET ";
		    	            if (!name.isEmpty()) {
		    	                sql += "name = '" + name + "', ";
		    	            }
		    	            if (!price.isEmpty()) {
		    	                sql += "price = '" + price + "', ";
		    	            }
		    	            if (!weight.isEmpty()) {
		    	                sql += "weight = '" + weight + "', ";
		    	            }
		    	            if (!made.isEmpty()) {
		    	                sql += "made = '" + made + "', ";
		    	            }
		    	            if (no.isEmpty()) {
		    	                Alert alert = new Alert(Alert.AlertType.WARNING);
		    	                alert.setTitle("Warning");
		    	                alert.setContentText("The Process does not work because you did not provide the device number.");
		    	                alert.showAndWait();
		    	                sql = "SELECT * FROM device";
		    	            }

		    	            if (sql.endsWith(", ")) {
		     		            sql = sql.substring(0, sql.length() - 2); // Remove the trailing comma and space
		     		        }

		     		        if (!no.isEmpty()) {
		     		            sql += " WHERE no = " + no;
		     		        }
		     		       System.out.println(sql);
		     		        stmt.executeUpdate(sql);
		     		        // Assuming buildData() method is used to update UI after database changes
		     		       Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
		     	            successAlert.setTitle("Success");
		     	            
		     	            successAlert.setContentText("Device updated successfully!");

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
	
	private void updateDev(String no, String name, String price, String weight, String made) {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");

	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
	        Statement stmt = con.createStatement();
	        String sql = "UPDATE device";

	        if (name.isEmpty() && price.isEmpty() && weight.isEmpty() && made.isEmpty()) {
	            sql = "SELECT * FROM device";
	        } else {
	            sql += " SET ";
	            if (!name.isEmpty()) {
	                sql += "name = '" + name + "', ";
	            }
	            if (!price.isEmpty()) {
	                sql += "price = '" + price + "', ";
	            }
	            if (!weight.isEmpty()) {
	                sql += "weight = '" + weight + "', ";
	            }
	            if (!made.isEmpty()) {
	                sql += "made = '" + made + "', ";
	            }
	            if (no.isEmpty()) {
	                Alert alert = new Alert(Alert.AlertType.WARNING);
	                alert.setTitle("Warning");
	                alert.setContentText("The Process does not work because you did not provide the device number.");
	                alert.showAndWait();
	                sql = "SELECT * FROM device";
	            }

	            String[] splitArray = sql.split(" ");
	            String lastWord = splitArray[splitArray.length - 1];

	            if (lastWord.equalsIgnoreCase(",")) {
	                sql = String.join(" ", Arrays.copyOf(splitArray, splitArray.length - 1));
	                if (!no.isEmpty()) {
	                    sql += " WHERE no = " + no;
	                }

	                stmt.executeUpdate(sql);
	                // Assuming buildData() method is used to update UI after database changes
	                buildData();
	            }
	            System.out.println(sql);
	        }

	        stmt.close();
	        con.close();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	}

	    		
	public GridPane Delete() {
		
        ComboBox<String> numField = new ComboBox<>();

        
        List<String> carnames =fetchnumber();
        numField.getItems().addAll(carnames);
        
        
        Label numLabel = new Label("Number:");
       
        // Set font and style for labels
        numLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 17));
       

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.addRow(0, numLabel, numField);
      

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
			  String name = numField.getValue();
			  deleteDev(name);
		});
		return gridPane;
	}
	private void deleteDev(String no) {
        try {
            Statement stmt = con.createStatement();

           
           
                // Now that associated records in car_part are deleted, delete the car
                String deleteAddressQuery = "DELETE FROM device WHERE no = '" + no + "'";
                int rowsAffectedAddress = stmt.executeUpdate(deleteAddressQuery);

                if (rowsAffectedAddress > 0) {
                	Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    
                    successAlert.setContentText("device deleted successfully.");

                    successAlert.showAndWait();
                    System.out.println("device deleted successfully.");
                    // Refresh TableView or rebuild data after deletion
                    buildData();
                } else {
                	
                	
                	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    
                    errorAlert.setContentText("Device not found or couldn't be deleted..");

                    errorAlert.showAndWait();
                    System.out.println("device not found or couldn't be deleted.");
                }
            

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting device");
        }
    }
	 public class DeviceModel {
	        
		    private final SimpleStringProperty no;
		    private final SimpleStringProperty name; // Corrected column name
		    private final SimpleStringProperty price;
		    private final SimpleStringProperty weight;
		    private final SimpleStringProperty made;
		    
		    public DeviceModel(String no, String name, String price, String weight, String made) {
		        this.no = new SimpleStringProperty(no);
		        this.name = new SimpleStringProperty(name); // Updated variable name
		        this.price = new SimpleStringProperty(price);
		        this.weight = new SimpleStringProperty(weight);
		        this.made = new SimpleStringProperty(made);
		    }

			public SimpleStringProperty getNo() {
				return no;
			}

			public SimpleStringProperty getName() {
				return name;
			}

			public SimpleStringProperty getPrice() {
				return price;
			}

			public SimpleStringProperty getWeight() {
				return weight;
			}

			public SimpleStringProperty getMade() {
				return made;
			}
		
	    
	    

	}
	    private List<String> fetchMnuName() {
	        List<String> manufactureList = new ArrayList<>();
	        try {
	            // Establish connection
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
	            Statement stmt = con.createStatement();

	            // Query to retrieve customer data
	            ResultSet rs = stmt.executeQuery("SELECT name FROM manufacture"); 

	            // Iterate through the result set and add customer names to the list
	            while (rs.next()) {
	                String customerName = rs.getString("name"); 
	                manufactureList.add(customerName);
	            }

	            
	            rs.close();
	            stmt.close();
	            con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error fetching manufacture data");
	        }
	        return manufactureList;
	    }
	    

	    private List<String> fetchnumber() {
	        List<String> devicenum = new ArrayList<>();
	        try {
	            // Establish connection
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
	            Statement stmt = con.createStatement();

	            // Query to retrieve customer data
	            ResultSet rs = stmt.executeQuery("SELECT no FROM device"); 

	            // Iterate through the result set and add customer names to the list
	            while (rs.next()) {
	                String customerName = rs.getString("no"); 
	                devicenum.add(customerName);
	            }

	            
	            rs.close();
	            stmt.close();
	            con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error fetching manufacture data");
	        }
	        return devicenum;
	    }
	    
	
	
	 
	
}
