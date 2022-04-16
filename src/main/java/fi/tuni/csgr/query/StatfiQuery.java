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

/**
 * A query containing all data specific to a STATFI query.
 */

public class StatfiQuery implements  Query {

    private YearSelector years;
    private MultipleChoiceDropDown gas;
    private ArrayList<ControlComponent> controlComponents;

    private JsonToResultConverter resultConverter;
    private GraphDataManager graphDataManager;
    private VBox resultView;

    /**
     * Constructor protected, to make it only accessible through QuerySingletonFactory.
     */
    protected StatfiQuery() {
        // Initialize objects required for data fetching and conversion
        graphDataManager = new GraphDataManager();
        resultConverter = new StatfiJsonToResultConverter();
        resultView = new VBox();

        // Connecting graphDataManager and resultView to chartManager, which handles all resultView updates
        ChartManager chartManager = new ChartManager(graphDataManager, resultView);

        // TO DO: Need a map to connect these to variables for http query
        ObservableList<String> gasList = FXCollections.observableArrayList("CO2 tonnes", "CO2 intensity", "CO2 indexed", "CO2 indexed intensity");

        // Create UI components and add to list of components
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

    /**
     * Create Http request from currently selected values.
     * @return
     */
    @Override
    public HttpRequest getHttpRequest() {
        return null;
    }

    /**
     * Convert JSON response from Statfi to ResultList
     *
     * @param json
     * @return
     */
    @Override
    public ResultList JsonToResult(String json) {
        return resultConverter.convert(json);
    }

    /**
     * @return Pane containing visualization of results.
     */
    @Override
    public Pane getResultView() {
        return resultView;
    }

    @Override
    public void updateGraphs(ResultList results) {
        graphDataManager.update(results);
    }

    /**
     * @return list of all UI components needed to select parameters of query
     */
    @Override
    public ArrayList<ControlComponent> getControlComponents() {
        return controlComponents;
    }
}
