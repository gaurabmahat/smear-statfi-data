package fi.tuni.csgr;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Roger Wanamo
 */
public class PrototypeMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PrototypeMain.class.getResource("PrototypeFXML.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(PrototypeMain.class.getResource("prototype.css").toExternalForm());

            primaryStage.setTitle("Project work Prototype");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(PrototypeMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
