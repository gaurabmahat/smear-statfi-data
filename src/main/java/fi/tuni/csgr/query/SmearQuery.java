package fi.tuni.csgr.query;

import fi.tuni.csgr.components.ControlComponent;
import fi.tuni.csgr.components.DateSelector;
import fi.tuni.csgr.components.MultipleChoiceDropDown;
import fi.tuni.csgr.components.SingleChoiceDropdown;
import fi.tuni.csgr.converters.json.JsonToResultConverter;
import fi.tuni.csgr.converters.json.ResultList;
import fi.tuni.csgr.converters.json.SmearJsonToResultConverter;
import fi.tuni.csgr.components.ControlPanel;
import fi.tuni.csgr.query.resultviews.ResultView;
import fi.tuni.csgr.query.resultviews.SmearResultsView;
import fi.tuni.csgr.managers.graphs.GraphDataManager;
import fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear.SmearTimeAndVariableData;
import fi.tuni.csgr.stationNames.Station;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear.PredefinedStationsInfo.*;

/**
 * A query containing all data specific to a SMEAR query.
 */

public class SmearQuery implements Query {
    private static Map<String, Station> initialDataFromSmear;

    private DateSelector from;
    private DateSelector to;
    private MultipleChoiceDropDown gas;
    private MultipleChoiceDropDown station;
    private SingleChoiceDropdown value;
    private ControlPanel controlPanel;

    private GraphDataManager graphDataManager;
    private JsonToResultConverter resultConverter;
    private ResultView resultView;

    /**
     * Constructor protected, to make it only accessible through QuerySingletonFactory.
     */

    protected SmearQuery() {
        CompletableFuture<Void> smearTimeAndVariableData = CompletableFuture.runAsync(() -> {
            System.out.println("Async starts");
            initialDataFromSmear =  SmearTimeAndVariableData.smearMapData;
            System.out.println("Async ends");
        });

        graphDataManager = new GraphDataManager();
        resultConverter = new SmearJsonToResultConverter();
        // Create a resultView based on chartManager, which handles all resultView updates
        resultView = new SmearResultsView(graphDataManager);

        // Set variables in dropdowns
        ObservableList<String> stationList = FXCollections.observableArrayList(stationNameTableVariableName.keySet());
        ObservableList<String> gasList = FXCollections.observableArrayList(gasAndItsKeywords.keySet());
        ObservableList<String> typeList = FXCollections.observableArrayList(aggregationType.keySet());

        // Create UI components and add to InputControls
        from = new DateSelector(LocalDate.now().minusDays(2), "From date", true);
        to = new DateSelector(LocalDate.now(), "To date", true);
        gas = new MultipleChoiceDropDown(gasList, "Gas", true);
        station = new MultipleChoiceDropDown(stationList, "Station", true);
        value = new SingleChoiceDropdown(typeList, "Aggregation", true);
        value.setSelection("None");

        controlPanel = new ControlPanel();
        controlPanel.addControl("fromDate", from);
        controlPanel.addControl("toDate",to);
        controlPanel.addControl("gas", gas);
        controlPanel.addControl("station", station);
        controlPanel.addControl("value", value);

        // TO DO: These values should be retrieved from initialDataFromSmear
        from.limitDatePicker(LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now());
        to.limitDatePicker(LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now());

        // Add listener to fromPicker to limit lower range of toPicker
        DatePicker fromPicker = (DatePicker)from.getControl();
        DatePicker toPicker = (DatePicker)to.getControl();
        fromPicker.valueProperty().addListener((observable, oldDate, newDate) -> {
            to.limitDatePicker(newDate, LocalDate.now());
            if (toPicker.getValue().isBefore(newDate)) {
                toPicker.setValue(newDate);
            }
        });
    }

    @Override
    public LocalDate getFromDate() {
        return from.getDate();
    }

    @Override
    public LocalDate getToDate() {
        return to.getDate();
    }

    @Override
    public HashMap<String, ArrayList<String>> getSelectionData() {
        return controlPanel.getSelectionData();
    }

    @Override
    public void setSelectionData(Map<String, ArrayList<String>> data) {
        controlPanel.setSelectionData(data);
    }

    /**
     *  Create Http request from currently selected values.
     * @return
     */
    @Override
    public HttpRequest getHttpRequest() {
        //Get table variables from the class getTableVariable
        String tableVariables = getStationsCode();

        LocalDateTime fromTime = getFromDate().atStartOfDay();
        LocalDateTime toTime = getToDate().atTime(23, 59);
        String startTime = fromTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endTime = toTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String aggregation = aggregationType.get(value.getSelection());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://smear-backend.rahtiapp.fi/search/timeseries?aggregation="
                + aggregation
                + "&interval=60&from="+ startTime
                + "&to="+ endTime
                + tableVariables)).build();
        return request;
    }

    /**
     * Convert JSON response from Smear to ResultList
     *
     * @param json
     * @return
     */
    @Override
    public ResultList JsonToResult(String json) {
        return resultConverter.convert(json);
    }

    @Override
    public void updateGraphs(ResultList results) {
        graphDataManager.update(results);
    }

    /**
     * @return Pane containing visualization of results.
     */
    @Override
    public Pane getResultView() {
        return resultView.getResultView();
    }

    /**
     * @return list of all UI components needed to select parameters of query
     */
    @Override
    public ArrayList<ControlComponent> getControls() {
        return controlPanel.getControlComponents();
    }

    @Override
    public String type(){
        return "Smear";
    }

    private String getStationsCode() {
        StringBuilder tableVariable = new StringBuilder("");
        for (String gas : gas.getSelectedItems()) {
            for(String station : station.getSelectedItems()){
                String stationCode = initialDataFromSmear.get(station)
                        .getStationMap()
                        .get(gas)
                        .getGasValues()
                        .getVariableName();
                tableVariable.append("&tablevariable=").append(stationCode);
            }
        }
        return tableVariable.toString();
    }

}
