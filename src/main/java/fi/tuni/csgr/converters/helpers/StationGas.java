package fi.tuni.csgr.converters.helpers;

import java.util.Objects;

public class StationGas {
    private String station;
    private String gas;

    public StationGas(String station, String gas) {
        this.station = station;
        this.gas = gas;
    }

    public String getStation() {
        return station;
    }

    public String getGas() {
        return gas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationGas that = (StationGas) o;
        return Objects.equals(gas, that.gas) && Objects.equals(station, that.station);
    }

    @Override
    public int hashCode() {
        return gas.hashCode() + station.hashCode();
    }
}
