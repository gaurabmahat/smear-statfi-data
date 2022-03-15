package fi.tuni.csgr.converters.json;

import fi.tuni.csgr.converters.helpers.StationGas;

import java.util.*;

public class SGResultList implements Iterable<SGResult>{
    private HashMap<StationGas, SGResult> results;

    public SGResultList() {
        this.results = new HashMap<>();
    }

    public void addResult(StationGas stationGas, SGResult SGResult){
        results.put(
                stationGas,
                SGResult
        );
    }

    //todo:
    @Override
    public Iterator<SGResult> iterator() {
        return null;
    }

    public int size(){
        return this.results.size();
    }

    public SGResult getSGResult(String station, String gas){
        return results.get(new StationGas(station, gas));
    }
}
