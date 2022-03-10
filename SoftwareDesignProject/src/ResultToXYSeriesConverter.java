import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.time.LocalDateTime;
import java.util.Map;

public class ResultToXYSeriesConverter {
    static XYChart.Series<LocalDateTime, Double> convert(Result result){
        String name = result.gas;
        ObservableList<XYChart.Data> dataList = FXCollections.observableArrayList();
        for(Map.Entry dataEntry : result.data.entrySet()){
            XYChart.Data<LocalDateTime, Double> data = new XYChart.Data<LocalDateTime, Double>(
                    (LocalDateTime) dataEntry.getKey(),
                    (Double) dataEntry.getValue()
            );
            dataList.add(data);
        }
        return new XYChart.Series(name, dataList);
    }
}
