package fi.tuni.csgr.query;

import fi.tuni.csgr.components.*;
import fi.tuni.csgr.converters.json.JsonToResultConverter;
import fi.tuni.csgr.converters.json.ResultList;
import fi.tuni.csgr.converters.json.StatfiJsonToResultConverter;
import fi.tuni.csgr.components.ControlPanel;
import fi.tuni.csgr.managers.graphs.BarGraphDataManager;
import fi.tuni.csgr.query.resultviews.ResultView;
import fi.tuni.csgr.managers.graphs.GraphDataManager;
import fi.tuni.csgr.query.resultviews.StatfiResultsView;
import javafx.scene.layout.Pane;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

/**
 * A query containing all data specific to a STATFI query.
 */

public class StatfiQuery implements  Query {

    private YearSelector years;
    private ControlPanel controlPanel;

    private JsonToResultConverter resultConverter;
    private BarGraphDataManager graphDataManager;
    private ResultView resultView;

    /**
     * Constructor protected, to make it only accessible through QuerySingletonFactory.
     */
    protected StatfiQuery() {
        // Initialize objects required for data fetching and conversion
        graphDataManager = new BarGraphDataManager();
        resultConverter = new StatfiJsonToResultConverter();
        // Create a resultView based on chartManager, which handles all resultView updates
        resultView = new StatfiResultsView(graphDataManager);

        // Create UI components and add to list of components
        years = new YearSelector(1990, 2016, "Years", true);

        controlPanel = new ControlPanel();
        controlPanel.addControl("years", years);

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
     * Create Http request from currently selected values.
     * @return
     */
    @Override
    public HttpRequest getHttpRequest() {

        try {
            URI uri = new URI("https://pxnet2.stat.fi/PXWeb/api/v1/en/ymp/taulukot/Kokodata.px");
            String postString = getStatfiPOSTString();

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
    public ResultList jsonToResult(String json) {
        return resultConverter.convert(json);
    }

    /**
     * @return Pane containing visualization of results.
     */
    @Override
    public Pane getResultView() {
        return resultView.getResultView();
    }

    @Override
    public void updateResults(ResultList results) {
        graphDataManager.update(results);
        //Sorting the XYCharts.Series
        for(var key : graphDataManager.getGases().keySet()){
            for(int i = 0; i < graphDataManager.getGases().get(key).get(0).getData().size(); i++){
                graphDataManager.getGases().get(key).get(0).getData().sort(Comparator.comparingInt(e -> Integer.parseInt(e.getXValue())));
            }
        }
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
        return "Statfi";
    }

    private String getStatfiPOSTString(){
        List<Integer> vuosiList = new ArrayList<>();
        IntStream
                .range(years.getFromDate(), years.getToDate())
                .forEach(vuosiList::add);
        vuosiList.add(years.getToDate());

        String vuosiValues = "";
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
                "                    \"values\": ["+
            "                           \"Khk_yht\",\n" +
            "                           \"Khk_yht_index\",\n" +
            "                           \"Khk_yht_las\",\n" +
            "                           \"Khk_yht_las_index\"\n"  +
                "                   ]\n" +
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
