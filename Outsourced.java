package InventoryManagement;

/**
 *This class extends Part and has a unique attribute for the name of the provider company.
 *
 * @author Brian H Stewart
 */

public class Outsourced extends Part{
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String cName) {
        super(id, name, price, stock, min, max);
        this.companyName = cName;
    }

    /**
     *
     * @param newName the new name of the part producer
     */
    public void setCompanyName(String newName) {
        companyName = newName;
    }

    /**
     *
     * @return the producing company of the part
     */
    public String getCompanyName() {return companyName;}
}
