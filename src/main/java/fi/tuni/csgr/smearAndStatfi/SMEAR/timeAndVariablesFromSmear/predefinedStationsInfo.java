package fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class predefinedStationsInfo {
    //Map containing the station name and the table variable name to look for
    public static final Map<String, List<String>> stationNameTableVariableName = Map.of(
            "Kumpula", Arrays.asList("KUM_EDDY", "KUM_META"),
            "Hyytiälä", Arrays.asList("HYY_META"),
            "Värriö", Arrays.asList("VAR_EDDY", "VAR_META")
    );

    public static final Map<String, String> gasAndItsKeywords = Map.of(
            "CO2", "CO₂ concentration",
            "SO2","SO₂ concentration",
            "NO", "NO concentration"
    );
}
