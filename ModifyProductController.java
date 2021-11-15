package myinventory;




import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;



/**
 *
 * @author sdeloach
 * 11/15/20
 * Controller for the Modify Product page
 */
public class ModifyProductController {

    @FXML
    private TextField productIDTextField;

    @FXML
    private TextField productNameTextField;

    @FXML
    private TextField inventoryTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private TextField searchPartTextField;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Part, Integer> partIDColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Integer> partInventoryLevelColumn;

    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    @FXML
    private TableView<Part> AssociatedPartTableView;

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

    
    private Product currentProduct;
    private ObservableList<Part> associatedPartsList;

    /**
     * No initialization needed
     */
    public void initialize()
    {
          
    }
    
    /**
     * Saves the Product to current product Data list
     * @exception NumberFormatException thrown if partIDTextField, inventoryTextField, partPriceTextField, minTextField, 
     *      maxTextField or machineIDTextField are not Integers
     * @exception NullPointerException thrown if unable to open FXML.fxml resource
     */
    public void saveButtonListener() {

        //Get the text from all textfields and save into new Product
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
        
        // checks if any of the textfields have been left empty
        if(productIDTextField.getText().isEmpty() || productNameTextField.getText().isEmpty() || inventoryTextField.getText().isEmpty()
           || priceTextField.getText().isEmpty() || minTextField.getText().isEmpty() || maxTextField.getText().isEmpty()){
            exceptions.add("Exception: Cannot leave field empty");
            emptyflag = true;
        }
        //Check each of the numeric textfields. Add an exception to the list if the textfield is non-numeric
        try {
            id = Integer.parseInt(productIDTextField.getText());
        } catch (NumberFormatException e) {
            if(!emptyflag){
                flag = true;
                exceptions.add("Exception: Product ID must be an integer");
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
            price = Double.parseDouble(priceTextField.getText());
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
        
        // if one of the flags is true, add the exceptions in the list to the exceptions label
        if(flag || emptyflag){
            StringBuilder estr = new StringBuilder();
            for(String str: exceptions){
                estr.append(str);
                estr.append("\n");
            }
            exceptionLabelArea.setText(estr.toString());
        }
        else{
            //Remove the modifed product from the product data list
            for(Product item : Inventory.getAllProducts()){
                if(item.getId()== Integer.parseInt(productIDTextField.getText())){
                    Inventory.deleteProduct(item);
                    break;
                }
            }
            newProduct = new Product(id, name, price, stock, min, max);
            for(Part item : associatedPartsList){
                newProduct.addAssociatedPart(item);
            }
        
            //Add new part to list
            Inventory.addProduct(newProduct);
            try{
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
                System.out.println("Caught exception: " + e.toString());
            }
        }
    }
    
    /**
     * Returns to main stage without saving
     * @exception NullPointerException thrown if unable to open FXML.fxml resource
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
            System.out.println("Caught exception: " + e.toString());
        }
    }
    
    /**
     * set the product to modify
     * @param productToModify the product to modify
     */
    public void setProduct(Product productToModify){
        
        currentProduct = productToModify;
        
    }
    
    /**
     * Sets the text fields to the appropriate values based on the current product to be modified
     */
    public void setContent(){
        
        associatedPartsList = FXCollections.observableArrayList();
        
        // Disables ID text field
        productIDTextField.setText(Integer.toString(currentProduct.getId()));
        productIDTextField.setDisable(true);
        
        // sets the TextFields to the currect product's information
        productNameTextField.setText(currentProduct.getName());
        inventoryTextField.setText(Integer.toString(currentProduct.getStock()));
        priceTextField.setText(Double.toString(currentProduct.getPrice()));
        maxTextField.setText(Integer.toString(currentProduct.getMax()));
        minTextField.setText(Integer.toString(currentProduct.getMin()));
        
        //sets the identifiers for the columns of the part Table View
        partIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        
        // adds the data in the current part data list to the part table view
        partTableView.getItems().setAll(Inventory.getAllParts());
        
        //set up associated columns for Table View 1 (associated parts table)
        partIDColumn1.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn1.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevelColumn1.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn1.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        
        associatedPartsList = currentProduct.getAllAssociatedParts();
        AssociatedPartTableView.getItems().addAll(associatedPartsList);
        
    }
    
    /**
     * Adds and associated part to the current product
     * Error: If no part is selected, dialog box displays error
     */
    public void addAssociatedPartButtonListener() {
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
        }
        // If part has been selected, add to associated part table view and associated parts list
        else{
            Part partToAdd = partTableView.getSelectionModel().getSelectedItem();
             
            AssociatedPartTableView.getItems().addAll(partToAdd);
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
        String selection = searchPartTextField.getText();
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
            for(Part item : Inventory.getAllParts()){
                if(item.getName().toLowerCase().contains(selection.toLowerCase())){
                    newList.add(item);
                }
            }
            if(newList.size() > 0){
                partTableView.getItems().clear();
                partTableView.getItems().addAll(newList);
            }
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
        // Check to see if part is selected. Displays error in dialog box if not
        if(AssociatedPartTableView.getSelectionModel().isEmpty()){
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
        
            
        }
        // If part selected, removes the part from the associated part table view and associated parts list
        else{
            
            Button okButton = new Button("OK");
            okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0){
            Part selection = AssociatedPartTableView.getSelectionModel().getSelectedItem();
        
                for(Part item : associatedPartsList){ 
                    if(selection.getId() == item.getId()){
                        associatedPartsList.remove(item);
                        break;
                    }
                }
                cancelDialog.hide();
                AssociatedPartTableView.getItems().clear();
                AssociatedPartTableView.getItems().addAll(associatedPartsList);
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
