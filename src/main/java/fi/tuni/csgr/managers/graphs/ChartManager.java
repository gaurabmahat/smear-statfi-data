package fi.tuni.csgr.managers.graphs;

import fi.tuni.csgr.components.ChartView;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class ChartManager {
    GraphDataManager graphDataManager;
    HashMap<String, ChartView> charts;
    Pane chartContainer;

    public ChartManager(GraphDataManager dataManager, Pane container) {
        graphDataManager = dataManager;
        chartContainer = container;
        charts = new HashMap<>();

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
                    chartContainer.getChildren().remove(charts.get(name).getChartBox());
                    charts.remove(name);
                }
                if(change.wasAdded()) {
                    charts.put(name, new ChartView(name, (ObservableList<XYChart.Series<Long, Double>>) change.getValueAdded()));
                    chartContainer.getChildren().add(charts.get(name).getChartBox());
                }
            }
        };
        graphDataManager.addMainListener(mainListener);
    }
}
