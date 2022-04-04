package fi.tuni.csgr.stationNames;

import java.util.HashMap;
import java.util.Map;

public class Station {
    private final String name;
    private final Map<String, Values> stationMap;

    public Station(String name_) {
        this.name = name_;
        this.stationMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addGasToStationMap(Gases gases_) {
        stationMap.put(gases_.getGasName(), gases_.getGasValues());
    }

    public Map<String, Values> getStationMap() {
        return this.stationMap;
    }
}
