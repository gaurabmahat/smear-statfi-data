package fi.tuni.csgr.query;

import fi.tuni.csgr.components.ControlComponent;
import fi.tuni.csgr.components.DateSelector;
import fi.tuni.csgr.components.MultipleChoiceDropDown;
import fi.tuni.csgr.components.SingleChoiceDropdown;
import fi.tuni.csgr.converters.json.JsonToResultConverter;
import fi.tuni.csgr.converters.json.ResultList;
import fi.tuni.csgr.converters.json.SmearJsonToResultConverter;
import fi.tuni.csgr.managers.graphs.ChartManager;
import fi.tuni.csgr.managers.graphs.GraphDataManager;
import fi.tuni.csgr.smearAndStatfi.SMEAR.fetchSeriesDataFromSmear.getTableVariable;
import fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear.SmearTimeAndVariableData;
import fi.tuni.csgr.stationNames.Station;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static fi.tuni.csgr.stationNames.stationNameMapping.mapStationAndGas;

public class SmearQuery implements Query {
    private final Map<String, Station> initialDataFromSmear;

    private DateSelector from;
    private DateSelector to;
    private MultipleChoiceDropDown gas;
    private MultipleChoiceDropDown station;
    private SingleChoiceDropdown value;
    private ArrayList<ControlComponent> controlComponents;

    private GraphDataManager graphDataManager;
    private JsonToResultConverter resultConverter;
    private VBox resultView;

    protected SmearQuery() {
        initialDataFromSmear = new SmearTimeAndVariableData().getSmearTimeData();
        graphDataManager = new GraphDataManager();
        resultConverter = new SmearJsonToResultConverter();
        resultView = new VBox();
        ChartManager chartManager = new ChartManager(graphDataManager, resultView);

        ObservableList<String> stationList = FXCollections.observableArrayList();
        mapStationAndGas.forEach((k,v) -> stationList.add(k) );

        ObservableList<String> gasList = FXCollections.observableArrayList();
        mapStationAndGas.get(stationList.get(0)).forEach((k,v) -> gasList.add(k));

        ObservableList<String> typeList = FXCollections.observableArrayList("None", "Min", "Max");

        from = new DateSelector(LocalDate.now().minusDays(2), "From date", true);
        to = new DateSelector(LocalDate.now(), "To date", true);
        gas = new MultipleChoiceDropDown(gasList, "Gas", true);
        station = new MultipleChoiceDropDown(stationList, "Station", true);
        value = new SingleChoiceDropdown(typeList, "Value type", true);
        value.setSelection("None");

        controlComponents = new ArrayList<>();
        controlComponents.add(from);
        controlComponents.add(to);
        controlComponents.add(gas);
        controlComponents.add(station);
        controlComponents.add(value);
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
    public HashMap<String, ArrayList<String>> getQueryArgs() {
        return null;
    }

    @Override
    public HttpRequest getHttpRequest() {
        //Get table variables from the class getTableVariable
        String tableVariables = new getTableVariable(gas.getSelectedItems(), station.getSelectedItems()).getStationsCode();

        LocalDateTime fromTime = getFromDate().atStartOfDay();
        LocalDateTime toTime = getToDate().atTime(23, 59);
        String startTime = fromTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endTime = toTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://smear-backend.rahtiapp.fi/search/timeseries?aggregation="
                + value.getSelection().toUpperCase()
                + "&interval=60&from="+ startTime
                + "&to="+ endTime
                + tableVariables)).build();
        return request;
    }

    @Override
    public ResultList JsonToResult(String json) {
        return resultConverter.convert(json);
    }

    @Override
    public void updateGraphs(ResultList results) {
        graphDataManager.update(results);
    }

    @Override
    public Pane getResultView() {
        return resultView;
    }

    @Override
    public ArrayList<ControlComponent> getControlComponents() {
        return controlComponents;
    }
}
