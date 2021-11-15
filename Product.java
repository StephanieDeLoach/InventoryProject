
package myinventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author sdeloach
 * 
 */
public class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;  
    
    
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.associatedParts = FXCollections.observableArrayList();
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    /**
     * @param part add Part to Associated Parts list
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart removed from Associated Parts list
     * @return boolean: true if the part is found and deleted, false otherwise
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        for(int i = 0; i < associatedParts.size(); i++){
            if(associatedParts.get(i).equals(selectedAssociatedPart)){
                associatedParts.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return list of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
