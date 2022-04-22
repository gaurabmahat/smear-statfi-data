package fi.tuni.csgr.managers.graphs;

import fi.tuni.csgr.converters.json.ResultList;
import fi.tuni.csgr.utils.SmearAggregates;
import javafx.collections.*;
import javafx.scene.chart.XYChart;

import java.util.*;

/**
 * Class for managing all data displayed on graphs
 *
 * @author Cecylia Borek
 */

public class GraphDataManager {

    ObservableMap<String, ObservableList<XYChart.Series<Long, Double>>> gases;

    public GraphDataManager(){
        this.gases = FXCollections.observableMap(new HashMap<>());
    }


    /**
     * updates the stored data with fetched results
     * @param results results as ResultList
     */
    public void update(ResultList results){
        List<String> resultGases = results.getGases();
        List<String> currentGases = gases.keySet().stream().toList();

        DiffResult gasDiff = getDiff(currentGases, resultGases);
        removeOldGases(gasDiff.getToRemove());
        addNewGases(gasDiff.getToAdd());

        for (String gas : gases.keySet()){
            List<String> currentStationsForGas = getStoredStationNamesForGas(gas);
            List<String> resultsStationsForGas = results.getStationsForGas(gas);

            DiffResult stationsDiff = getDiff(currentStationsForGas, resultsStationsForGas);
            removeOldStationsForGas(gas, stationsDiff.getToRemove());
            addNewStationsForGas(gas, stationsDiff.getToAdd());

            for (String station: getStoredStationNamesForGas(gas)){
                updateGasStationResults(results, station, gas);
            }
        }
    }

    /**
     * adds listener to the main map storing all result gases
     * @param listener
     */
    public void addMainListener(MapChangeListener listener){
        gases.addListener(listener);
    }

    /**
     * adds listener to the map storing all stations for specified gas
     * @param gas
     * @param listener
     */
    public void addGasListener(String gas, ListChangeListener listener){
        gases.get(gas).addListener(listener);
    }

    private List<String> getStoredStationNamesForGas(String gas){
        List<XYChart.Series<Long, Double>> stationSeries = gases.get(gas);
        return stationSeries.stream().map(s -> s.getName()).toList();
    }

    private XYChart.Series<Long, Double> getGasStationSeries(String gas, String station){
        List<XYChart.Series<Long, Double>> allGasSeries = gases.get(gas);
        int i = 0;
        while (! allGasSeries.get(i).getName().equals(station)){
            i++;
        }
        return allGasSeries.get(i);
    }

    public List<XYChart.Data<Long, Double>> updateGasStationResults(ResultList resultList, String station, String gas){
        ArrayList<XYChart.Data<Long, Double>> data = getXYChartDataList(
                resultList.getSGResult(station, gas).getData()
        );
        XYChart.Series<Long, Double> stationSeries = getGasStationSeries(gas, station);
        ObservableList<XYChart.Data<Long, Double>> seriesData = stationSeries.getData();
        seriesData.clear();
        seriesData.addAll(data);
        //System.out.println(seriesData);
        if(seriesData.isEmpty()){
            System.out.println("The aggregates cannot be displayed as the data in the series is empty");
        }
        else {
            SmearAggregates agg = new SmearAggregates();
            agg.findMaximum(seriesData);
            agg.findMinimum(seriesData);
            agg.findAverage(seriesData);
        }
        return seriesData;
    }

    private void removeOldGases(List<String> gasesToRemove){
        for(String gas: gasesToRemove){
            gases.remove(gas);
        }
    }

    private void addNewGases(List<String> gasesToAdd){
        for(String gas: gasesToAdd){
            gases.put(gas, FXCollections.observableArrayList(new ArrayList<>()));
        }
    }

    private void addNewStationsForGas(String gas, List<String> stations){
        List<XYChart.Series<Long, Double>> gasStations = gases.get(gas);
        for (String station: stations){

            XYChart.Series<Long, Double> stationSeries = new XYChart.Series<>(
                    station,
                    FXCollections.observableArrayList(new ArrayList<>())
            );
            gasStations.add(stationSeries);
        }

    }

    private void removeOldStationsForGas(String gas, List<String> stations){
        List<XYChart.Series<Long, Double>> gasStations = gases.get(gas);
        for(String station: stations){
            gasStations.removeIf(series -> series.getName().equals(station));
        }
    }

    private DiffResult getDiff(List<String> currentKeys, List<String> newKeys){
        ArrayList<String> toAdd = new ArrayList<>(newKeys);
        toAdd.removeAll(currentKeys);
        ArrayList<String> toRemove = new ArrayList(currentKeys);
        toRemove.removeAll(newKeys);
        return new DiffResult(toAdd, toRemove);
    }

    private ArrayList<XYChart.Data<Long, Double>> getXYChartDataList(Map<Long, Double> data){
        ArrayList<XYChart.Data<Long, Double>> xyChartData = new ArrayList<>();
        for(Map.Entry<Long, Double> dataEntry: data.entrySet()){
            XYChart.Data<Long, Double> xy = new XYChart.Data(
                    dataEntry.getKey(),
                    dataEntry.getValue());
            xyChartData.add(xy);
        }
        return xyChartData;
    }
}
