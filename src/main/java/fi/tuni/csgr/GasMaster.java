package fi.tuni.csgr;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Roger Wanamo
 */
public class GasMaster extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GasMaster.class.getResource("MainUI.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(GasMaster.class.getResource("gasmaster.css").toExternalForm());

            primaryStage.setTitle("GasMaster 2000");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            primaryStage.setMaximized(true);
            primaryStage.show();

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Quit application");
                    alert.setHeaderText(null);
                    alert.setGraphic(null);
                    alert.setContentText("Are you sure you want to quit?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        Platform.exit();
                    } else {
                        event.consume();
                    }
                }
            });
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
