package fi.tuni.csgr.managers.graphs;

import fi.tuni.csgr.converters.json.ResultList;
import javafx.collections.*;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarGraphDataManager {
    ObservableMap<String, ObservableList<XYChart.Series<String, Double>>> gases;

    public BarGraphDataManager(){
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

    public ObservableMap<String, ObservableList<XYChart.Series<String, Double>>> getGases() {
        return gases;
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
        List<XYChart.Series<String, Double>> stationSeries = gases.get(gas);
        return stationSeries.stream().map(s -> s.getName()).toList();
    }

    private XYChart.Series<String, Double> getGasStationSeries(String gas, String station){
        List<XYChart.Series<String, Double>> allGasSeries = gases.get(gas);
        int i = 0;
        while (! allGasSeries.get(i).getName().equals(station)){
            i++;
        }
        return allGasSeries.get(i);
    }

    public List<XYChart.Data<String, Double>> updateGasStationResults(ResultList resultList, String station, String gas){
        ArrayList<XYChart.Data<String, Double>> data = getXYChartDataList(
                resultList.getSGResult(station, gas).getData()
        );
        XYChart.Series<String, Double> stationSeries = getGasStationSeries(gas, station);
        ObservableList<XYChart.Data<String, Double>> seriesData = stationSeries.getData();
        seriesData.clear();
        seriesData.addAll(data);

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
        List<XYChart.Series<String, Double>> gasStations = gases.get(gas);
        for (String station: stations){

            XYChart.Series<String, Double> stationSeries = new XYChart.Series<>(
                    station,
                    FXCollections.observableArrayList(new ArrayList<>())
            );
            gasStations.add(stationSeries);
        }

    }

    private void removeOldStationsForGas(String gas, List<String> stations){
        List<XYChart.Series<String, Double>> gasStations = gases.get(gas);
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

    private ArrayList<XYChart.Data<String, Double>> getXYChartDataList(Map<Long, Double> data){
        ArrayList<XYChart.Data<String, Double>> xyChartData = new ArrayList<>();
        for(Map.Entry<Long, Double> dataEntry: data.entrySet()){
            XYChart.Data<String, Double> xy = new XYChart.Data(
                    Long.toString(dataEntry.getKey()),
                    dataEntry.getValue());
            xyChartData.add(xy);
        }
        return xyChartData;
    }
}
