package myinventory;




import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author sdeloach
 * 11/15/20
 * Controller for the Modify Part Stage
 */
public class ModifyPartInHouseController {

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private RadioButton outsourcedRadioButton;

    @FXML
    private TextField partIDTextField;

    @FXML
    private TextField partNameTextField;

    @FXML
    private TextField inventoryTextField;

    @FXML
    private TextField priceTextField;

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
    private Label machineIdTextLabel;
    
    @FXML
    private Label exceptionLabelArea;
  
    private InHouse currentPart;
    private Outsourced currentOutPart;
    //private ObservableList<Part> partData;
    private Part partToModify;

    /**
     * No initialization
     *
     */
    public void initialize()
    {
   
    }
    
     /**
     * Saves the Part as an InHouse or Outsourced part to current Inventory Part list with data validation
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
        
        // checks if any of the textfields have been left empty
        if(partIDTextField.getText().isEmpty() || partNameTextField.getText().isEmpty() || inventoryTextField.getText().isEmpty()
           || priceTextField.getText().isEmpty() || minTextField.getText().isEmpty() || maxTextField.getText().isEmpty() || machineIDTextField.getText().isEmpty()){
            exceptions.add("Exception: Cannot leave field empty");
            emptyflag = true;
        }
        //Check each of the numeric textfields. Add an exception to the list if the textfield is non-numeric
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
        
        // if one of the flags is true, add the exceptions in the list to the exceptions label
        if(flag || emptyflag){
            StringBuilder estr = new StringBuilder();
            for(String str: exceptions){
                estr.append(str);
                estr.append("\n");
            }
            exceptionLabelArea.setText(estr.toString());
        }
        // All data validated, adds the part to the list as either an InHouse or Outsourced part
        else{
            if(inHouseRadioButton.isSelected()){
                newPart = new InHouse(id, name, price, stock, min, max, machineId);
                
                
            }else{
                newPart = new Outsourced(id, name, price, stock, min, max, companyName);
                
            }
            Inventory.deletePart(partToModify);

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
     * 
     * Returns to main page without saving
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
     * 
     * Changes machineIDTextLabel to "Company Name" for an outsourced part
     * Sets the content for the current part to be modified.
     * Selects outsourcedRadioButton
     */
    public void outsourcedRadioButtonListener(){
        
        this.setContent();
        machineIdTextLabel.setText("Company Name");
        outsourcedRadioButton.setSelected(true);
        
    }
    
    /**
     * Changes machineIDTextLabel to "Machine ID" for an InHouse part
     * Sets the content for the current part to be modified.
     * Selects inHouseRadioButton
     */
    public void inHouseRadioButton(){
        
        this.setContent();
        machineIdTextLabel.setText("Machine ID");
        inHouseRadioButton.setSelected(true);
    }
    
    /**
     * Sets the part to be modified
     * @param partToModify the Part to be modified
     */
    public void setPart(Part partToModify){
       this.partToModify = partToModify;
        
    }
    
    /**
     * Sets the text fields to the appropriate values based on the current part to be modified
     */
    public void setContent(){
        // If the partToModify is and InHouse part, use currentPart
        if(partToModify instanceof InHouse){
            inHouseRadioButton.setSelected(true);
            currentPart = (InHouse)partToModify;
            
            partIDTextField.setText(Integer.toString(currentPart.getId()));
            partIDTextField.setDisable(true);
            partNameTextField.setText(currentPart.getName());
            inventoryTextField.setText(Integer.toString(currentPart.getStock()));
            priceTextField.setText(Double.toString(currentPart.getPrice()));
            maxTextField.setText(Integer.toString(currentPart.getMax()));
            minTextField.setText(Integer.toString(currentPart.getMin()));
            machineIDTextField.setText(Integer.toString(currentPart.getMachineId()));
        }
        // If the partToModify is an Outsourced part use currentOutPart
        else if(partToModify instanceof Outsourced){
            
            currentOutPart = (Outsourced)partToModify;
            outsourcedRadioButton.setSelected(true);
            partIDTextField.setText(Integer.toString(currentOutPart.getId()));
            partIDTextField.setDisable(true);
            partNameTextField.setText(currentOutPart.getName());
            inventoryTextField.setText(Integer.toString(currentOutPart.getStock()));
            priceTextField.setText(Double.toString(currentOutPart.getPrice()));
            maxTextField.setText(Integer.toString(currentOutPart.getMax()));
            minTextField.setText(Integer.toString(currentOutPart.getMin()));
            machineIDTextField.setText(currentOutPart.getCompanyName());
            machineIdTextLabel.setText("Company Name");
        }
   
        
    }

}

    
