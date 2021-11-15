package myinventory;





    

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;

/** 
 * 
 * @author sdeloach
 * Controller for the Add Product page
 */
public class AddProductController {

    @FXML
    private TextField productIDTextField;

    @FXML
    private TextField productNameTextField;

    @FXML
    private TextField inventoryTextField;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private TextField partSearchTextField;

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
    private TableView<Part> partTableView1;

    @FXML
    private TableColumn<Part, Integer> partIDColumn1;

    @FXML
    private TableColumn<Part, String> partNameColumn1;

    @FXML
    private TableColumn<Part, Integer> partInventoryLevelColumn1;

    @FXML
    private TableColumn<Part, Double> partPriceColumn1;

    @FXML
    private Button addAssociatedPartButton;

    @FXML
    private Button removeAssociatedPartButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;
    
    @FXML
    private Label exceptionLabelArea;

    
    private ObservableList<Part> associatedPartsList;


    /**
     * Initializes both the Parts Table View and the Associated parts table view.
     * Determines the next unique product ID by finding the max ID in Inventory of products + 1
     */
    public void initialize()
    {
        int max = 0;
               
        partIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        
        partTableView.getItems().addAll(Inventory.getAllParts());
        
        //set up associated columns for Table View 1
        partIDColumn1.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn1.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevelColumn1.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn1.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        
        
        for(Product item : Inventory.getAllProducts()){
            if (item.getId()> max){
                max = item.getId();
            }
        }
        productIDTextField.setText(Integer.toString(max+1));
        productIDTextField.setDisable(true);
 
        associatedPartsList = FXCollections.observableArrayList();
        
    }
    /**
     * Saves the Product to current Inventory Product list
     * @exception NumberFormatException thrown if productIDTextField, inventoryTextField, productPriceTextField, minTextField, 
     *      maxTextField are not Integers
     * @exception NullPointerException thrown if unable to open FXML.fxml resource
     */
    public void saveButtonListener() {
        ArrayList <String> exceptions = new ArrayList<>(); 
        boolean flag = false;
        boolean emptyflag = false;
        int id =0;
        String name = "";
        int stock=0;
        Double price=0.0;
        int min=0;
        int max=0;
        
        Product newProduct;
        
        //Check to see if any of the textfields are empty. Adds exception to exceptions list
        if(productIDTextField.getText().isEmpty() || productNameTextField.getText().isEmpty() || inventoryTextField.getText().isEmpty()
           || productPriceTextField.getText().isEmpty() || minTextField.getText().isEmpty() || maxTextField.getText().isEmpty()){
            exceptions.add("Exception: Cannot leave field empty");
            emptyflag = true;
        }
        // check to see if the integer textfields are integers. If not, adds an exception to exceptions list
        try {
            id = Integer.parseInt(productIDTextField.getText());
        } catch (NumberFormatException e) {
            if(!emptyflag){
                flag = true;
                exceptions.add("Exception: Part ID must be an integer");
            }
        }
        try {
            stock = Integer.parseInt(inventoryTextField.getText());
        } catch (NumberFormatException e) {
            if(!emptyflag){
                flag = true;
                exceptions.add("Exception: Inventory must be an integer");
            }
        }
        try {
            price = Double.parseDouble(productPriceTextField.getText());
        } catch (NumberFormatException e) {
            if(!emptyflag){
                flag = true;
                exceptions.add("Exception: Price must be a double");
            }
        }
        try {
            min = Integer.parseInt(minTextField.getText());
        } catch (NumberFormatException e) {
            if(!emptyflag){
                flag = true;
                exceptions.add("Exception: Min must be an integer");
            }
        }
        try {
            max = Integer.parseInt(maxTextField.getText());
        } catch (NumberFormatException e) {
            if(!emptyflag){
                flag = true;
                exceptions.add("Exception: Max must be an integer");
            }
        }
        
        
        name = productNameTextField.getText();
        
        //Error given if min is more than max
        if(min >= max){
            flag = true;
            exceptions.add("Exception: Max must be greater than Min");
        }
        //Error given if stock is outside of the min - max range
        if(stock > max || stock < min){
            flag = true;
            exceptions.add("Inventory must be between Max and Min");
        }
        // if either the empty flag or the numeric flag is true, print errors to stage
        if(flag || emptyflag){
            StringBuilder estr = new StringBuilder();
            for(String str: exceptions){
                estr.append(str);
                estr.append("\n");
            }
            exceptionLabelArea.setText(estr.toString());
        }else{
            // if data is validated, add part to Inventory Parts list, close current stage, reopen main stage
            newProduct = new Product(id, name, price, stock, min, max);
            for(Part item : associatedPartsList){
                newProduct.addAssociatedPart(item);
            }
            try{
        
                //Add new part to list
                Inventory.addProduct(newProduct);
       
                //Close current window
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
        
                //Reopen Main page
                Parent newparent = FXMLLoader.load(getClass().getResource("FXML.fxml"));
                Scene scene = new Scene(newparent);
                Stage secondStage = new Stage();
                secondStage.setScene(scene);
                secondStage.show();
            }
            catch(Exception e){
                System.out.println("Exception caught: " + e.toString());
            }
            
        }    
    }
    
    /**
     * Returns to main stage without saving
     * @exception NullPointerException if FXML.fxml is not found 
     */
    public void cancelButtonListener(){
        try{
        
        // Close currebt stage
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        
        //Reopen Main page
        Parent newparent = FXMLLoader.load(getClass().getResource("FXML.fxml"));
        Scene scene = new Scene(newparent);
        Stage secondStage = new Stage();
        secondStage.setScene(scene);
        secondStage.show();
        }catch(Exception e){
            System.out.println("Exception caught: " + e.toString());
        }
    }
    
    /**
     * Adds and associated part to the current product
     * Error: If no part is selected, dialog box displays error
     */
    public void addAssociatedPartButtonListener() {
        if(!partTableView.getSelectionModel().isEmpty()){
            Part partToAdd = partTableView.getSelectionModel().getSelectedItem();
        

            partTableView1.getItems().addAll(partToAdd);
            associatedPartsList.add(partToAdd);
        }
    }
    
    /**
     * Selects parts from the Part Table View using either the Part ID or the Part name
     * Dialog opened if part is not found
     * Highlights part if searched by ID
     * Filters Parts if searched by name
     */
    public void partSearchTextFieldListener(){
        String selection = partSearchTextField.getText();
        boolean numeric = true;
        ObservableList<Part> newList = FXCollections.observableArrayList();

        Stage cancelDialog = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(gridPane, 300,150);
        cancelDialog.setScene(dialogScene);
        
        try {
            Integer num = Integer.parseInt(selection);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        if(numeric){
            for(Part item : Inventory.getAllParts()){
                if(Integer.parseInt(selection)==item.getId()){                   
                    partTableView.getSelectionModel().select(item);
                }
            }
        }else{            
            newList = Inventory.lookupPart(selection);
            partTableView.getItems().clear();
            partTableView.getItems().addAll(newList);
        }
        if(partTableView.getSelectionModel().isEmpty() && newList.size()==0){
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
     * Removes the selected associated part from the associated parts table view and associated parts list
     * Checks to ensure that part is selected
     * Verifies with user to remove associated part.
     */
    public void removeAssociatedPartButtonListener(){
        Stage cancelDialog = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(gridPane, 300,150);
        
        cancelDialog.setScene(dialogScene);
        if(partTableView1.getSelectionModel().isEmpty()){
            Button cancelButton = new Button("Ok");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent arg1){
                cancelDialog.hide();
            }
        });
        Label okToDelete = new Label();
        okToDelete.setText("No part selected to remove");
        gridPane.add(okToDelete,0,2);
        gridPane.add(cancelButton, 200, 100);
     
        cancelDialog.show();
        
            
        }else{
            
            Button okButton = new Button("OK");
            okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0){
                Part selection = partTableView1.getSelectionModel().getSelectedItem();
            
                for(Part item : associatedPartsList){ 
                    if(selection.getId() == item.getId()){
                        associatedPartsList.remove(item);
                        break;
                    }   
                }
                cancelDialog.hide();
                partTableView1.getItems().clear();
                partTableView1.getItems().addAll(associatedPartsList);
            }});
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent arg1){
                cancelDialog.hide();
            }
        });
        Label okToDelete = new Label();
        okToDelete.setText("Are you sure you want to remove?");
        gridPane.add(okToDelete,0,2);
        gridPane.add(okButton, 0, 100);
        gridPane.add(cancelButton, 200, 100);
     
        cancelDialog.show();
        }
        
              
    }

    }

