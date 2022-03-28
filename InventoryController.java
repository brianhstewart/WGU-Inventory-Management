package InventoryManagement;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

import static InventoryManagement.Inventory.PART_ID_GEN;
import static InventoryManagement.Inventory.PROD_ID_GEN;

/**
 * This Controller operates all of the GUI elements in this program.
 *
 *This Controller ended up being the bulk of my work on this project, since the demands of a controller program
 * were totally new to me.  This program seems to run well and cover the requirements of the project but
 * I think there are a lot of improvements that could be made using the things i learned while creating it.
 * I found this project very educational since i learned a lot about how to use a GUI along with Thread/runnables,
 * all of which were new to me when i started this class and project.
 * @author Brian H Stewart
 */

public class InventoryController {
        @FXML
        public TextField enterMachineID;
        @FXML
        public TextField enterCName;
        @FXML
        public Button AddProd;

        @FXML
        public Button ModifyProd;

        @FXML
        public Button DeleteProd;

        @FXML
        public Button AddPart;

        @FXML
        public Button ModifyPart;

        @FXML
        public Button DeletePart;

        @FXML
        public TableView<Part> PartResult;

        @FXML
        public TableView<Product> ProductResult;

        @FXML
        public TextField PartSearch;

        @FXML
        public TextField ProductSearch;

        @FXML
        public Button ExitButton;

        @FXML
        public RadioButton ihbutton;

        @FXML
        public ToggleGroup optiongroup;

        @FXML
        public RadioButton osbutton;

        @FXML
        public TextFlow titledisplay;

        @FXML
        public TextField enterID;

        @FXML
        public TextField enterName;

        @FXML
        public TextField enterStock;

        @FXML
        public TextField Price;

        @FXML
        public TextField enterMax;

        @FXML
        public TextFlow IDorCompany;

        @FXML
        public TextField enterIDorCName;

        @FXML
        public TextField enterMin;

        @FXML
        public Button savePart;

        @FXML
        public Button cancelPart;

        @FXML
        public Text titletext;

        @FXML
        private TextField prodMin;

        @FXML
        private TextField prodMax;

        @FXML
        private TextField prodPrice;

        @FXML
        private TextField prodStock;

        @FXML
        private TextField prodName;

        @FXML
        private Button linkPart;

        @FXML
        private TableView<Part> ProdPartsTable;

        @FXML
        private TableView<Part> toAddResult;

        @FXML
        private TextField toAddSearch;

        @FXML
        private Button SaveProduct;

        @FXML
        private Button unlinkPart;

        @FXML
        private TextField prodID;

        @FXML
        private Button cancelProd;

        @FXML
        private Button ModifyProduct;

        public static int validation = -1;

        public static Part select;
        public static Product selectprod;


        /*
         * i wanted to make sure i had a way to access the selection across methods,
         * there are other ways to do this that might be better for a larger project but
         * for something small like this i thought simple would be better.
         * ********************************************************************
         * In hindsight (im writing this part near the end of the project),
         * this project has a lot more duplicated code than i would normally want,
         * and if this was a long term project i think one of the best things to start working
         * on would be to try and consolidate the code where ever possible. I think the best places to
         * begin would be finding a way to consolidate all the table population sequences into one good function
         * and a second good place would be an overhaul of my error messages, since they are still a bit sloppy
         *
         * After some excess testing of all the parts of the project, i am fairly sure it works well but
         * as is it would be somewhat painstaking to edit and make the quality consistent throughout
         * so if this were a project i wanted to use and edit long term I would definitely want to take the
         * time and improve the consistency of the backend.
         *
         * It has the fingerprints of a learner all over it, but hey, I think by the end i was
         * making much better decisions than i did at the start and i guess thats the whole point of a school project
         *¯\_(ツ)_/¯
         */

        /**
         * Tracks the selections from the Part table in a static variable for ease of access across pages.
         */
        public void PartResultListener() {
                select = PartResult.getSelectionModel().getSelectedItem();
        }

        /**
         * Tracks the selections from the Product table in a static variable for ease of access across pages.
         */
        public void ProductResultListener() {
                selectprod = ProductResult.getSelectionModel().getSelectedItem();
        }


        /**
         * exit button controller, asks for confirmation before exiting the program
         */

        public void ExitButtonListener() {
                confBox.popUp();
                if (validation == 1) {
                        Platform.exit();
                }
                else { validation = -1; }
        }

        /**
         * controls the part delete button, only works when a part is selected and asks for confirmation before deletion.
         */
        public void DeletePartListener() {
                try {
                        if (select == null) throw new ArithmeticException();
                        confBox.popUp();
                        if (validation == 1) {
                                Inventory.deletePart(select);
                                fixPartsTables(PartResult, Inventory.getAllParts());
                                validation = -1;
                        } else {
                                validation = -1;
                        }
                }
                catch (Exception e) {
                        if (e instanceof ArithmeticException) {
                                confBox.areYouSure("You must choose a valid Part to delete.");
                        }
                        else {
                                e.printStackTrace();
                        }
                }
        }
        /**
         * controls the product delete button, only works when a product is selected and asks for confirmation before deletion.
         *
         * also sends a user error message if the product has parts that must be unlinked before deletion.
         */
        public void DeleteProductListener() {
                try {
                        if (selectprod == null) throw new ArithmeticException();
                        if (!(selectprod.getAllAssociatedParts().isEmpty())) {
                                confBox.areYouSure("Please unlink all parts from the product\nbefore attempting to delete the product");
                                return;
                        }
                        confBox.popUp();
                        if (validation == 1) {
                                Inventory.deleteProduct(selectprod);
                                fixProductsTables(ProductResult, Inventory.getAllProducts());
                                validation = -1;
                        } else {
                                validation = -1;
                        }
                }
                catch (Exception e) {
                        if (e instanceof ArithmeticException) {
                                confBox.areYouSure("You must choose a valid Product to delete.");
                        }
                        else {
                                e.printStackTrace();
                        }
                }
        }

        /**
         * this opens the add part panel when the add parts button is clicked.
         *
         * @throws IOException if the FXML load does not work
         */
        public void AddPartButtonListener() throws IOException {
                FXMLLoader newpage = new FXMLLoader();
                URL newfxml = getClass().getResource("AddPartForm.fxml");
                newpage.setLocation(newfxml);
                /**
                 * RUNTIME ERROR
                 * Ran into trouble here because my new FXML template for this page
                 * had the name of the controller wrong.  If I run into
                 * class not found exemptions on load again, I probably
                 * goofed the controller name again!! check that first :)
                 */
                Stage window = new Stage();
                Parent page = newpage.load();
                Scene scene = new Scene(page);
                window.setScene(scene);


                window.setTitle("Add Part Form");
                window.initModality(Modality.APPLICATION_MODAL);

                window.show();

        }

        /**
         * When the modify parts button is clicked, this operation loads the
         * window for modifying existing parts. It displays an error message
         * if a part has not been selected, and populates the new page with the info
         * of the selected part when used correctly.
         * @throws IOException if IO operations during load do not work
         */
        public void ModifyPartButtonListener() throws IOException {
                try {
                        if (select == null) throw new ArithmeticException();
                        FXMLLoader newpage = new FXMLLoader();
                        URL newfxml = getClass().getResource("ModifyPartForm.fxml");
                        newpage.setLocation(newfxml);
                        Stage window = new Stage();
                        Parent page = newpage.load();
                        Scene scene = new Scene(page);
                        window.setScene(scene);
                        window.setTitle("Modify Part Form");
                        window.initModality(Modality.APPLICATION_MODAL);
                        Runnable populate = () -> {
                                ModifyPopulate(window);
                        };
                        /**
                         * RUNTIME ERROR
                         * I had a really hard time trying to figure out how to edit pages on the fly in this program
                         * so I ended up using online resources to teach myself the very basics of threads and runnables
                         *
                         * It seems to work fine as long as it is done outside the main thread, so ill be using this method for
                         * the rest of the project when necessary.
                         */
                        Thread pleaseWorkThisTime = new Thread(populate);
                        pleaseWorkThisTime.start();
                        window.showAndWait();


                }
                catch (Exception e) {
                        if (e instanceof ArithmeticException){
                                confBox.areYouSure("Please select a valid Part from the table to modify");
                        }
                        else {
                                e.printStackTrace();
                        }
                }
        }

        /**
         * Populates the modify parts page with info from the selected part when used inside of a thread.
         *
         * must run inside of a thread, since gui elements cannot be changed inside of the main thread.
         * @param s the stage that needs to be populated with this information.
         */
        public void ModifyPopulate(Stage s) {

                Scene scene = s.getScene();
                Parent root = scene.getRoot();
                TextField id = (TextField) root.lookup("#enterID");
                TextField name = (TextField) root.lookup("#enterName");
                TextField stock = (TextField) root.lookup("#enterStock");
                TextField price = (TextField) root.lookup("#Price");
                TextField min = (TextField) root.lookup("#enterMin");
                TextField max = (TextField) root.lookup("#enterMax");
                TextField Mid = (TextField) root.lookup("#enterMachineID");
                TextField CName = (TextField) root.lookup("#enterCName");

                RadioButton ihbut = (RadioButton) root.lookup("#ihbutton");
                RadioButton osbut = (RadioButton) root.lookup("#osbutton");


                id.setPromptText("ID Assigned: " + String.valueOf(select.getId()));
                name.setText(select.getName());
                stock.setText(String.valueOf(select.getStock()));
                price.setText(String.valueOf(select.getPrice()));
                max.setText(String.valueOf(select.getMax()));
                min.setText(String.valueOf(select.getMin()));
                if (select instanceof InHouse) {
                        ihbut.setSelected(true);
                        Mid.setDisable(false);
                        Mid.setText(String.valueOf(((InHouse) select).getMachineId()));
                        CName.setDisable(true);
                }
                else {
                        osbut.setSelected(true);
                        Mid.setDisable(true);
                        CName.setDisable(false);
                        CName.setText( ((Outsourced) select).getCompanyName());
                }
                ihbut.setDisable(true);
                osbut.setDisable(true);

                /**
                 * RUNTIME ERROR:
                 * I have had some difficulties with populating the text fields on the modify page!!
                 * i am embarrassed how long i have been staring at this one problem and getting
                 * it wrong again and again, but i think i will be able to work around this problem.
                 *
                 * I tried accessing the text fields directly but that was impossible,
                 * so i tried a few workarounds that all met with failure.  the only thing
                 * i have tried the seems to have worked so far is using a lookup function
                 * on the root of the modify parts window and assigning local variables to point
                 * at the window's text fields and using those to set the correct text
                 * pulled from the selected part's attributes.
                 *
                 * i have left the failed original skeleton of this function commented in,
                 * in case we want to reference it later.  I know i have made a lot of mistakes during this project,
                 * since both Java and GUI concepts are totally new to me, but i have to keep
                 * reminding myself that powering through your mistakes is the only way
                 * to learn programming.  ¯\_(ツ)_/¯
                 * *******************************************************************************************
                 * RUNTIME ERROR: Part 2!
                 * OK so that got me closer to the answer but we ALSO needed to learn how to create a runnable
                 * and put it on a thread because that is the only way to modify an FXML template
                 * after loading it (as far as i can tell, since nothing else worked.)
                 *
                 * I am very tired of working on this problem, it has been my whole day, but hey
                 * i learned some stuff that i didn't see in the book and that's always a good feeling.
                 */
                /*
                enterID.setPromptText("ID Assigned " + String.valueOf(select.getId()));
                enterName.setText(select.getName());
                enterStock.setText(String.valueOf(select.getStock()));
                Price.setText(String.valueOf(select.getPrice()));
                enterMax.setText(String.valueOf(select.getMax()));
                enterMin.setText(String.valueOf(select.getMin()));
                if (select instanceof InHouse) {
                        ihbutton.setSelected(true);
                        enterMachineID.setDisable(false);
                        enterMachineID.setText(String.valueOf(((InHouse) select).getMachineId()));
                        enterCName.setDisable(true);
                }
                else {
                        osbutton.setSelected(true);
                        enterMachineID.setDisable(true);
                        enterCName.setDisable(false);
                        enterCName.setText( ((Outsourced) select).getCompanyName());
                }
                ihbutton.setDisable(true);
                osbutton.setDisable(true);
                */

        }

        /**
         * Loads the add product form for the user.
         *
         * @throws IOException if the FXML file does not correctly load
         */
        public void addProdButtonListener() throws IOException {
                FXMLLoader newpage = new FXMLLoader();
                URL newfxml = getClass().getResource("AddProduct.fxml");
                newpage.setLocation(newfxml);
                Stage window = new Stage();
                Parent page = newpage.load();
                Scene scene = new Scene(page);
                window.setScene(scene);


                window.setTitle("Add Product Form");
                window.initModality(Modality.APPLICATION_MODAL);

                Parent root = scene.getRoot();
                TableView link = (TableView) root.lookup("#ProdPartsTable");
                TableView parts = (TableView) root.lookup("#toAddResult");

                fixPartsTables(link, Inventory.readLink());
                fixPartsTables(parts, Inventory.getAllParts());


                window.showAndWait();
        }

        /**
         * Loads the Modify Product form when a product is selected, and creates a thread to update that page with
         * the correct product's information.
         *
         * If no product is selected an error message will play instead.  If a product is selected, a separate function
         * is called into a thread for updating the new page created here.
         */
        public void ModifyProdButtonListener() {
                try {
                        if (selectprod == null) throw new ArithmeticException();

                        FXMLLoader newpage = new FXMLLoader();
                        URL newfxml = getClass().getResource("ModifyProduct.fxml");
                        newpage.setLocation(newfxml);
                        Stage window = new Stage();
                        Parent page = newpage.load();
                        Scene scene = new Scene(page);
                        window.setScene(scene);
                        window.setTitle("Modify Part Form");
                        window.initModality(Modality.APPLICATION_MODAL);

                        window.show();
                        ModifyProdPopulate(window);

                }
                catch (Exception e) {
                        if (e instanceof ArithmeticException){
                                confBox.areYouSure("Please select a valid Product from the table to modify");
                        }
                        else {
                                e.printStackTrace();
                        }
                }
        }

        /**
         * Populates the modify product page with the proper info of the selected product.
         *
         * This function must be called inside of a thread, since the main thread cannot change
         * the GUI elements they display.
         * @param s the Stage that the modify parts form is being displayed on
         */
        public void ModifyProdPopulate(Stage s) {
                Scene scene = s.getScene();
                Parent root = scene.getRoot();
                TextField id = (TextField) root.lookup("#prodID");
                TextField name = (TextField) root.lookup("#prodName");
                TextField stock = (TextField) root.lookup("#prodStock");
                TextField price = (TextField) root.lookup("#prodPrice");
                TextField min = (TextField) root.lookup("#prodMin");
                TextField max = (TextField) root.lookup("#prodMax");
                TableView link = (TableView) root.lookup("#ProdPartsTable");
                TableView parts = (TableView) root.lookup("#toAddResult");

                id.setPromptText("ID Assigned: " + String.valueOf(selectprod.getId()));
                name.setText(selectprod.getName());
                stock.setText(String.valueOf(selectprod.getStock()));
                price.setText(String.valueOf(selectprod.getPrice()));
                max.setText(String.valueOf(selectprod.getMax()));
                min.setText(String.valueOf(selectprod.getMin()));
                fixPartsTables(link, selectprod.getAllAssociatedParts());
                fixPartsTables(parts, Inventory.getAllParts());
                link.setPlaceholder(new Label("No Parts for this Product"));

        }

        /**
         * This listener should update the part table with matching results from the search bar.
         *
         * as far as i can tell, it works, but it is a bit inefficient along with
         * the rest of my search functions, so if i came back to this project I would
         * need to work on making those functions better as a group.
         *
         */
        public void PartSearchListener() {
                String content = PartSearch.getText();
                ObservableList<Part> partlist = FXCollections.observableArrayList();
                if (content.equals("")) {
                        partlist.addAll(Inventory.getAllParts());
                }
                else {
                        try {
                                int tempID = Integer.parseInt(content);
                                Part part = Inventory.lookupPart(tempID);
                                if (part != null) {
                                        partlist.add(part);
                                }
                        } catch (Exception e) {
                                if (!(Inventory.lookupPart(content).isEmpty())) {
                                        partlist.addAll(Inventory.lookupPart(content));
                                }
                        }
                }
                Parent root = Main.getPrimaryStage().getScene().getRoot();
                TableView update = (TableView) root.lookup("#PartResult");
                fixPartsTables(update, partlist);
        }

        /**
         * very similar to the Part listener but this one is for the product table.
         *
         */
        public void ProductSearchListener() {
                String content = ProductSearch.getText();
                ObservableList<Product> productlist = FXCollections.observableArrayList();
                productlist.clear();

                if (content.equals("")) {
                        productlist.addAll(Inventory.getAllProducts());
                }
                else {
                        try {
                                int tempID = Integer.parseInt(content);
                                Product prod = Inventory.lookupProduct(tempID);
                                if (prod != null) {
                                        productlist.add(prod);
                                }

                        } catch (Exception e) {
                                if (!(Inventory.lookupProduct(content).isEmpty())) {
                                        productlist.addAll(Inventory.lookupProduct(content));
                                }
                        }
                }
                Parent root = Main.getPrimaryStage().getScene().getRoot();
                TableView update = (TableView) root.lookup("#ProductResult");
                fixProductsTables(update, productlist);

        }
        /**
         * here, we have the listener for saving parts into the system.
         *
         * making and testing this function led me to discovering i was doing a bunch of things with OLs wrong before.
         * they should be correct now, as far as i can tell. This is a simple function that pulls the user
         * input, validates it, and either creates the object or displays an error message if the user made a mistake.
         *
         */
        public void SavePartButtonListener() {
                //input validation
                //fields to validate: id, name, stock, cost, max, min
                //then check the radio buttons to validate either company or machine id
                try {
                        int machineID = 0;
                        String Cname = "default";
                        int id = PART_ID_GEN.getAndIncrement();
                        int stock = Integer.parseInt(enterStock.getText());
                        double price = Double.parseDouble(Price.getText());
                        int min = Integer.parseInt(enterMin.getText());
                        int max = Integer.parseInt(enterMax.getText());
                        if (!(min <= stock && stock <= max)) throw new ArithmeticException();
                        if (ihbutton.isSelected()) {
                                machineID = Integer.parseInt(enterMachineID.getText()); //numeric validation done
                        }
                        else {  Cname = enterCName.getText(); }
                        String name = enterName.getText();
                        /**
                         * for some reason, i am getting a null pointer exception when adding part objects to
                         * the static inventory of parts.  not sure why, part does not appear to be a null pointer
                         * when tested.  will update when this is solved.
                         *
                         * SOLVED: i was instantiating the ObservableLists allParts and allProducts incorrectly
                         * i was treating java like c++ and i forgot that many classes cannot be instantiated through new
                         * and must use other functions to be created.  once i figured out that was the broken piece it
                         * was a simple google search write a more correct version.
                         */
                        if (ihbutton.isSelected()) {
                                InHouse part = new InHouse(id, name, price, stock, min, max, machineID);
                                Inventory.addPart(part);
                        }
                        else {
                                Outsourced part = new Outsourced(id, name, price, stock, min, max, Cname);
                                Inventory.addPart(part);
                        }


                        Window window  = savePart.getScene().getWindow();
                        if (window instanceof Stage) {
                                ((Stage) window).close();
                        }

                        Parent root = Main.getPrimaryStage().getScene().getRoot();
                        TableView update = (TableView) root.lookup("#PartResult");
                        fixPartsTables(update, Inventory.getAllParts());

                } catch (Exception e) {
                        if (e instanceof NumberFormatException) {
                                confBox.areYouSure("Ensure that all numeric fields (such as price or stock)\n" + "are filled with proper numeric values");
                        }
                        else if (e instanceof ArithmeticException){
                                confBox.areYouSure("Please ensure that your current stock is between\n" + "the minimum and maximum stock for this product");
                        }
                        else {
                                e.printStackTrace();
                        }
                }
        }

        /**
         * This button saves the user modifications to an existing part to the part object.
         *
         * I had a very difficult time getting the modify parts page to read in the data from the new part,
         * but after that was solved, this part worked fine on the first try.  So, no errors here as
         * far as i have found unless they come from the more problematic functions.
         *
         * this function takes the edits the user put into the page and applies them to the
         * selected object before exiting to the main page.  if the user tried to
         * use non-numeric values for numeric fields or tried to have an invalid stock, itll
         * warn them to correct the mistake instead of saving.
         */
        public void saveModifiedPartListener() {
                //validation first
                int stock = Integer.parseInt(enterStock.getText());
                int min = Integer.parseInt(enterMin.getText());
                int max = Integer.parseInt(enterMax.getText());
                assert ((stock >= min) && (stock <= max));
                //then reset the selected objects attr and exit
                select.setName(enterName.getText());
                select.setStock(stock);
                select.setMin(min);
                select.setMax(max);
                select.setPrice(Double.parseDouble(Price.getText()));
                if (ihbutton.isSelected()){
                        ((InHouse) select).setMachineId(Integer.parseInt(enterMachineID.getText()));
                }
                else {
                        ((Outsourced) select).setCompanyName(enterCName.getText());
                }
                Window window  = cancelPart.getScene().getWindow();
                if (window instanceof Stage) {
                        ((Stage) window).close();
                }
                Parent root = Main.getPrimaryStage().getScene().getRoot();
                TableView update = (TableView) root.lookup("#PartResult");
                fixPartsTables(update, Inventory.getAllParts());
        }

        /**
         * This button cancels out of Add or Modify Part forms and returns the user to the main page.
         */
        public void cancelButtonListener() {
                Window window  = cancelPart.getScene().getWindow();
                if (window instanceof Stage) {
                        ((Stage) window).close();
                }
        }
        /**
         * This button cancels out of Add or Modify Product forms and returns the user to the main page.
         */
        public void cancelProdButtonListener() {
                Inventory.dumpLink();
                Window window  = cancelProd.getScene().getWindow();
                if (window instanceof Stage) {
                        ((Stage) window).close();
                }
        }

        /**
         * The radio buttons in the Parts forms should restrict the user to
         * the text fields used in the type of part they are creating.
         */
        public void osbuttonListener() {
                enterCName.setDisable(false);
                enterMachineID.setDisable(true);
        }
        /**
         * The radio buttons in the Parts forms should restrict the user to
         * the text fields used in the type of part they are creating.
         */
        public void ihbuttonListener() {
                enterCName.setDisable(true);
                enterMachineID.setDisable(false);
        }

        /**
         * Search bar in the Add/Modify Product forms is used to discover parts to add to the
         * product object.
         *
         * This function allows the user to search the inventory of any parts they might want to link to the
         * Product, or they can clear the bar to see all possible parts.
         *
         *
         */
        public void toAddSearchListener() {
                String content = PartSearch.getText();
                ObservableList<Part> partlist = FXCollections.observableArrayList();
                if (content.equals("")) {
                        partlist.addAll(Inventory.getAllParts());
                }
                else {
                        try {
                                int tempID = Integer.parseInt(content);
                                Part part = Inventory.lookupPart(tempID);
                                if (part != null) {
                                        partlist.add(part);
                                }
                        } catch (Exception e) {
                                if (!(Inventory.lookupPart(content).isEmpty())) {
                                        partlist.addAll(Inventory.lookupPart(content));
                                }
                        }
                }
                Parent root = PartSearch.getScene().getRoot();
                TableView update = (TableView) root.lookup("#toAddResult");
                fixPartsTables(update, partlist);

        }

        /**
         * This button links parts from the search table to the temporary array of parts that is associated with the
         * Product when it is saved.
         *
         * It will do nothing if no part has been selected, and will display a user error message if the user selects an
         * item already associated with the product.  otherwise, it updates the tempLink and the table displaying the
         * tempLink to reflect the change.
         */
        public void linkPartListener() {
                Part toAdd = toAddResult.getSelectionModel().getSelectedItem();
                if (toAdd == null) {
                        return;
                }
                if (Inventory.readLink().contains(toAdd)) {
                        confBox.areYouSure("This Part is already linked to this Product.");

                } else {
                        Inventory.tempAdd(toAdd);
                }
                Parent root = toAddResult.getScene().getRoot();
                TableView update = (TableView) root.lookup("#ProdPartsTable");
                fixPartsTables(update, Inventory.readLink());

        }

        /**
         * This button removes parts from the tempLink to ensure that they are not saved to the object at save time.
         *
         * if nothing is selected then this function does nothing, by exiting early.
         */
        public void unlinkPartListener() {
                // should mirror the function above, same except for how it affects the Inventory.tempLink
                Part outLink = ProdPartsTable.getSelectionModel().getSelectedItem();
                if (outLink == null) {
                        return;
                }
                confBox.popUp();
                if (validation == 0) {
                        validation = -1;
                        return;
                }
                validation = -1;
                if (Inventory.readLink().contains(outLink)) {
                        Inventory.unlink(outLink);
                }

                fixPartsTables(ProdPartsTable, Inventory.readLink());
        }

        /**
         * This function saves the new product to the inventory of all products when the save button is clicked.
         *
         * if the values in the potential product are invalid, such as non-numeric prices or stock outside of
         * the min/max range, this function will send the user an error and direct them back to the page to edit their
         * information.  if the product is valid, the program saves the product into the main array of products
         * and exits to the main page.  it also reads the templink into the object before clearing the templink to
         * keep it clean for the next object.
         */
        public void saveProdListener() {
                try {
                        int stock = Integer.parseInt(prodStock.getText());
                        int min = Integer.parseInt(prodMin.getText());
                        int max = Integer.parseInt(prodMax.getText());
                        if ((stock < min) || (stock > max)) {
                                throw new ArithmeticException();
                        }
                        Product temp = new Product(
                                PROD_ID_GEN.getAndIncrement(),
                                prodName.getText(),
                                Double.parseDouble(prodPrice.getText()),
                                stock, min, max
                        );
                        for (Part p : Inventory.readLink()) {
                                temp.addAssociatedPart(p);
                        }
                        Inventory.dumpLink();
                        Inventory.addProduct(temp);
                        Window window = SaveProduct.getScene().getWindow();
                        if (window instanceof Stage) {
                                ((Stage) window).close();
                        }

                        Parent root = Main.getPrimaryStage().getScene().getRoot();
                        TableView update = (TableView) root.lookup("#ProductResult");
                        fixProductsTables(update, Inventory.getAllProducts());
                }
                catch (Exception e) {
                        if (e instanceof NumberFormatException) {
                                confBox.areYouSure("Ensure that all numeric fields (such as price or stock)\n" + "are filled with proper numeric values");
                        }
                        else if (e instanceof ArithmeticException){
                                confBox.areYouSure("Please ensure that your current stock is between\n" + "the minimum and maximum stock for this product");
                        }
                        else {
                                e.printStackTrace();
                        }
                }
        }

        /**
         * This function is tied to the "modify" button on the modify product page and changes the selected part to
         * match the user input on the form.
         *
         * If the input is invalid, the user will receive an error message and be directed back to the modify
         * Product form to correct their input.
         * otherwise, it edits the selected part to match the new user input before
         * redirecting to the main page.
         * it also uses and clears the templink, to keep it clean for the next product created.
         */
        public void ModifyProdListener() {
                try {
                        //validation first
                        int stock = Integer.parseInt(prodStock.getText());
                        int min = Integer.parseInt(prodMin.getText());
                        int max = Integer.parseInt(prodMax.getText());
                        if ((stock < min) || (stock > max)) {
                                throw new ArithmeticException();
                        }
                        //then reset the selected objects attr and exit
                        selectprod.setName(prodName.getText());
                        selectprod.setStock(stock);
                        selectprod.setMin(min);
                        selectprod.setMax(max);
                        selectprod.setPrice(Double.parseDouble(prodPrice.getText()));
                        selectprod.dropAssociatedParts();
                        if (Inventory.readLink() != null) {
                                for (Part p : Inventory.readLink()) {
                                        selectprod.addAssociatedPart(p);
                                }
                        }
                        Inventory.dumpLink();
                        Window window  = cancelProd.getScene().getWindow();
                        if (window instanceof Stage) {
                                ((Stage) window).close();
                        }
                        Parent root = Main.getPrimaryStage().getScene().getRoot();
                        TableView update = (TableView) root.lookup("#ProductResult");
                        fixProductsTables(update, Inventory.getAllProducts());
                }
                catch (Exception e) {
                        if (e instanceof NumberFormatException) {
                                confBox.areYouSure("Ensure that all numeric fields (such as price or stock)\n" + "are filled with proper numeric values");
                        }
                        else if (e instanceof ArithmeticException){
                                confBox.areYouSure("Please ensure that your current stock is between\n" + "the minimum and maximum stock for this product");
                        }
                        else {
                                e.printStackTrace();
                        }
                }
        }

        /**
         * This function is designed to update the Part tables across this project, whenever
         * an update would be appropriate.
         *
         * I want to create a function that auto updates tables based on the input of
         * what table and what object list.
         * this would be called every time an Part tableview is
         * added, deleted, modified, linked, or unlinked.
         *
         * I have had a hard time updating GUIs on the fly so if things go wrong down the road, retest this function
         * first.
         *
         * NOTES: I originally planned for this to run inside of a thread since that was the only way I could get
         * the modify/populate functions to work, but here that does not work at all!! It seems that you cant reference
         * a window already in use from a new thread, although I'm still reading about why it works that way.
         * It does work when called normally though, so I'm going to leave it as is unless I hit a problem.
         *
         * @param table is the table being populated by this function.
         * @param display is the list of parts filling the table.
         */
        public void fixPartsTables(TableView<Part> table, ObservableList<Part> display) {
                TableColumn<Part, String> nameColumn = new TableColumn<>("Name");
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                TableColumn<Part, String> idColumn = new TableColumn<>("ID");
                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                TableColumn<Part, String> priceColumn = new TableColumn<>("Price");
                priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                table.getColumns().clear();
                if (Inventory.getAllParts().isEmpty()) {
                        table.setPlaceholder(new Label("No Parts Currently Exist"));
                } else { table.setPlaceholder(new Label("No Parts to Display, clear search bar for all Parts")); }

                table.getColumns().add(idColumn);
                table.getColumns().add(nameColumn);
                table.getColumns().add(priceColumn);
                table.setItems(display);
        }

        /**
         * This function is designed to update the Product tables across this project
         * whenever an update would be appropriate.
         *
         * This function is designed to be a mirror of the function above but with the changes appropriate to
         * Product objects, such as a display of how many parts are linked to the product.  This would be called
         * when products are added, deleted, or modified.
         */
        public void fixProductsTables(TableView<Product> table, ObservableList<Product> display) {
                TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                TableColumn<Product, String> idColumn = new TableColumn<>("ID");
                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                TableColumn<Product, String> priceColumn = new TableColumn<>("Price");
                priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                TableColumn<Product, String> linkColumn = new TableColumn<>("# of Parts");
                linkColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
                table.getColumns().clear();

                if (Inventory.getAllProducts().isEmpty()) {
                        table.setPlaceholder(new Label("No Products Currently Exist"));
                } else { table.setPlaceholder(new Label("No Products to Display, clear search bar for all Products")); }

                table.getColumns().add(idColumn);
                table.getColumns().add(nameColumn);
                table.getColumns().add(priceColumn);
                table.getColumns().add(linkColumn);
                table.setItems(display);


        }
}
