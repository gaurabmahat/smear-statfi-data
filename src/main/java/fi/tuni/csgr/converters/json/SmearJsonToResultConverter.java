package fi.tuni.csgr.converters.json;

import com.google.gson.Gson;
import fi.tuni.csgr.converters.slug.StationGasSlugConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class SmearJsonToResultConverter implements JsonToResultConverter {

    private static Map getMapFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Map.class);
    }

    private List<String> getColumns(Map responseMap){
        return (List<String>) responseMap.get("columns");
    }
    private List<Map> getData(Map response){
        return (List<Map>) response.get("data");
    }

    public ResultList convert(String json){
        ResultList resultList = new ResultList();
        Map jsonMap = getMapFromJson(json);
        for (String slug: getColumns(jsonMap)){
            String[] stationGas = StationGasSlugConverter.getStationGas(slug);
            SGResult newSGResult = new SGResult();
            for(Map dataSample: getData(jsonMap)){
                Double value = (Double) dataSample.get(slug);
                LocalDateTime dateTime = LocalDateTime.
                        parse((CharSequence) dataSample.get("samptime"),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                newSGResult.addDataEntry(dateTime, value);
            }
            resultList.addResult(stationGas[0], stationGas[1], newSGResult);
        }
        return resultList;
    }

    public static void main(String[] args) {
        String json = "{\"aggregation\":\"MAX\",\"aggregationInterval\":60,\"columns\":[\"KUM_EDDY.av_c_ep\",\"HYY_META.CO2icos168\"],\"data\":[{\"KUM_EDDY.av_c_ep\":418.917,\"samptime\":\"2022-01-19T14:00:00.000\",\"HYY_META.CO2icos168\":417.8223},{\"KUM_EDDY.av_c_ep\":418.803,\"samptime\":\"2022-01-19T15:00:00.000\",\"HYY_META.CO2icos168\":417.3998},{\"KUM_EDDY.av_c_ep\":419.309,\"samptime\":\"2022-01-19T16:00:00.000\",\"HYY_META.CO2icos168\":417.5876}],\"endTime\":\"2022-01-19T17:00:00.000\",\"recordCount\":3,\"startTime\":\"2022-01-19T14:00:00.000\"}";
        SmearJsonToResultConverter converter = new SmearJsonToResultConverter();
        ResultList results = converter.convert(json);
        System.out.println(results.size());
        System.out.println(results.getSGResult("Kumpula", "CO2"));

    }


}

