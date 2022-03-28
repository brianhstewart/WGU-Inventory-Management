package InventoryManagement;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * This class template is for Products, which can hold multiple Parts alongside its own attributes.
 *
 * @author Brian H Stewart
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private int count = 0;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

/**
  *@param id new id to set
*/
    public void setID(int id) {this.id = id;}

    /**
     *  @return the id of the product
     */
    public int getId() {return id;}

    /**
     *
     * @param name new name to set
     */
    public void setName(String name) {this.name = name;}

    /**
     *
     * @return the name of the item
     */
    public String getName() {return name;}

    /**
     *
     * @param price the new price to set
     */
    public void setPrice(double price) {this.price = price;}

    /**
     *
     * @return the price of the item
     */
    public double getPrice() {return price;}

    /**
     *
     * @param stock is the adjusted stock of the product
     */
    public void setStock(int stock) {this.stock = stock;}

    /**
     *
     * @return the currents stock of the product
     */
    public int getStock() {return stock;}

    /**
     *
     * @param min new minimum required stock of product
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *
     * @return minimum required stock of this product
     */
    public int getMin() {
        return min;
    }

    /**
     *
     *@param max the new max for on-hand product
     */

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the max on-hand prodcut
     */
    public int getMax() {
        return max;
    }

    /**
     *
     * @param part to be added
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
        setCount(associatedParts.size());

    }

    /**
     *
     * @param part to be removed from list
     * @return true if deleted, false if not present at operation
     */
    public boolean deleteAssociatedPart(Part part) {
        if (associatedParts.contains(part)) {
            associatedParts.remove(part);
            setCount(associatedParts.size());

            return true;
        }
        else {return false;}
    }

    /**
     *
     * @return a copy of the list of associated parts for this product
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * this function clears the associated parts list to ready the object for reading in the modified parts list.
     */
    public void dropAssociatedParts() {
        associatedParts.clear();
        setCount(associatedParts.size());

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
