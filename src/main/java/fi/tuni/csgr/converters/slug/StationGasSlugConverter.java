package fi.tuni.csgr.converters.slug;

import fi.tuni.csgr.converters.helpers.StationGas;
import fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear.PredefinedStationsInfo;
import fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear.SmearTimeAndVariableData;

import java.util.HashMap;
import java.util.Map;

public class StationGasSlugConverter {
    private static final Map<String, StationGas> slugToStationGasMapping = getSlugToStationGasMapping();

    private static Map<String, StationGas> getSlugToStationGasMapping(){
        Map<String, StationGas> retrieveSlugToStationGasMapping = new HashMap<>();

        for(var gas : PredefinedStationsInfo.gasAndItsKeywords.keySet()){
            for(var station : PredefinedStationsInfo.stationNameTableVariableName.keySet()){
                var key = SmearTimeAndVariableData.smearMapData
                        .get(station)
                        .getStationMap()
                        .get(gas)
                        .getGasValues()
                        .getVariableName();
                var value = new StationGas(station, gas);
                retrieveSlugToStationGasMapping.put(key, value);
            }
        }
        return retrieveSlugToStationGasMapping;
    }

    private static final Map<StationGas, String> stationGasToSlugMapping = getStationGasToSlugMapping();

    private static Map<StationGas, String> getStationGasToSlugMapping(){
        Map<StationGas, String> retrieveStationGasToSlugMapping = new HashMap<>();

        for(var gas : PredefinedStationsInfo.gasAndItsKeywords.keySet()){
            for(var station : PredefinedStationsInfo.stationNameTableVariableName.keySet()){
                var key = new StationGas(station, gas);
                var value = SmearTimeAndVariableData.smearMapData
                        .get(station)
                        .getStationMap()
                        .get(gas)
                        .getGasValues()
                        .getVariableName();
                retrieveStationGasToSlugMapping.put(key,value);
            }
        }
        return retrieveStationGasToSlugMapping;
    }


    /*private static final Map<String, StationGas> slugToStationGasMapping = Map.of(
            "KUM_EDDY.av_c_ep", new StationGas("Kumpula", "CO2"),
            "KUM_META.SO_2", new StationGas("Kumpula", "SO2"),
            "KUM_META.NO", new StationGas("Kumpula", "NO"),
            "HYY_META.CO2icos168", new StationGas("Hyytiälä", "CO2"),
            "HYY_META.SO2168", new StationGas("Hyytiälä", "SO2"),
            "HYY_META.NO168", new StationGas("Hyytiälä", "NO"),
            "VAR_EDDY.av_c", new StationGas("Värriö", "CO2"),
            "VAR_META.SO2_1", new StationGas("Värriö", "SO2"),
            "VAR_META.NO_1", new StationGas("Värriö", "NO")

    );

    private static final Map<StationGas, String> stationGasToSlugMapping = Map.of(
            new StationGas("Kumpula", "CO2"), "KUM_EDDY.av_c_ep",
            new StationGas("Kumpula", "SO2"), "KUM_META.SO_2",
            new StationGas("Kumpula", "NO"), "KUM_META.NO",
            new StationGas("Hyytiälä", "CO2"), "HYY_META.CO2icos168",
            new StationGas("Hyytiälä", "SO2"), "HYY_META.SO2168",
            new StationGas("Hyytiälä", "NO"), "HYY_META.NO168",
            new StationGas("Värriö", "CO2"), "VAR_EDDY.av_c",
            new StationGas("Värriö", "SO2"), "VAR_META.SO2_1",
            new StationGas("Värriö", "NO"), "VAR_META.NO_1"

    );*/

    public static String getSlug(String stationName, String gas){
        return stationGasToSlugMapping.get(new StationGas(stationName, gas));
    }

    public static StationGas getStationGas(String slug){
        return slugToStationGasMapping.get(slug);
    }
}
