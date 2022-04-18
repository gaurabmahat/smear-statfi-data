package fi.tuni.csgr.query;

import fi.tuni.csgr.components.ControlComponent;
import fi.tuni.csgr.converters.json.ResultList;
import javafx.scene.layout.Pane;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interface for creating Queries. The query object keeps track of everything related to a specific query:
 *
 *      -UI components needed to select parameters for query
 *      -Methods needed to perform the specific Query
 *      -A Pane to display the results of a Query
 */

public interface Query {
    LocalDate getFromDate();
    LocalDate getToDate();
    Map<String, ArrayList<String>> getSelectionData();
    void setSelectionData(Map<String, ArrayList<String>> data);
    String type();

    HttpRequest getHttpRequest();
    ResultList JsonToResult(String json);
    void updateGraphs(ResultList results);

    Pane getResultView();

    ArrayList<ControlComponent> getControls();
}
