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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

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

    private String getStatfiPOSTString(){
        var tiedotList = gas.getSelectedItems();

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
