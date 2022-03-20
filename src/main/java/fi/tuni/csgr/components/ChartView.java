package fi.tuni.csgr.components;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Component containing a chart with title.
 *
 * @author Roger Wanamo
 */

public class ChartView {
    private VBox chartBox;
    private LineChart<Number, Number> chart;
    private Map<String, XYChart.Series<Number, Number>> seriesMap;
    private String title;

    public ChartView(String title) {
        chartBox = new VBox();
        chartBox.getStyleClass().add("chart-box");
        chartBox.setFillWidth(true);

        this.title = title;

        seriesMap = new HashMap<>();

        Text titleText = new Text(title);
        titleText.getStyleClass().add("graph-title");
        chartBox.getChildren().add(titleText);

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);

        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {

            @Override
            public String toString(Number t) {
                LocalDateTime dateTime =
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(t.longValue()), TimeZone.getDefault().toZoneId());
                String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy HH:mm"));
                return formattedDate;
            }

            @Override
            public Number fromString(String string) {
                return 1;
            }
        });

        chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
        chartBox.getChildren().add(chart);
    }

    /**
     * Adds a series to the graph.
     *
     * @param series
     */
    public void addSeries(XYChart.Series<Number, Number> series) {
        chart.getData().add(series);
        seriesMap.put(series.getName(), series);
    }

    /**
     * Removes a series from the graph.
     *
     * @param name Name of series to remove.
     */
    public void removeSeries(String name) {
        chart.getData().remove(seriesMap.get(name));
        seriesMap.remove(name);
    }

    public VBox getChartBox() {
        return chartBox;
    }
}
