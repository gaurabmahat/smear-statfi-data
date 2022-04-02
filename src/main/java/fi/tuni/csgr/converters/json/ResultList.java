package fi.tuni.csgr.converters.json;

import fi.tuni.csgr.converters.helpers.StationGas;

import java.util.*;

public class ResultList {
    private HashMap<StationGas, SGResult> results;

    public ResultList() {
        this.results = new HashMap<>();
    }

    public void addResult(StationGas stationGas, SGResult SGResult){
        results.put(
                stationGas,
                SGResult
        );
    }

    public int size(){
        return this.results.size();
    }

    public SGResult getSGResult(String station, String gas){
        return results.get(new StationGas(station, gas));
    }

    public List<String> getGases(){
        Set<String> gases = new HashSet<>();
        for(StationGas sg: results.keySet()){
            gases.add(sg.getGas());
        }
        return gases.stream().toList();
    }

    public List<String> getStationsForGas(String gas){
        Set<String> stations = new HashSet<>();
        for(StationGas sg: results.keySet()){
            if (sg.getGas().equals(gas)){
                stations.add(sg.getStation());
            }

        }
        return stations.stream().toList();
    }
}
