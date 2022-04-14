package fi.tuni.csgr.stationNames;

import java.util.HashMap;
import java.util.Map;

public class Station {
    private final String name;
    private final Map<String, Gases> stationMap;

    public Station(String name_) {
        this.name = name_;
        this.stationMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addGasToStationMap(Gases gases_) {
        stationMap.put(gases_.getGasName(), gases_);
    }

    public Map<String, Gases> getStationMap() {
        return this.stationMap;
    }
}
