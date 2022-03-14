/*
* This class will return a string that will contain the tablevariable name of the gas corresponding to its station.
* The class takes a List<List<String>> value.
* If the user selects 'CO2' gas and selects all three station, we need to create a List<String> for each station as:
*       List<String> a = Arrays.asList(new String[]{"CO2", "Kumpula"});
*       List<String> b = Arrays.asList(new String[]{"CO2", "Hyytiälä"});
*       List<String> c = Arrays.asList(new String[]{"CO2", "Värriö"});
* Then create a new List: List<List<String>> listStr = new ArrayList<>();
*       listStr.add(a);
*       listStr.add(b);
*       listStr.add(c);
* After that we can call this class as:
*       String station = new stationNames(listStr).getStation();
* The return string in this case will be:
*       &tablevariable=KUM_EDDY.av_c_ep&tablevariable=HYY_META.CO2icos168&tablevariable=VAR_EDDY.av_c
* */

package fi.tuni.csgr.SMEAR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class stationNames {
    private List<List<String>> gasStationName;
    private static final Map<List<String>, String> stationNameMapping = Map.of(
            Arrays.asList(new String[]{"CO2", "Kumpula"}), "KUM_EDDY.av_c_ep",
            Arrays.asList(new String[]{"SO2", "Kumpula"}), "KUM_META.SO_2",
            Arrays.asList(new String[]{"NO", "Kumpula"}), "KUM_META.NO",
            Arrays.asList(new String[]{"CO2", "Hyytiälä"}), "HYY_META.CO2icos168",
            Arrays.asList(new String[]{"SO2", "Hyytiälä"}), "HYY_META.SO2168",
            Arrays.asList(new String[]{"NO", "Hyytiälä"}), "HYY_META.NO168",
            Arrays.asList(new String[]{"CO2", "Värriö"}), "VAR_EDDY.av_c",
            Arrays.asList(new String[]{"SO2", "Värriö"}), "VAR_META.SO2_1",
            Arrays.asList(new String[]{"NO", "Värriö"}), "VAR_META.NO_1"
    );

    public stationNames(List<List<String>> gasStation) {
        gasStationName = gasStation;
    }

    public String getStation() {
        StringBuilder tableVariable = new StringBuilder("");
        for (var item : gasStationName) {
            String stationCode = stationNameMapping.get(item);
            tableVariable.append("&tablevariable=" + stationCode);
        }
        return tableVariable.toString();
    }

    //To test the class
    public static void main(String[] args){
        List<String> a = Arrays.asList(new String[]{"CO2", "Kumpula"});
        List<String> b = Arrays.asList(new String[]{"CO2", "Hyytiälä"});
        List<String> c = Arrays.asList(new String[]{"CO2", "Värriö"});

        List<List<String>> listStr = new ArrayList<>();
        listStr.add(a);
        listStr.add(b);
        listStr.add(c);

        System.out.println(new stationNames(listStr).getStation());
    }

}
