package fi.tuni.csgr;

import fi.tuni.csgr.components.ControlComponent;
import fi.tuni.csgr.components.ControlContainer;
import fi.tuni.csgr.network.QueryClient;
import fi.tuni.csgr.query.QueriesInfo;
import fi.tuni.csgr.query.Query;
import fi.tuni.csgr.query.QuerySingletonFactory;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainUIController implements Initializable {

    private QuerySingletonFactory queryFactory;
    private Query currentQuery;
    private QueryClient queryClient;

    private String defaultText = "COMP.SE.110 2022 Project work - By CSGR";

    @FXML
    private VBox chartContainer;

    @FXML
    private VBox controlsContainer;

    @FXML
    private ComboBox<String> querySelector;

    @FXML
    private Label footerText;

    @FXML
    private ScrollPane viewPane;

    @FXML
    void exitApp(ActionEvent event) {
        Stage stage = (Stage) viewPane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void handleAboutMenu(ActionEvent event) {

    }

    @FXML
    void handleLoad(ActionEvent event) {

    }

    @FXML
    void handleQuerySelector(ActionEvent event) {
        controlsContainer.getChildren().clear();
        currentQuery = queryFactory.getInstance(querySelector.getValue());
        currentQuery.getControls().forEach(component ->
                controlsContainer.getChildren().add(new ControlContainer(component)));
        Pane resultView = currentQuery.getResultView();
        resultView.prefWidthProperty().bind(Bindings.add(-38, viewPane.widthProperty()));
        viewPane.setContent(resultView);
    }

    @FXML
    void handleSave(ActionEvent event) {

    }

    @FXML
    void handleShowBtn(ActionEvent event) {
        boolean queryValid = true;
        ArrayList<String> missingValues = new ArrayList<>();
        for (ControlComponent component : currentQuery.getControls()) {
            if (!component.selectionValid()) {
                queryValid = false;
                missingValues.add(component.getLabel());
            }
        }
        if (queryValid) {
            queryClient.performQuery(currentQuery);

        }
        else
            showAlert("Please select " + String.join(", ", missingValues));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        querySelector.setItems(QueriesInfo.queryList);
        queryFactory = new QuerySingletonFactory();
        queryClient = new QueryClient();
        footerText.setText(defaultText);
    }

    public void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
