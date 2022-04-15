package fi.tuni.csgr.query;

import fi.tuni.csgr.components.ControlComponent;
import fi.tuni.csgr.converters.json.JsonToResultConverter;
import fi.tuni.csgr.converters.json.ResultList;
import javafx.scene.layout.Pane;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public interface Query {
    LocalDate getFromDate();
    LocalDate getToDate();
    HashMap<String, ArrayList<String>> getQueryArgs();

    HttpRequest getHttpRequest();
    ResultList JsonToResult(String json);
    void updateGraphs(ResultList results);

    Pane getResultView();

    ArrayList<ControlComponent> getControlComponents();
}
