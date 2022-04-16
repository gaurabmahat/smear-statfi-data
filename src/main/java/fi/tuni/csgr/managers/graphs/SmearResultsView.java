package fi.tuni.csgr.managers.graphs;

import fi.tuni.csgr.components.ChartView;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class SmearResultsView extends VBox {
    private GraphDataManager graphDataManager;
    private HashMap<String, ChartView> charts;

    public SmearResultsView(GraphDataManager dataManager) {
        graphDataManager = dataManager;
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
                    getChildren().remove(charts.get(name).getChartBox());
                    charts.remove(name);
                }
                if(change.wasAdded()) {
                    charts.put(name, new ChartView(name, (ObservableList<XYChart.Series<Long, Double>>) change.getValueAdded()));
                    getChildren().add(charts.get(name).getChartBox());
                }
            }
        };
        graphDataManager.addMainListener(mainListener);
    }
}
