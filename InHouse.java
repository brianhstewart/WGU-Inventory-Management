package InventoryManagement;

/**
 * This class extends Part and has a unique attribute for production machine ID.
 *
 * @author Brian H Stewart
 */

public class InHouse extends Part{
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineId = machineID;
    }

    /**
     *
     * @param ID the new machine ID for this part
     */
    public void setMachineId(int ID) {machineId = ID;}

    /**
     *
      * @return the machine ID of the part
     */
    public int getMachineId() {return machineId;}

}
