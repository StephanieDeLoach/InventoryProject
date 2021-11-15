package myinventory;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;



/**
 *
 * @author sdeloach
 * 11/15/20 
 * Controller for the main application window
 * Possible modification for future version: When user is deleting a product, a pane allows the user to see the associated products without
 * having to "modify" the product.  This way the user can choose to delete the associated parts at the same time as they delete the product
 * 
 * Logical error: See partSearchTextFieldListener
 *
 */
public class InventoryMainController {
    

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Part,Integer> partIDColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Integer> partInventoryLevelColumn;

    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    @FXML
    private TextField partsSearchTextField;

    @FXML
    private Button addButton;

    @FXML
    private Button modifyButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, Integer> productIDColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;

    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    @FXML
    private TextField productSearchTextField;

    @FXML
    private Button addProductButton;

    @FXML
    private Button modifyProductButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Button exitButton;
    
    @FXML
    private Label exceptions;
    


    /**
     * Initializes the Part Table view and Product Table View
     */
   
    public void initialize()
    {   
        
        //Sets the columns for the Part TableView
        partIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        
        // Adds the parts in the Part Data list
        partTableView.getItems().setAll(Inventory.getAllParts());
        
        //Sets the columns for the Product Table view
        productIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        //Adds the products to the product table view
        productTableView.getItems().setAll(Inventory.getAllProducts());
    }
    
    /**
     * Closes the application
     */
    public void exitButtonListener()
    {
        Platform.exit();

    }
    
    /**
     * opens AddPart form 
     * Exception thrown if AddPart.fxml is not found.
     */
    public void addButtonListener(){
        try{
      
            Parent newparent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
       
        
            Scene scene = new Scene(newparent);
            Stage secondStage = new Stage();
            secondStage.setScene(scene);
            secondStage.show();

            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.hide();
        }catch(Exception e){
            System.out.println("Cannot open AddPart.fxml");
        }
       
    }
    
    /**
     * Deletes the selected part.  
     * If no part is selected, error given with a dialog box
     */
    public void deleteButtonListener(){
        Stage cancelDialog = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(gridPane, 300,150);
        
        cancelDialog.setScene(dialogScene);
        //check to see if an item has been selected in the table view
        if(partTableView.getSelectionModel().isEmpty()){
            Button cancelButton = new Button("Ok");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent arg1){
                cancelDialog.hide();
            }
        });
        Label okToDelete = new Label();
        okToDelete.setText("No part selected to delete");
        gridPane.add(okToDelete,0,2);
        gridPane.add(cancelButton, 200, 100);
     
        cancelDialog.show();
        
        // Verify that the user wants to delete the selected part
        // then deletes it if the user clicks "OK"
        }else{
            
            Button okButton = new Button("OK");
            okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0){
            Part selection = partTableView.getSelectionModel().getSelectedItem();

                Inventory.deletePart(Inventory.lookupPart(selection.getId()));
                
                cancelDialog.hide();
                partTableView.getItems().clear();
                partTableView.getItems().addAll(Inventory.getAllParts());
            }});
        
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override 
                public void handle(ActionEvent arg1){
                    cancelDialog.hide();
                }
            });
            Label okToDelete = new Label();
            okToDelete.setText("Are you sure you want to delete?");
            gridPane.add(okToDelete,0,2);
            gridPane.add(okButton, 100, 100);
            gridPane.add(cancelButton, 200, 100);
     
            cancelDialog.show();
        }   
    }
    
    /**
     * Opens the modify part stage
     * @exception nullPointerException if ModifyPartInHouse not found
     *  
     */
    public void modifyButtonListener(){
        Stage cancelDialog = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(gridPane, 300,150);
        
        cancelDialog.setScene(dialogScene);
        
        // Error if no product is selected in the table view
        if(partTableView.getSelectionModel().isEmpty()){
            Button cancelButton = new Button("Ok");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override 
                public void handle(ActionEvent arg1){
                    cancelDialog.close();
                }
            });
            Label okToDelete = new Label();
            okToDelete.setText("No part selected to modify");
            gridPane.add(okToDelete,0,2);
            gridPane.add(cancelButton, 200, 100);
     
            cancelDialog.show();
        }
        else{
        
            Part part = partTableView.getSelectionModel().getSelectedItem();
            
            // Opens the modify part stage and sets the content for an InHouse part
            //if(partTableView.getSelectionModel().getSelectedItem() instanceof InHouse){
                try{
                             
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPartInHouse.fxml"));                               
                    Parent newparent = loader.load();
                
                    ModifyPartInHouseController controller = (ModifyPartInHouseController)loader.getController();

                    controller.setPart(part);
                    controller.setContent();
                    Scene scene = new Scene(newparent);
                    Stage secondStage = new Stage();
                    secondStage.setScene(scene);
                    secondStage.show();

                    Stage stage = (Stage) addButton.getScene().getWindow();
                    stage.hide();
                }
                catch(Exception e){
                    System.out.println("Caught Exception " + e.toString());
                }
            }            
    }
                               
    
    /**
     * Opens the addProduct stage
     *  Exception caught if AddProduct.fxml is not found
     */
    public void addProductButtonListener(){
        try{
            Parent thisparent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
       
        
            Scene scene = new Scene(thisparent);
            Stage thirdStage = new Stage();
            thirdStage.setScene(scene);
            thirdStage.show();
            
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.hide();
        }catch(Exception e){
            System.out.println("Exception: " + e.toString());
        }
    }
    
    /**
     * Deletes the selected product
     * If no product selected, prompts user with a dialog box
     * If Product has associated parts, product will not be deleted
     */
    public void deleteProductButtonListener(){
        Stage cancelDialog = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(gridPane, 300,150);
        
        cancelDialog.setScene(dialogScene);
        
        // Error if no product is selected in the table view
        if(productTableView.getSelectionModel().isEmpty()){
            Button cancelButton = new Button("Ok");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent arg1){
                cancelDialog.close();
            }
        });
        Label okToDelete = new Label();
        okToDelete.setText("No product selected to delete");
        gridPane.add(okToDelete,0,2);
        gridPane.add(cancelButton, 200, 100);
     
        cancelDialog.show();
        
        // Removes the product if there are no associated parts
        }else if(productTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().size() == 0){
            
            Button okButton = new Button("OK");
            okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0){
                Product selection = productTableView.getSelectionModel().getSelectedItem();
                          
                Inventory.deleteProduct(Inventory.lookupProduct(selection.getId()));
                    
                cancelDialog.hide();
                productTableView.getItems().clear();
                productTableView.getItems().setAll(Inventory.getAllProducts());
            }});
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent arg1){
                cancelDialog.hide();
            }
        });
        Label okToDelete = new Label();
        okToDelete.setText("Are you sure you want to delete?");
        gridPane.add(okToDelete,0,2);
        gridPane.add(okButton, 0, 100);
        gridPane.add(cancelButton, 200, 100);
     
        cancelDialog.show();
        // Error given if user tries to delete a product with associated parts
        }else{
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override 
                public void handle(ActionEvent arg1){
                    cancelDialog.hide();
                }
            });
            Label okToDelete = new Label();
            okToDelete.setText("This product has associated parts.\n Cannot delete");
            gridPane.add(cancelButton, 200, 100);
            gridPane.add(okToDelete, 0,2);
            cancelDialog.show();
            
        }
    }
    
    /**
     * Opens the Modify Product Stage
     * Exception caught if ModifyProduct.fxml is not found
     */
    public void modifyProductButtonListener(){
        try{
        Product product = productTableView.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
        Parent thisparent = loader.load();
        
        ModifyProductController controller = (ModifyProductController)loader.getController();
        
        //Sends the Product to modify to the ModifyProduct controller
        controller.setProduct(product);
        controller.setContent();
        
        Scene scene = new Scene(thisparent);
        Stage thirdStage = new Stage();
        thirdStage.setScene(scene);
        thirdStage.show();
        
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.hide();
        
        }catch(Exception e){
            System.out.println("Unable to open ModifyProduct.fxml");
        }
    }
    
    /**
     * Selects parts from the Part Table View using either the Part ID or the Part name
     * Dialog opened if part is not found
     * Highlights part if searched by ID
     * Filters Parts if searched by name
     */
    public void partSearchTextFieldListener(){
        String selection = partsSearchTextField.getText();
        boolean numeric = true;
        ObservableList<Part> newList = FXCollections.observableArrayList();
        Stage cancelDialog = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(gridPane, 300,150);
        
        cancelDialog.setScene(dialogScene);
        
        //Determines if the data in the TextField is numeric or textual
        try {
            Integer num = Integer.parseInt(selection);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        //If numeric, searches by part ID, highlight part found
        // Logical error: the code at first filtered both if the user serched by Part ID or by Part name. I had to look up how to select
        // a line in the Table View and highlight it. This code went through many revisions because I forgot to use the Inventory class and 
        // created my own classes for part lists and product lists.  I eventually had to replace all of my own part list and product lists with 
        // the Inventory lists. 
        if(numeric){           
            partTableView.getSelectionModel().select(Inventory.lookupPart(Integer.parseInt(selection)));
        //Otherwise, searches by part name, filter by 'selection'
        }else{
            newList = Inventory.lookupPart(selection);
            if(newList.size() > 0){
                partTableView.getItems().clear();
                partTableView.getItems().addAll(newList);
            }
        }
        // Gives an error if the part is not found
        if(partTableView.getSelectionModel().isEmpty() && newList.isEmpty()){
            Button cancelButton = new Button("Ok");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent arg1){
                cancelDialog.hide();
            }
        });
        Label okToDelete = new Label();
        okToDelete.setText("Part not found");
        gridPane.add(okToDelete,0,2);
        gridPane.add(cancelButton, 200, 100);
     
        cancelDialog.show();
        
            
        }
    }
    
    /**
     * Selects products from the Product Table View using either the Product ID or the Product name
     * Dialog opened if product is not found
     * Highlights product if searched by ID
     * Filters Product if searched by name
     */
    public void productSearchTextFieldListener(){
        String selection = productSearchTextField.getText();
        boolean numeric = true;
        ObservableList<Product> newList = FXCollections.observableArrayList();
        
        Stage cancelDialog = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(gridPane, 300,150);
        cancelDialog.setScene(dialogScene);
        
        //Decide if the data in the Text Field is an Integer or a String
        try {
            Integer num = Integer.parseInt(selection);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        // If the data is numeric, search by part ID
        if(numeric){
            productTableView.getSelectionModel().select(Inventory.lookupProduct(Integer.parseInt(selection)));
        //Otherwise search buy part name
        }else{
            newList = Inventory.lookupProduct(selection);
            if(newList.size() > 0){
                productTableView.getItems().clear();
                productTableView.getItems().addAll(newList);
            }
        }
        //Gives an error if no item is found
        if(productTableView.getSelectionModel().isEmpty() && newList.isEmpty()){
            Button cancelButton = new Button("Ok");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent arg1){
                cancelDialog.hide();
            }
        });
        Label okToDelete = new Label();
        okToDelete.setText("Product not found");
        gridPane.add(okToDelete,0,2);
        gridPane.add(cancelButton, 200, 100);
     
        cancelDialog.show();
        
            
        }
        
    }
    
    
}
