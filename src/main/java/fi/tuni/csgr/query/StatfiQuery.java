package fi.tuni.csgr.query;

import fi.tuni.csgr.components.*;
import fi.tuni.csgr.converters.json.JsonToResultConverter;
import fi.tuni.csgr.converters.json.ResultList;
import fi.tuni.csgr.converters.json.StatfiJsonToResultConverter;
import fi.tuni.csgr.managers.graphs.SmearResultsView;
import fi.tuni.csgr.managers.graphs.GraphDataManager;
import fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear.PredefinedStationsInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A query containing all data specific to a STATFI query.
 */

public class StatfiQuery implements  Query {

    private YearSelector years;
    private MultipleChoiceDropDown gas;
    private ArrayList<ControlComponent> controlComponents;

    private JsonToResultConverter resultConverter;
    private GraphDataManager graphDataManager;
    private Pane resultView;

    /**
     * Constructor protected, to make it only accessible through QuerySingletonFactory.
     */
    protected StatfiQuery() {
        // Initialize objects required for data fetching and conversion
        graphDataManager = new GraphDataManager();
        resultConverter = new StatfiJsonToResultConverter();
        // Create a resultView based on chartManager, which handles all resultView updates
        resultView = new SmearResultsView(graphDataManager);


        // TO DO: Need a map to connect these to variables for http query
        ObservableList<String> gasList = FXCollections.observableArrayList("CO2 tonnes", "CO2 intensity", "CO2 indexed", "CO2 indexed intensity");

        // Create UI components and add to list of components
        years = new YearSelector(1990, 2016, "Years", true);
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
        try {
            URI uri = new URI("https://pxnet2.stat.fi/PXWeb/api/v1/en/ymp/taulukot/Kokodata.px");
            var postString = getStatfiPOSTString();

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(postString))
                    .header("Content-type", "application/json")
                    .build();
            return request;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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

    private String getStatfiPOSTString(){
        var getTiedot = gas.getSelectedItems();
        List<String> tiedotList = new ArrayList<>();

        for(var i : getTiedot){
            tiedotList.add(PredefinedStationsInfo.statfiValues.get(i));
        }

        List<Integer> vuosiList = new ArrayList<>();
        IntStream
                .range(years.getFromDate(), years.getToDate())
                .forEach(vuosiList::add);
        vuosiList.add(years.getToDate());

        String tiedotValues = "";
        String vuosiValues = "";
        //Convert tiedot List to String
        for(int i = 0; i < tiedotList.size()-1; i++){
            tiedotValues += "\"" + tiedotList.get(i) + "\", ";
        }
        tiedotValues += "\"" + tiedotList.get(tiedotList.size()-1) + "\"";

        //Convert vuosi List to String
        for(int i = 0; i < vuosiList.size()-1; i++){
            vuosiValues += "\"" + vuosiList.get(i) + "\", ";
        }
        vuosiValues += "\"" + vuosiList.get(vuosiList.size()-1) + "\"";

        var statfiPOSTString = "{\n" +
                "        \"query\": [\n" +
                "            {\n" +
                "                \"code\": \"Tiedot\",\n" +
                "                \"selection\": {\n" +
                "                    \"filter\": \"item\",\n" +
                "                    \"values\": ["+ tiedotValues + "]\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"code\": \"Vuosi\",\n" +
                "                \"selection\": {\n" +
                "                    \"filter\": \"item\",\n" +
                "                    \"values\": [" + vuosiValues + "]\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n";

        return statfiPOSTString;
    }
}
