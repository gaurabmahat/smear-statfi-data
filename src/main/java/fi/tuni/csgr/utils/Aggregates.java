package fi.tuni.csgr.utils;

import fi.tuni.csgr.converters.json.ResultList;
import fi.tuni.csgr.managers.graphs.GraphDataManager;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.*;

public class Aggregates extends  GraphDataManager{

    public double findMaximum(List<XYChart.Data<Long, Double>> seriesData){
        Double result;
        List<Double> values = new ArrayList<>();
        for (int i=0; i< seriesData.size(); i++){
            result = (seriesData.get(i)).getYValue();

            values.add(result);
        }
        //System.out.println(values);
        double maximum = Collections.max(values);
        System.out.println("Maximum value of series data is "+maximum);
        return maximum;
    }

    public double findMinimum(List<XYChart.Data<Long, Double>> seriesData){
        Double result;
        List<Double> values = new ArrayList<>();
        for (int i=0; i< seriesData.size(); i++){
            result = (seriesData.get(i)).getYValue();

            values.add(result);
        }
        //System.out.println(values);
        double minimum = Collections.min(values);
        System.out.println("Minimum value of the series data is "+minimum);
        return minimum;
    }
    public double findAverage(List<XYChart.Data<Long, Double>> seriesData){
        Double result;
        List<Double> values = new ArrayList<>();
        for (int i=0; i< seriesData.size(); i++){
            result = (seriesData.get(i)).getYValue();

            values.add(result);
        }
        double average = values.stream().mapToDouble(val -> val).average().orElse(0.0);
        System.out.println("Average value of the series data is "+average);
        return average;
    }
}
