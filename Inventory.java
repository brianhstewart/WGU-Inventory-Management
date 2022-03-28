package InventoryManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.atomic.AtomicInteger;



/**
 * RUNTIME ERROR: when i started this project i was initializing the OLs with new instead of the FXCollections function
 * because I did not realize the OLs are an abstract class that needs to be initialized differently.  Once i
 * realized what the problem was, it stopped causing problems and i havent had a problem yet with the current version.
 */

/**
 * FUTURE ENHANCEMENT: I think i could have been a lot better during the planning phase of this project if i had the
 * experience i gained while building it.  If i was to return to this project for long term use, i would want to change
 * a few things to make it more useful.
 * I would probably start with getting rid of tempLink and do the work to make entire
 * tempProducts in the add/modify product screens instead.  I think that would reduce the chance of a user finding an
 * unexpected case and leaving data in the tempLink that is no longer wanted.
 */
/**
 * This is the class that holds the static members and classes necessary for this project.
 *
 *
 * @author Brian H Stewart
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Part> tempLink = FXCollections.observableArrayList();
    final static AtomicInteger PART_ID_GEN = new AtomicInteger(1);
    final static AtomicInteger PROD_ID_GEN = new AtomicInteger(1);
    //^^ never used one of these before but its pretty convenient
    //the code was way less compact when i was incrementing a normal integer for every new item

    public static void tempAdd(Part p) {tempLink.add(p);}
    public static void unlink(Part o)  {tempLink.remove(o);}
    public static ObservableList<Part> readLink() {
            return tempLink;
    }
    public static void dumpLink() {tempLink.clear();}

    /**
     *
     * @param newPart is added to the global array of parts
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    /**
     * @param newProduct is added to the global array of products
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     *
     * @param partID is the wanted ID of the part we are looking up
     * @return the part record if it exists, or a null pointer if no part found.
     */
    public static Part lookupPart(int partID) {
        for(Part e : allParts) {
            if (e.getId() == partID) {
                return e;
            }
        }
        return null;
    }

    /**
     *
     * @param productID is the wanted ID of the product we are looking up
     * @return the product record if it exists, or a null pointer if no product found.
     */
    public static Product lookupProduct(int productID) {
        for(Product p: allProducts) {
            if(p.getId() == productID) {
                return p;
            }
        }
        return null;
    }

    /**
     *
     * @param productName is the substring to search against part names
     * @return an Observable List of all parts containing the string put into the function
     */
    public static ObservableList<Part> lookupPart(String productName) {
        ObservableList<Part> holder = FXCollections.observableArrayList();
        for (Part e : allParts) {
            if (e.getName().contains(productName)) {
                holder.add(e);
            }
        }
        return holder;
    }

    /**
     *
     * @param productName is the substring to search against product names
     * @return an OL of all Products with names containing the input string
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> holder = FXCollections.observableArrayList();
        for (Product e : allProducts) {
            if (e.getName().contains(productName)) {
                holder.add(e);
            }
        }
        return holder;
    }

    /**
     *
     * @param index ID of the part being updated
     * @param selectedPart the updated version of the part
     */
    public static void updatePart(int index, Part selectedPart) {
        for (Part e : allParts) {
            if (e.getId() == index) {
                allParts.remove(e);
                allParts.add(selectedPart);
                return;
            }
        }
    }

    /**
     *
     * @param index ID of the Product to update
     * @param selectedProduct the updated version of the Product
     */
    public static void updateProduct(int index, Product selectedProduct) {
        for (Product p : allProducts) {
            if (p.getId() == index) {
                allProducts.remove(p);
                allProducts.add(selectedProduct);
                return;
            }
        }
    }

    /**
     *
     * @param toDelete the part to be deleted from the list of parts
     * @return true if the part was found and removed, false if the part did not exist
     */
    public static boolean deletePart(Part toDelete) {
        for (Part e: allParts) {
            if (e.getId() == toDelete.getId()) {
                allParts.remove(e);
                return true;
            }
        }
        return false;
    }
    /**
     *
     * @param toDelete the product to be deleted from the list of products
     * @return true if the product was found and removed, false if the product did not exist
     */
    public static boolean deleteProduct(Product toDelete) {
        for (Product e: allProducts) {
            if (e.getId() == toDelete.getId()) {
                allProducts.remove(e);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return the OL of all Parts in the program
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     *
     * @return the OL of all Products in the program
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
