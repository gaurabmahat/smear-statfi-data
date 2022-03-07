import java.time.LocalDateTime;
import java.util.HashMap;

public class Result {
    String station;
    String gas;
    HashMap<LocalDateTime, Double> data;

    public Result(String station, String gas) {
        this.station = station;
        this.gas = gas;
        this.data = new HashMap<>();
    }

    public void addDataEntry(LocalDateTime dateTime, Double value) {
        data.put(dateTime, value);
    }
}
