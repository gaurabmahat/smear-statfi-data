package fi.tuni.csgr.query;

import fi.tuni.csgr.components.*;
import fi.tuni.csgr.converters.json.JsonToResultConverter;
import fi.tuni.csgr.converters.json.ResultList;
import fi.tuni.csgr.converters.json.StatfiJsonToResultConverter;
import fi.tuni.csgr.managers.graphs.ChartManager;
import fi.tuni.csgr.managers.graphs.GraphDataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class StatfiQuery implements  Query {

    private YearSelector years;
    private MultipleChoiceDropDown gas;
    private ArrayList<ControlComponent> controlComponents;

    private JsonToResultConverter resultConverter;
    private GraphDataManager graphDataManager;
    private VBox resultView;

    protected StatfiQuery() {
        graphDataManager = new GraphDataManager();
        resultConverter = new StatfiJsonToResultConverter();
        resultView = new VBox();
        ChartManager chartManager = new ChartManager(graphDataManager, resultView);

        ObservableList<String> gasList = FXCollections.observableArrayList("CO2 tonnes", "CO2 intensity", "CO2 indexed", "CO2 indexed intensity");

        years = new YearSelector(1975, 2017, "Years", true);
        gas = new MultipleChoiceDropDown(gasList, "Gas", true);

        controlComponents = new ArrayList<>();
        controlComponents.add(years);
        controlComponents.add(gas);
    }

    @Override
    public LocalDate getFromDate() {
        return null;
    }

    @Override
    public LocalDate getToDate() {
        return null;
    }

    @Override
    public HashMap<String, ArrayList<String>> getQueryArgs() {
        return null;
    }

    @Override
    public HttpRequest getHttpRequest() {
        return null;
    }

    @Override
    public ResultList JsonToResult(String json) {
        return resultConverter.convert(json);
    }

    @Override
    public Pane getResultView() {
        return resultView;
    }

    @Override
    public void updateGraphs(ResultList results) {
        graphDataManager.update(results);
    }

    @Override
    public ArrayList<ControlComponent> getControlComponents() {
        return controlComponents;
    }
}
