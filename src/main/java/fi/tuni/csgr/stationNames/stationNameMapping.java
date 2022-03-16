package fi.tuni.csgr.stationNames;

import java.util.Map;

public class stationNameMapping {
    public static final Map<String, Map<String, String>> mapStationAndGas = Map.of(
            "Kumpula", Map.of("CO2", "KUM_EDDY.av_c_ep",
                    "SO2", "KUM_META.SO_2",
                    "NO", "KUM_META.NO"),
            "Hyytiälä", Map.of("CO2", "HYY_META.CO2icos168",
                    "SO2", "HYY_META.SO2168",
                    "NO", "HYY_META.NO168"),
            "Värriö", Map.of("CO2", "VAR_EDDY.av_c",
                    "SO2", "VAR_META.SO2_1",
                    "NO", "VAR_META.NO_1")

    );
}
