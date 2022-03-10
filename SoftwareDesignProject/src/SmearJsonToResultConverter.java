import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmearJsonToResultConverter implements JsonToResultConverter {

    private static final Map<String, String[]> codeToStationGasMapping = Map.of(
            "KUM_EDDY.av_c_ep", new String[] {"Kumpula", "CO2"},
            "KUM_META.SO_2", new String[] {"Kumpula", "SO2"},
            "KUM_META.NO", new String[] {"Kumpula", "NO"},
            "HYY_META.CO2icos168", new String[] {"Hyytiälä", "CO2"},
            "HYY_META.SO2168", new String[] {"Hyytiälä", "SO2"},
            "HYY_META.NO168", new String[] {"Hyytiälä", "NO"},
            "VAR_EDDY.av_c", new String[] {"Värriö", "CO2"},
            "VAR_META.SO2_1", new String[] {"Värriö", "SO2"},
            "VAR_META.NO_1", new String[] {"Värriö", "NO"}

    );


    private static SmearResultJsonObject getObjectFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, SmearResultJsonObject.class);
    }

    public List<Result> convert(String json){
        ArrayList<Result> convertedResults = new ArrayList<>();
        SmearResultJsonObject jsonObj = getObjectFromJson(json);

        for(String entry: jsonObj.columns){
            String[] stationGas = codeToStationGasMapping.get(entry);
            Result newResult = new Result(stationGas[0], stationGas[1]);
            for(Map dataSample: jsonObj.data){
                Double value = (Double) dataSample.get(entry);
                String dateTimeString = (String) dataSample.get("samptime");
                LocalDateTime dateTime = LocalDateTime.
                        parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                newResult.addDataEntry(dateTime, value);
            }
            convertedResults.add(newResult);
        }

        return convertedResults;
    }

    public static void main(String[] args) {
        String json = "{\"aggregation\":\"MAX\",\"aggregationInterval\":60,\"columns\":[\"KUM_EDDY.av_c_ep\",\"HYY_META.CO2icos168\"],\"data\":[{\"KUM_EDDY.av_c_ep\":418.917,\"samptime\":\"2022-01-19T14:00:00.000\",\"HYY_META.CO2icos168\":417.8223},{\"KUM_EDDY.av_c_ep\":418.803,\"samptime\":\"2022-01-19T15:00:00.000\",\"HYY_META.CO2icos168\":417.3998},{\"KUM_EDDY.av_c_ep\":419.309,\"samptime\":\"2022-01-19T16:00:00.000\",\"HYY_META.CO2icos168\":417.5876}],\"endTime\":\"2022-01-19T17:00:00.000\",\"recordCount\":3,\"startTime\":\"2022-01-19T14:00:00.000\"}";
        SmearJsonToResultConverter converter = new SmearJsonToResultConverter();
        List<Result> results = converter.convert(json);
        System.out.println(results.size());
        for(Result result: results){
            System.out.println(result.gas);
            System.out.println(result.station);
            System.out.println(result.data);
        }
    }


}
