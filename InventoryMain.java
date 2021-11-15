package myinventory;


import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author sdeloach
 */
public class InventoryMain extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        
       // Load the FXML file.
        Parent parent = FXMLLoader.load(getClass().getResource("FXML.fxml"));
       
        
        Scene scene = new Scene(parent);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void newMain(String[] args) {
        launch(args);
    }
    
}
