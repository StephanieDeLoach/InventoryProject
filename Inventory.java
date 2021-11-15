package myinventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author sdeloach
 * Inventory class to keep track of parts and products
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();;
    
    /**
     * 
     * @param newPart the part to add
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    
    /**
     * 
     * @param newProduct the product to add
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    
    /**
     * 
     * @param partId the part id to find
     * @return the Part 
     */
    public static Part lookupPart(int partId){
        for(int i = 0; i < allParts.size();i++)
        {
            if(allParts.get(i).getId() == partId)
            {
                return allParts.get(i);
            }

        }
        return null;
    }
    
    /**
     * 
     * @param productId the id of the product to find
     * @return the Product
     */
    public static Product lookupProduct(int productId){
        for(int i = 0; i < allProducts.size();i++)
        {
            if(allProducts.get(i).getId() == productId)
            {
                return allProducts.get(i);
            }
        }
        return null;
    }
    
    /**
     * 
     * @param partName the name of the part to find
     * @return a list of the parts that contain partName
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> listToReturn = FXCollections.observableArrayList();
        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getName().toLowerCase().contains(partName.toLowerCase()))
            {
                listToReturn.add(allParts.get(i));
            } 
        }
        return listToReturn;
    }
    
    /**
     * 
     * @param productName the name of the product to find
     * @return a list of products that contain productName 
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> listToReturn = FXCollections.observableArrayList();
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getName().toLowerCase().contains(productName.toLowerCase()))
            {
                listToReturn.add(allProducts.get(i));
            } 
        }
        return listToReturn;
        
    }
    
    /**
     * 
     * @param index the index of the part to update
     * @param selectedPart the new information for the selected Part
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.remove(index);
        allParts.add(index, selectedPart);
    }
    
    /**
     * 
     * @param index the index of the product to update
     * @param selectedProduct the new information for the selected Product
     */
    public static void updateProduct(int index, Product selectedProduct){
        allProducts.remove(index);
        allProducts.add(index, selectedProduct);
    }
    
    /**
     * 
     * @param selectedPart Part to delete
     * @return true if part is successfully deleted, false otherwise
     */
    public static boolean deletePart(Part selectedPart){
        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).equals(selectedPart)){
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @param selectedProduct Product to delete
     * @return true if product is successfully deleted, false otherwise
     */
    public static boolean deleteProduct(Product selectedProduct){
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).equals(selectedProduct)){
                allProducts.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @return the list of all parts
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    
    /** 
     * 
     * @return the list of all products
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
    
}
