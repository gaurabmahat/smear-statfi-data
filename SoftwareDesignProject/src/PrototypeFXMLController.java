import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Roger Wanamo
 */
public class PrototypeFXMLController implements Initializable {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tab1;
    @FXML
    private AnchorPane t1_anchorPane;
    @FXML
    private ComboBox<String> t1_cb_gas;
    @FXML
    private DatePicker t1_datePicker_from;
    @FXML
    private DatePicker t1_datePicker_to;
    @FXML
    private ComboBox<String> t1_cb_stations;
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
    
    private XYChart.Series randomData1;
    private XYChart.Series randomData2;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String selectedGas;
    private String selectedStation;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        randomData1 = new XYChart.Series();
        randomData1.setName("Data");
        
        randomData2 = new XYChart.Series();
        randomData2.setName("Data");
        
        fromDate = LocalDate.of(2022, Month.JANUARY, 1);
        t1_datePicker_from.setValue(LocalDate.of(2022, Month.JANUARY, 1));
        toDate = LocalDate.now();
        t1_datePicker_to.setValue(LocalDate.now());
        
        restrictDatePicker(t1_datePicker_from, LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now());
        restrictDatePicker(t1_datePicker_to, fromDate, LocalDate.now());
        
        t1_graph1.setCreateSymbols(false);
        
        ObservableList<String> gasList = FXCollections.observableArrayList("CO2", "SO2", "NO", "Ozone", "CO2 flux");
        t1_cb_gas.setItems(gasList);
        
        ObservableList<String> stationList = FXCollections.observableArrayList("Kumpula", "Vaario", "Hyytiala");
        t1_cb_stations.setItems(stationList);
        
        ObservableList<String> displayList = FXCollections.observableArrayList("selection", "average", "selection", "selection");
        t1_cb_display.setItems(displayList);
    }    

    @FXML
    private void handleT1CbGas(ActionEvent event) {
        selectedGas = t1_cb_gas.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void handleT1DatePickerFrom(ActionEvent event) {
        fromDate = t1_datePicker_from.getValue();
        if (fromDate.isAfter(toDate)) {
            toDate = fromDate;
            t1_datePicker_to.setValue(fromDate);
        }
        restrictDatePicker(t1_datePicker_to, fromDate, LocalDate.now());
    }
    
    @FXML
    private void handleT1DatePickerTo(ActionEvent event) {
        toDate = t1_datePicker_to.getValue();
    }

    @FXML
    private void handleT1CbStations(ActionEvent event) {
        selectedStation = t1_cb_stations.getSelectionModel().getSelectedItem();
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
        if (selectedGas == null | selectedStation == null) {
            showAlert("Please select a gas and a station to show data.");
        }
        else {
            t1_graph1.setVisible(true);
            t1_graph2.setVisible(true);
            t1_txt_select.setVisible(false);
            updateGraph(t1_graph1, selectedGas + ", " + selectedStation, fromDate, toDate);
            updateGraph(t1_graph2, "some other gas", fromDate, toDate);
        }        
    }
    
    
    public void updateGraph(XYChart<?,?> graph, String name, LocalDate from, LocalDate to) {
        XYChart.Series randomData = new XYChart.Series();
        long dayDiff = ChronoUnit.DAYS.between(from, to);
        randomData.setName(name);
        graph.getData().clear();
        randomData.getData().clear();
        for (int i = 0 ; i < dayDiff ; i++) {
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
    
    /**
     * Restricts selectable dates on datePicker to allow only dates from minDate to maxDate.
     * @param datePicker
     * @param minDate
     * @param maxDate
     */
    public void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                         if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }
}
