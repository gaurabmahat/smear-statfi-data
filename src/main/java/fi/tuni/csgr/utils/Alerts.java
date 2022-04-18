package fi.tuni.csgr.utils;

import javafx.scene.control.Alert;

/**
 * Class for alert displays
 */

public class Alerts {

    /**
     * Show neutral information alert.
     *
     * @param msg Alert message
     */

    public static void showInformationAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
