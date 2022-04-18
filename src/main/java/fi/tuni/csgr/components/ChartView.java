package fi.tuni.csgr.components;

import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public ChartView(String title, ObservableList<XYChart.Series<Long, Double>> seriesList) {
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
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);

        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);

        // Set text format of xAxis
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

        // Create LineChart
        chart = new LineChart(xAxis, yAxis, seriesList);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
        stackPane.getChildren().add(chart);
        chartBox.getChildren().add(stackPane);
    }

    public VBox getChartBox() {
        return chartBox;
    }
}
