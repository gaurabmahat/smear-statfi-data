import java.util.List;
import java.util.Map;

public class ResultSkeleton {
    String aggregation;
    int aggregationInterval;
    List<String> columns;
    List<Map<String, Object>> data;
    String endTime;
    int recordCount;
    String startTime;
}
