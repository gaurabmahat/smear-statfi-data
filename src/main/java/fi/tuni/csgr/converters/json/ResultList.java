package fi.tuni.csgr.converters.json;

import java.util.*;

public class ResultList implements Iterable<SGResult>{
    private HashMap<StationGas, SGResult> results;

    public ResultList() {
        this.results = new HashMap<>();
    }

    public void addResult(String station, String gas, SGResult SGResult){
        results.put(
                new StationGas(station, gas),
                SGResult
        );
    }

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
