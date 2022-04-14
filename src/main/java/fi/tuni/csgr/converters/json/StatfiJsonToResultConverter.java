package fi.tuni.csgr.converters.json;

import fi.tuni.csgr.converters.helpers.StationGas;

import java.util.List;
import java.util.Map;

public class StatfiJsonToResultConverter implements JsonToResultConverter{

    private Map<String, Double> getGasesAndIndex(Map responseMap){
        return ((Map<String, Map<String, Double>>) ( (Map) ((Map<String, Map>) responseMap.get("dimension"))
                .get("Tiedot")).get("category")).get("index");
    }

    private Map<String, Double> getYearsAndIndexes(Map responseMap){
        return ((Map<String, Map<String, Double>>) (((Map<String, Map>) responseMap.get("dimension"))
                .get("Vuosi")).get("category")).get("index");
    }

    private int numberOfEntriesPerGas(Map responseMap){
        return ((List<Double>) responseMap.get("size")).get(1).intValue();
    }

    private double getYearGasValue(Map responseMap, int gasIndex, int yearIndex, int entriesPerGas){
        return ((List<Double>) responseMap.get("value")).get(gasIndex * entriesPerGas + yearIndex);
    }

    @Override
    public ResultList convert(String json) {
        ResultList resultList = new ResultList();
        Map jsonMap = JsonToMapConverter.convert(json);
        Map<String, Double> gasesAndIndexes = getGasesAndIndex(jsonMap);
        Map<String, Double> yearsAndIndexes = getYearsAndIndexes(jsonMap);

        int entriesPerGas = numberOfEntriesPerGas(jsonMap);

        for (Map.Entry<String, Double> gasAndIndex: gasesAndIndexes.entrySet()){
            String gas = gasAndIndex.getKey();
            int gasIndex = gasAndIndex.getValue().intValue();
            SGResult newSGResult = new SGResult();
            for (Map.Entry<String, Double> yearAndIndex: yearsAndIndexes.entrySet()){
                String year = yearAndIndex.getKey();
                int yearIndex = yearAndIndex.getValue().intValue();
                double value = getYearGasValue(jsonMap, gasIndex, yearIndex, entriesPerGas);

                newSGResult.addDataEntry(Long.valueOf(year), value);
            }
            resultList.addResult(new StationGas("statfi", gas), newSGResult);
        }
        return resultList;
    }
}
