package fi.tuni.csgr.query.resultviews;

import fi.tuni.csgr.components.ChartView;
import fi.tuni.csgr.managers.graphs.GraphDataManager;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class SmearResultsView implements ResultView {
    private GraphDataManager graphDataManager;
    private HashMap<String, ChartView> charts;
    private VBox resultView;

    public SmearResultsView(GraphDataManager dataManager) {
        graphDataManager = dataManager;
        charts = new HashMap<>();
        resultView = new VBox();
        addGraphListeners();
    }

    /**
     * Adds listeners to graphDataManager, to add/remove charts when graphDataManager updated.
     *
     */
    private void addGraphListeners() {
        MapChangeListener mainListener = new MapChangeListener() {
            @Override
            public void onChanged(Change change) {
                String name = change.getKey().toString();
                if(change.wasRemoved()) {
                    resultView.getChildren().remove(charts.get(name).getChartBox());
                    charts.remove(name);
                }
                if(change.wasAdded()) {
                    charts.put(name, new ChartView(name, (ObservableList<XYChart.Series<Long, Double>>) change.getValueAdded()));
                    resultView.getChildren().add(charts.get(name).getChartBox());
                }
            }
        };
        graphDataManager.addMainListener(mainListener);
    }

    @Override
    public Pane getResultView() {
        return resultView;
    }
}
