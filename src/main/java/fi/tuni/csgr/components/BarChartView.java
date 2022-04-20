package fi.tuni.csgr.components;

import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Map;

/**
 * Components containing a bar chart.
 *
 * @author mahat
 */

public class BarChartView implements ChartView {
    private VBox chartBox;
    private BarChart<String, Number> chart;
    private Map<String, XYChart.Series<Number, Number>> seriesMap;
    private String title;

    public BarChartView(String title, ObservableList<XYChart.Series<String, Double>> seriesList) {
        chartBox = new VBox();
        chartBox.getStyleClass().add("chart-box");
        chartBox.setFillWidth(true);

        StackPane stackPane = new StackPane();

        // Create title text
        this.title = title;
        Text titleText = new Text(title);
        titleText.getStyleClass().add("graph-title");
        chartBox.getChildren().add(titleText);

        // Create x and y axis for graph
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);

        xAxis.setAutoRanging(true);
        //xAxis.setForceZeroInRange(false);

        // Create BarChart
        chart = new BarChart(xAxis, yAxis, seriesList);
        chart.setAnimated(false);
        //chart.setCreateSymbols(false);
        stackPane.getChildren().add(chart);
        chartBox.getChildren().add(stackPane);
    }

    public VBox getChartBox() {
        return chartBox;
    }
}
