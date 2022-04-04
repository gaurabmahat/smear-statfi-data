package fi.tuni.csgr.stationNames;

import java.util.Map;

public class Gases {
    private final String name;
    private final Values values;

    public Gases(String name_, Values values_) {
        this.name = name_;
        this.values = values_;
    }

    public String getGasName() {
        return this.name;
    }

    public Values getGasValues() {
        return this.values;
    }
}
