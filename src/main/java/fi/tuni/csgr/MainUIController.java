package fi.tuni.csgr;

import fi.tuni.csgr.components.ControlComponent;
import fi.tuni.csgr.components.ControlContainer;
import fi.tuni.csgr.managers.userdata.ErrorReadingUserDataException;
import fi.tuni.csgr.managers.userdata.ErrorWritingUserDataException;
import fi.tuni.csgr.managers.userdata.UserDataManager;
import fi.tuni.csgr.network.QueryClient;
import fi.tuni.csgr.query.QueriesInfo;
import fi.tuni.csgr.query.Query;
import fi.tuni.csgr.query.QuerySingletonFactory;
import fi.tuni.csgr.utils.Alerts;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainUIController implements Initializable {

    private QuerySingletonFactory queryFactory;
    private ArrayList<Query> currentQuery;
    private QueryClient queryClient;
    private UserDataManager userDataManager = new UserDataManager(System.getProperty("user.dir"));

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
    private void exitApp(ActionEvent event) {
        Stage stage = (Stage) viewPane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    private void handleAboutMenu(ActionEvent event) {

    }

    @FXML
    private void handleLoad(ActionEvent event) {
        try {
            Map<String, ArrayList<String>> loadedSelection = userDataManager.readSelection(querySelector.getValue());
            currentQuery.forEach(query -> query.setSelectionData(loadedSelection));
        } catch (ErrorReadingUserDataException e) {
            Alerts.showInformationAlert("Error reading the saved data");
        } catch (FileNotFoundException e) {
            Alerts.showInformationAlert("No file with saved data found");
        }

    }

    @FXML
    private void handleQuerySelector(ActionEvent event) {
        ArrayList<String> queries = QueriesInfo.queryMap.get(querySelector.getValue());
        setQueries(queries);
    }

    /**
     * Fetches Query objects and adds controls and results for each query to the view.
     *
     * @param queries List of all included subqueries
     */
    private void setQueries(ArrayList<String> queries) {
        controlsContainer.getChildren().clear();
        VBox resultsVBox = new VBox();
        currentQuery.clear();
        queries.forEach(query -> {
            // Get query instance from factory
            Query newQuery = queryFactory.getInstance(query);
            currentQuery.add(newQuery);

            // Add query controls
            controlsContainer.getChildren().add(new Separator());
            Text source = new Text(query.toUpperCase());
            source.getStyleClass().add("query-source");
            controlsContainer.getChildren().add(source);
            newQuery.getControls().forEach(component ->
                    controlsContainer.getChildren().add(new ControlContainer(component)));

            // Add query results to results view
            Pane resultView = newQuery.getResultView();
            resultView.prefWidthProperty().bind(Bindings.add(-38, viewPane.widthProperty()));
            resultsVBox.getChildren().add(resultView);
        });
        viewPane.setContent(resultsVBox);
    }



    @FXML
    private void handleSave() {
        ArrayList<String> missingValues = new ArrayList<>();
        currentQuery.forEach(query -> {
            boolean queryValid = true;
            for (ControlComponent component : query.getControls()) {
                if (!component.selectionValid()) {
                    queryValid = false;
                    missingValues.add(component.getLabel());
                }
            }
            if (queryValid) {
                HashMap<String, ArrayList<String>> allSelections = new HashMap<>();
                currentQuery.forEach(q -> allSelections.putAll(q.getSelectionData()));
                try {
                    userDataManager.saveSelection(querySelector.getValue(), allSelections);
                    Alerts.showInformationAlert("Selection saved!");
                } catch (ErrorWritingUserDataException e) {
                    Alerts.showInformationAlert("Problem saving selection");
                }
            }
            else
                Alerts.showInformationAlert("Please select " + String.join(", ", missingValues));
            return;
        });
    }

    @FXML
    private void handleShowBtn(ActionEvent event) {
        ArrayList<String> missingValues = new ArrayList<>();
        currentQuery.forEach(query -> {
            boolean queryValid = true;
            for (ControlComponent component : query.getControls()) {
                if (!component.selectionValid()) {
                    queryValid = false;
                    missingValues.add(component.getLabel());
                }
            }
            if (queryValid) {
                queryClient.performQuery(query);

            }
            else
                Alerts.showInformationAlert("Please select " + String.join(", ", missingValues));
                return;
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentQuery = new ArrayList<>();
        querySelector.getItems().addAll(QueriesInfo.queryList);
        queryFactory = new QuerySingletonFactory();
        queryClient = new QueryClient();
        footerText.setText(defaultText);
    }
}
