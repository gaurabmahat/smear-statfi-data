package fi.tuni.csgr.managers.graphs;

import fi.tuni.csgr.converters.json.ResultList;
import javafx.collections.*;
import javafx.scene.chart.XYChart;

import java.util.*;

/**
 * Class for managing all data displayed on graphs
 *
 * @author Cecylia Borek
 */

public class GraphDataManager {

    ObservableMap<String, ObservableMap<String, ObservableList<XYChart.Data<Long, Double>>>> gases;

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

        for(Map.Entry<String, ObservableMap<String, ObservableList<XYChart.Data<Long, Double>>>> gasEntry:
                gases.entrySet()){
            String gas = gasEntry.getKey();
            Map<String, ObservableList<XYChart.Data<Long, Double>>> currentStationsForGasMap =
                    gasEntry.getValue();

            List<String> resultsStationsForGas = results.getStationsForGas(gas);
            List<String> currentStationsForGas = currentStationsForGasMap.keySet().stream().toList();

            DiffResult stationsDiff = getDiff(
                    currentStationsForGas,
                    resultsStationsForGas);
            removeOldStationsForGas(gas, stationsDiff.getToRemove());
            addNewStationsForGas(gas, stationsDiff.getToAdd());
        }
        for (String gas: gases.keySet()){
            for (String station: gases.get(gas).keySet()){
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
    public void addGasListener(String gas, MapChangeListener listener){
        gases.get(gas).addListener(listener);
    }

    /**
     * adds listener to the list storing results for the specified gas and station
     * @param gas
     * @param station
     * @param listener
     */
    public void addGasStationListener(String gas, String station, ListChangeListener listener){
        gases.get(gas).get(station).addListener(listener);
    }

    private void updateGasStationResults(ResultList resultList, String station, String gas){
        ArrayList<XYChart.Data<Long, Double>> data = getXYChartDataList(
                resultList.getSGResult(station, gas).getData()
        );

        ObservableList<XYChart.Data<Long, Double>> gasStationResultList = gases.get(gas).get(station);
        gasStationResultList.clear();
        gasStationResultList.addAll(data);
    }

    private void removeOldGases(List<String> gasesToRemove){
        for(String gas: gasesToRemove){
            gases.remove(gas);
        }
    }

    private void addNewGases(List<String> gasesToAdd){
        for(String gas: gasesToAdd){
            gases.put(gas, FXCollections.observableMap(new HashMap<>()));
        }
    }

    private void addNewStationsForGas(String gas, List<String> stations){
        Map<String, ObservableList<XYChart.Data<Long, Double>>> gasStations = gases.get(gas);
        for(String station: stations){
            gasStations.put(station, FXCollections.observableArrayList(new ArrayList<>()));
        }
    }

    private void removeOldStationsForGas(String gas, List<String> stations){
        Map<String, ObservableList<XYChart.Data<Long, Double>>> gasStations = gases.get(gas);
        for(String station: stations){
            gasStations.remove(station);
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