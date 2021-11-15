package myinventory;





import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author sdeloach
 * 11/15/20
 * Controller for the AddPart window
 */
public class AddPartController {

    @FXML
    private AnchorPane addPartInHouseScene;

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private ToggleGroup addPartToogleGroup;

    @FXML
    private RadioButton outsourcedRadioButton;

    @FXML
    private TextField partIDTextField;

    @FXML
    private TextField partNameTextField;

    @FXML
    private TextField inventoryTextField;

    @FXML
    private TextField partPriceTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField machineIDTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;
    
    @FXML
    private Label machineIdLabel;
    
    @FXML
    private Label exceptionLabelArea;
     
    
    //private ObservableList<Part> partData;

    /**
     * Sets the Part ID to a unique ID: 
     * Found by the max Part ID in the part data list +1
     * 
     */
    public void initialize()
    {
        int max = 0;
        //partData = PartDataList.getInstance().currentList();
        
        for(Part item : Inventory.getAllParts()){
            if (item.getId()> max){
                max = item.getId();
            }
        }
        partIDTextField.setText(Integer.toString(max+1));
        partIDTextField.setDisable(true);
    }
    
    /**
     * Saves the Part as an InHouse or Outsourced part to current Inventory Parts list
     * @exception NumberFormatException thrown if partIDTextField, inventoryTextField, partPriceTextField, minTextField, 
     *      maxTextField or machineIDTextField are not Integers
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
        int machineId=0;
        String companyName = "";
        Part newPart;
        
        //Check to see if any of the textfields are empty. Adds exception to exceptions list
        if(partIDTextField.getText().isEmpty() || partNameTextField.getText().isEmpty() || inventoryTextField.getText().isEmpty()
           || partPriceTextField.getText().isEmpty() || minTextField.getText().isEmpty() || maxTextField.getText().isEmpty() || machineIDTextField.getText().isEmpty()){
            exceptions.add("Exception: Cannot leave field empty");
            emptyflag = true;
        }
        // check to see if the integer textfields are integers. If not, adds an exception to exceptions list
        try {
            id = Integer.parseInt(partIDTextField.getText());
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
            price = Double.parseDouble(partPriceTextField.getText());
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
        try {
            if(inHouseRadioButton.isSelected())
                machineId = Integer.parseInt(machineIDTextField.getText());
            else
                companyName = machineIDTextField.getText();
        } catch (NumberFormatException e) {
            if(!emptyflag){
                flag = true;
                exceptions.add("Exception: Machine ID must be an integer");
            }
        }
        
        name = partNameTextField.getText();
        
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
        }
        // if data is validated, add part to Inventory Parts list, close current stage, reopen main stage
        else{
            if(inHouseRadioButton.isSelected())
                newPart = new InHouse(id, name, price, stock, min, max, machineId);
            else
                newPart = new Outsourced(id, name, price, stock, min, max, companyName);
            
            //Add new part to list
            Inventory.addPart(newPart);
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
            }catch(Exception e){
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
            System.out.println("Exception caught: " + e);
        }
    }
    
    /**
     * Changes the machineID label to "Company Name"
     */
    public void outsourcedRadioButtonListener(){
        machineIdLabel.setText("Company Name");
        
    }
    /**
     * Changes the machineID label to "Machine ID"
     */
    public void inHouseRadioButtonListener(){
        machineIdLabel.setText("Machine ID");
        
    }

    
}
