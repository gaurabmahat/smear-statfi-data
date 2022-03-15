package fi.tuni.csgr.converters.json;

import java.time.LocalDateTime;
import java.util.HashMap;

public class SGResult {

    HashMap<LocalDateTime, Double> data;

    public SGResult() {
        this.data = new HashMap<LocalDateTime, Double>();
    }

    public HashMap<LocalDateTime, Double> getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public void addDataEntry(LocalDateTime dateTime, Double value) {
        data.put(dateTime, value);
    }
}
