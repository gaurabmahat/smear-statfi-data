import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
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
    private DatePicker t1_datepicker;
    @FXML
    private ComboBox<String> t1_cb_stations;
    @FXML
    private ComboBox<String> t1_cb_display;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_load;
    @FXML
    private Text t1_txt_select;
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
    private LocalDate selectedDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        randomData1 = new XYChart.Series();
        randomData1.setName("Data");
        
        randomData2 = new XYChart.Series();
        randomData2.setName("Data");
        
        selectedDate = LocalDate.of(2022, Month.JANUARY, 1);
        
        restrictDatePicker(t1_datepicker, LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now());
        
        ObservableList<String> gasList = FXCollections.observableArrayList("CO2", "SO2", "NO", "Ozone", "CO2 flux");
        t1_cb_gas.setItems(gasList);
        
        ObservableList<String> stationList = FXCollections.observableArrayList("Kumpula", "Vaario", "Hyytiala");
        t1_cb_stations.setItems(stationList);
        
        ObservableList<String> displayList = FXCollections.observableArrayList("selection", "average", "selection", "selection");
        t1_cb_display.setItems(displayList);
    }    

    @FXML
    private void handleT1CbGas(ActionEvent event) {
        t1_graph1.setVisible(true);
        t1_txt_select.setVisible(false);
        String gas = t1_cb_gas.getSelectionModel().getSelectedItem();
        updateGraph(t1_graph1, gas, selectedDate);
    }

    @FXML
    private void handleT1Datepicker(ActionEvent event) {
        selectedDate = t1_datepicker.getValue();
    }

    @FXML
    private void handleT1CbStations(ActionEvent event) {
        t1_graph2.setVisible(true);
        t1_txt_select.setVisible(false);
        String station = t1_cb_stations.getSelectionModel().getSelectedItem();
        updateGraph(t1_graph2, station, selectedDate);
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
    
    
    public void updateGraph(XYChart<?,?> graph, String name, LocalDate startDate) {
        XYChart.Series randomData = new XYChart.Series();
        randomData.setName(name);
        graph.getData().clear();
        randomData.getData().clear();
        for (int i = 0 ; i < 12 ; i++) {
            int next = (int)(20 + Math.random()*70);
            randomData.getData().add(new XYChart.Data(startDate.plusDays(i).toString(), next));
        }
        graph.getData().add(randomData);
        
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
