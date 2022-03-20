package fi.tuni.csgr.converters.json;

import java.util.HashMap;

public class SGResult {

    HashMap<Long, Double> data;

    public SGResult() {
        this.data = new HashMap<Long, Double>();
    }

    public HashMap<Long, Double> getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public void addDataEntry(Long dateTime, Double value) {
        data.put(dateTime, value);
    }
}
