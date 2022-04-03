package fi.tuni.csgr;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import fi.tuni.csgr.components.ChartView;
import fi.tuni.csgr.managers.graphs.GraphDataManager;
import fi.tuni.csgr.network.Network;
import fi.tuni.csgr.network.SmearNetwork;
import fi.tuni.csgr.utils.DatePickerUtils;
import fi.tuni.csgr.utils.MenuUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static fi.tuni.csgr.stationNames.stationNameMapping.mapStationAndGas;

/**
 * FXML Controller class
 *
 * @author Roger Wanamo
 */
public class MainFXMLController implements Initializable {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tab1;
    @FXML
    private AnchorPane t1_anchorPane;
    @FXML
    private ScrollPane t1_scrollPane;
    @FXML
    private MenuButton t1_mb_gas;
    @FXML
    private DatePicker t1_datePicker_from;
    @FXML
    private DatePicker t1_datePicker_to;
    @FXML
    private MenuButton t1_mb_stations;
    @FXML
    private ComboBox<String> t1_cb_display;
    @FXML
    private Button btn_show;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_load;
    @FXML
    private StackPane t1_txt_pane;
    @FXML
    private Text t1_txt_select;
    @FXML
    private VBox t1_graphBox;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;

    private ArrayList<String> selectedGases = new ArrayList<>();
    private ArrayList<String> selectedStations = new ArrayList<>();

    private Map<String, ChartView> charts;

    private LocalDate fromDate;
    private LocalDate toDate;

    private GraphDataManager graphDataManager;
    private Network smearNetwork;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        t1_scrollPane.prefWidthProperty().bind(Bindings.add(-38, t1_anchorPane.widthProperty()));
        t1_scrollPane.prefHeightProperty().bind(Bindings.add(-65, t1_anchorPane.heightProperty()));

        charts = new HashMap<>();

        graphDataManager = new GraphDataManager();
        smearNetwork = new SmearNetwork(graphDataManager);
        addGraphListeners(graphDataManager);

        fromDate = LocalDate.now().minusDays(2);
        t1_datePicker_from.setValue(LocalDate.now().minusDays(2));
        toDate = LocalDate.now();
        t1_datePicker_to.setValue(LocalDate.now());

        DatePickerUtils.restrictDatePicker(t1_datePicker_from, LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now());
        DatePickerUtils.restrictDatePicker(t1_datePicker_to, fromDate, LocalDate.now());

        ArrayList<String> stationList = new ArrayList<>();
        mapStationAndGas.forEach((k,v) -> stationList.add(k) );
        selectedStations = MenuUtils.createCheckboxMenuItems(stationList, t1_mb_stations, "Station");

        ArrayList<String> gasList = new ArrayList<>();
        mapStationAndGas.get(stationList.get(0)).forEach((k,v) -> gasList.add(k));
        selectedGases = MenuUtils.createCheckboxMenuItems(gasList, t1_mb_gas, "Gas");

        // Not yet implemented
        ObservableList<String> displayList = FXCollections.observableArrayList("selection", "selection", "selection", "selection");
        t1_cb_display.setItems(displayList);
    }

    /**
     * Adds listeners to graphDataManager, which will add or remove charts when gases are added or removed from the query.
     *
     * @param graphDataManager
     */
    private void addGraphListeners(GraphDataManager graphDataManager) {
        MapChangeListener mainListener = new MapChangeListener() {
            @Override
            public void onChanged(Change change) {
                String name = change.getKey().toString();
                if(change.wasRemoved()) {
                    t1_graphBox.getChildren().remove(charts.get(name).getChartBox());
                    charts.remove(name);
                }
                if(change.wasAdded()) {
                    charts.put(name, new ChartView(name, (ObservableList<Series<Long, Double>>) change.getValueAdded()));
                    t1_graphBox.getChildren().add(charts.get(name).getChartBox());
                }
            }
        };
        graphDataManager.addMainListener(mainListener);
    }

    @FXML
    private void handleT1DatePickerFrom(ActionEvent event) {
        fromDate = t1_datePicker_from.getValue();
        if (fromDate.isAfter(toDate)) {
            toDate = fromDate;
            t1_datePicker_to.setValue(fromDate);
        }
        DatePickerUtils.restrictDatePicker(t1_datePicker_to, fromDate, LocalDate.now());
    }

    @FXML
    private void handleT1DatePickerTo(ActionEvent event) {
        toDate = t1_datePicker_to.getValue();
    }

    @FXML
    private void handleT1CbDisplay(ActionEvent event) {
    }

    @FXML
    private void handleSaveBtn(ActionEvent event) {
        t1_txt_select.setVisible(true);
    }

    @FXML
    private void handleLoadBtn(ActionEvent event) {
    }

    @FXML
    private void handleShowBtn(ActionEvent event) {
        if (selectedGases.isEmpty() | selectedStations.isEmpty()) {
            showAlert("Please select a gas and a station to show data.");
        }
        else {
            LocalDateTime from = fromDate.atStartOfDay();
            LocalDateTime to = toDate.atTime(23, 59);
            smearNetwork.getData(from, to, "MAX", selectedGases, selectedStations);
            t1_txt_select.setVisible(false);
        }
    }

    public void showAlert(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}

