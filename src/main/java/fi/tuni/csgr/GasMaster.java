package fi.tuni.csgr;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Roger Wanamo
 */
public class GasMaster extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GasMaster.class.getResource("MainFXML.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(GasMaster.class.getResource("gasmaster.css").toExternalForm());

            primaryStage.setTitle("GasMaster 2000");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(GasMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
