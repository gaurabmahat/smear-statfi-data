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

package fi.tuni.csgr.smearAndStatfi.SMEAR.fetchSeriesDataFromSmear;

import fi.tuni.csgr.MainFXMLController;
import fi.tuni.csgr.stationNames.stationNameMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class getTableVariable {
    private List<String> gases;
    private List<String> stations;

    public getTableVariable(List<String> gases_, List<String> stations_) {
        this.gases = gases_;
        this.stations = stations_;
    }

    public String getStationsCode() {
        StringBuilder tableVariable = new StringBuilder("");
        for (String gas : this.gases) {
            for(String station : this.stations){
                String stationCode = MainFXMLController.getInitialDataFromSmear.get(station).getStationMap().get(gas).getVariableName();
                tableVariable.append("&tablevariable=" + stationCode);
            }
        }
        return tableVariable.toString();
    }

    //To test the class
    public static void main(String[] args){
        /*List<String> a = Arrays.asList(new String[]{"CO2", "SO2"});
        List<String> b = Arrays.asList(new String[]{"Kumpula"});

        System.out.println(new getTableVariable(a,b).getStationsCode());*/
    }

}
