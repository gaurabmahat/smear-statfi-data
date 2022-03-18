package fi.tuni.csgr;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import fi.tuni.csgr.managers.graphs.GraphDataManager;
import fi.tuni.csgr.utils.DatePickerUtils;
import fi.tuni.csgr.utils.MenuUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
    private LineChart<?, ?> t1_graph1;
    @FXML
    private AreaChart<?, ?> t1_graph2;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;

    private ArrayList<String> selectedGases = new ArrayList<>();
    private ArrayList<String> selectedStations = new ArrayList<>();

    private Series<?,?> randomData1;
    private Series<?,?> randomData2;
    private LocalDate fromDate;
    private LocalDate toDate;

    private GraphDataManager graphDataManager;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        graphDataManager = new GraphDataManager();

        randomData1 = new Series<>();
        randomData1.setName("Data");

        randomData2 = new Series<>();
        randomData2.setName("Data");

        fromDate = LocalDate.now().minusDays(2);
        t1_datePicker_from.setValue(LocalDate.now().minusDays(2));
        toDate = LocalDate.now();
        t1_datePicker_to.setValue(LocalDate.now());

        DatePickerUtils.restrictDatePicker(t1_datePicker_from, LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now());
        DatePickerUtils.restrictDatePicker(t1_datePicker_to, fromDate, LocalDate.now());

        t1_graph1.setCreateSymbols(false);

        ArrayList<String> gasList = new ArrayList<String>(Arrays.asList("CO2", "SO2", "NO", "Ozone", "CO2 flux"));
        selectedGases = MenuUtils.createCheckboxMenuItems(gasList, t1_mb_gas, "Gas");

        ArrayList<String> stationList = new ArrayList<String>(Arrays.asList("Kumpula", "Vaario", "Hyytiala"));
        selectedStations = MenuUtils.createCheckboxMenuItems(stationList, t1_mb_stations, "Gas");

        ObservableList<String> displayList = FXCollections.observableArrayList("selection", "average", "selection", "selection");
        t1_cb_display.setItems(displayList);
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
        t1_graph1.setVisible(false);
        t1_graph2.setVisible(false);
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
            t1_graph1.setVisible(true);
            t1_graph2.setVisible(true);
            t1_txt_select.setVisible(false);
            updateGraph(t1_graph1, selectedGases.get(0) + ", " + selectedStations.get(0), fromDate, toDate);
            updateGraph(t1_graph2, "some other gas", fromDate, toDate);
        }
    }


    public void updateGraph(XYChart<?,?> graph, String name, LocalDate from, LocalDate to) {
        Series randomData = new Series<>();
        long dayDiff = ChronoUnit.DAYS.between(from, to);
        randomData.setName(name);
        graph.getData().clear();
        randomData.getData().clear();
        for (int i = 0 ; i <= dayDiff ; i++) {
            int next = (int)(20 + Math.random()*70);
            randomData.getData().add(new XYChart.Data(from.plusDays(i).toString(), next));
        }
        graph.getData().add(randomData);
    }

    public void showAlert(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}

