package InventoryManagement;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *  This class holds the methods for two types of pop-up windows.
 *
 *  I originally wrote this extra class for fun, but im glad i did because it gave me an easy
 * place to put the areYouSure class that i wrote later and used to redirect people who made errors when
 *  creating objects.  I havent had any problems with these two pop up windows because they are both very simple.
 *
 *  It was good practice to make windows without scene builder's help, but i think writing out
 *  any of the more complicated pages without scene builder would just take too long to be honest.
 */
public class confBox {

    /**
     * set validation a 1 to confirm action, or 0 to cancel action
     */
    public static void popUp() {
        Stage window = new Stage();
        window.setTitle("Confirmation Window");
        window.initModality(Modality.APPLICATION_MODAL);
        Label label = new Label();
        label.setText("Are you sure this decision is final?");
        Button no = new Button("No");
        no.setOnAction(e -> {
            InventoryController.validation = 0;
            window.close();
        });
        Button yes = new Button("Yes");
        yes.setOnAction(e -> {
            InventoryController.validation = 1;
            window.close();
        });
        VBox holder = new VBox(10);
        holder.setMinWidth(250);
        HBox buttonHolder = new HBox(80);
        holder.getChildren().addAll(label,buttonHolder);
        buttonHolder.getChildren().addAll(yes, no);
        holder.setAlignment(Pos.CENTER);
        buttonHolder.setAlignment(Pos.CENTER);
        Scene scene = new Scene(holder);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * I wrote this window format near the end of the project when i decided that would be the easiest way
     * to redirect people who made logic or numeric errors while making objects in the program.
     *
     * "FUTURE ENHANCEMENT" I think that i could make this class more useful by extending it into types of error messages
     * instead of entering an error message every time, but this project is small enough that it was faster to do
     * each error message by hand.
     * @param error is a string the holds the error message the pop up must display for the user to see before clicking away
     */
    public static void areYouSure(String error) {
        Stage window = new Stage();
        window.setTitle("Error Window");
        window.initModality(Modality.APPLICATION_MODAL);
        Label label = new Label();
        label.setText(error);
        Button ok = new Button("OK");
        ok.setOnAction(e -> {
            window.close();
        });

        VBox holder = new VBox(10);
        holder.setMinWidth(250);
        holder.setAlignment(Pos.CENTER);
        holder.getChildren().addAll(label, ok);
        Scene scene = new Scene(holder);
        window.setScene(scene);
        window.showAndWait();
    }
}
