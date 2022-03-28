package InventoryManagement;




import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;


/**
 * The main function loads the main page before leaving its running operations to the InventoryController.
 *
 *  FUTURE ENHANCEMENT: If I returned to this project, the first and most vital thing that needs work is
 *  the ability to save information between loads of the program, since the base specs did not call for
 *  the ability to read and save to file but it would be a nice function in a program like this.
 *  otherwise, I think a few parts of the controller I wrote could be vastly improved
 *  with some time and care.  some of the functions are very similar, and would benefit from being extensions of
 *  some core function to keep the operation more consistent and easier to edit.  I also think that it would be nice if
 *  the TableViews in the project would auto refresh when the user changes pages, but im not quite sure how to do that
 *  right now without causing problems and it isn't vital for the project to work so i'll leave it to my future self.
 *  Controllers and GUI work were new to me in this course, so with a little more experience i think they
 *  could be much more efficient and user friendly.
 *
 *  The future enhancement note above covers all of my important thoughts, but i also added similar notes to places in
 *  the controller that i thought could use more specific notes.  Sorry to any future reader, including myself,
 *  for being a bit of a perfectionist when it came to writing notes to myself.
 *
 * The Javadoc documentation is in a folder called "javadoc" which will be inside the main zip folder alongside
 * a folder called InventoryManagement which contains the rest of the project files.
 *
 * date -- 07/09/2021 is the day i finished the base version of this program
 * @author Brian H Stewart
 */
public class Main extends Application {

    private static Stage pStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
        Parent root = FXMLLoader.load(getClass().getResource("InventoryManagement.fxml"));
        primaryStage.setTitle("Inventory Management");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

         */
        /**
         * RUNTIME ERROR:
         * I'm not 100% sure why the code below worked,
         * I've tried code based on a couple of online solutions but
         * using a URL var worked when the others didn't
         * so i'm going to leave it as is unless it becomes a problem.
         *
         * before switching to this implementation i received problems
         * and exceptions every time i tired to load the FXML
         * and the system suggested setting the root before loading,
         * which also crashed.
         * ¯\_(ツ)_/¯
         */
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("InventoryManagement.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        //initialize
        TableView part = (TableView) root.lookup("#PartResult");
        TableView prod = (TableView) root.lookup("#ProductResult");
        InventoryController ic = new InventoryController();
        ic.fixPartsTables(part, Inventory.getAllParts());
        ic.fixProductsTables(prod, Inventory.getAllProducts());
        //done with init
        primaryStage.show();
        setPrimaryStage(primaryStage);
    }




    public static void main(String[] args) {
        launch(args);

    }

    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        Main.pStage = pStage;
    }
}
