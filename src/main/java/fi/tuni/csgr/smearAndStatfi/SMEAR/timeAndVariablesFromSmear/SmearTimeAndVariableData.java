package fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear;

import com.google.gson.*;
import fi.tuni.csgr.stationNames.Gases;
import fi.tuni.csgr.stationNames.Station;
import fi.tuni.csgr.stationNames.Values;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Class to fetch the gases and their periodStart and periodEnd times from the SMEAR station. It fetches the gases
 * that are closer to the current date.
 */
public class SmearTimeAndVariableData {

    public static final Map<String, Station> smearMapData = getSmearTimeData();

    /**
     * This method can be used to get the station id from SMEAR based on the given station name.
     * Ex: {Kumpula=3, Värriö=1, Hyytiälä=2}.
     * This method checks if the station name exists in the class PredefinedStationsInfo.
     * It also calls two inner methods getSmearJsonArray and getTableVariableIdOfAStation
     */
    private static Map<String, Station> getSmearTimeData() {
        Map<String, Station> mapOfStationClass = new HashMap<>();

        //map to store the station name and its id
        Map<String, Integer> mapOfStationNameAndStationId = new HashMap();

        //JsonArray data from SMEAR API
        String url = "https://smear-backend.rahtiapp.fi/search/station";
        JsonArray urlArray = getSmearJsonArray(url);

        for (int i = 0; i < urlArray.size(); i++) {
            JsonObject urlObject = urlArray.get(i).getAsJsonObject();
            String stationName = urlObject.get("name").getAsString();

            if (PredefinedStationsInfo
                    .stationNameTableVariableName
                    .containsKey(stationName)
            ) {
                mapOfStationNameAndStationId.put(stationName, urlObject.get("id").getAsInt());
            }
        }
        //call getTableVariableIdOfAStation method
        if (!mapOfStationNameAndStationId.isEmpty()) getTableVariableIdOfAStation(mapOfStationNameAndStationId, mapOfStationClass);
        return mapOfStationClass;

    }

    /**
     * This method will add the station name as the key and Station class as the values in the map.
     * Ex: In Kumpula, when two of its variable table names are given, in this instance: "KUM_META" and "KUM_EDDY",
     * it gets a list of its variable tables [2, 8].
     * It calls two inner methods getSmearJsonArray and getMapOfTimeStamp and one class PredefinedStationsInfo
     *
     * @param stationNameAndStationId map containing the name of the stations and its ids
     */
    private static void getTableVariableIdOfAStation(Map<String, Integer> stationNameAndStationId, Map<String, Station> mapOfStationClass_) {

        for (var nameOfTheStation : stationNameAndStationId.keySet()) {

            //station id from Map
            int stationId = stationNameAndStationId.get(nameOfTheStation);
            //data from SMEAR
            StringBuilder url = new StringBuilder("https://smear-backend.rahtiapp.fi/station/")
                    .append(stationId)
                    .append("/table");
            JsonArray urlArray = getSmearJsonArray(url.toString());

            List<Integer> tableIdList = new ArrayList<>();

            for (int i = 0; i < urlArray.size(); i++) {
                JsonObject urlObject = urlArray.get(i).getAsJsonObject();
                if (PredefinedStationsInfo
                        .stationNameTableVariableName
                        .get(nameOfTheStation)
                        .contains(urlObject.get("name").getAsString())
                ) {
                    tableIdList.add(urlObject.get("id").getAsInt());
                }
            }
            Station sName = new Station(nameOfTheStation);

            List<Gases> gases = getListOfGasesClass(url.toString(), tableIdList);
            for(Gases gas: gases){
                sName.addGasToStationMap(gas);
            }

            mapOfStationClass_.put(sName.getName(), sName);
        }
    }

    /**
     * This method returns a list containing the Gases class.
     * It calls two inner methods getSmearJsonArray and gasVariableNameAndTime.
     *
     * @param url_        url of the SMEAR website of variable tables of s station.
     * @param tableIdList a list that contains the table ids of variable tables of a station.
     * @return a list of the Gases class.
     */
    private static List<Gases> getListOfGasesClass(String url_, List<Integer> tableIdList) {
        List<Gases> gases = new ArrayList<>();

        //add all the JsonArray from all the table variables
        JsonArray newArray = new JsonArray();

        for (int tableId = 0; tableId < tableIdList.size(); tableId++) {
            //data from SMEAR
            StringBuilder url = new StringBuilder(url_)
                    .append("/")
                    .append(tableIdList.get(tableId))
                    .append("/variable");
            JsonArray urlArray = getSmearJsonArray(url.toString());
            newArray.addAll(urlArray);
        }

        for(var gas : PredefinedStationsInfo.gasAndItsKeywords.keySet()){
            //Map to store the gas variable name and periodStart, periodEnd values, with key as LocalDate of the periodEnd
            TreeMap<LocalDate, Values> gasTimeStamp = new TreeMap<>();
            gasTimeStamp.putAll(gasVariableNameAndTime(newArray, PredefinedStationsInfo.gasAndItsKeywords.get(gas)));
            gases.add(new Gases(gas, gasTimeStamp.get(gasTimeStamp.lastKey())));
        }

        return gases;
    }

    /**
     * This method checks for given string in the title field of the given JsonArray. It fetches the periodStart,
     * periodEnd values and variable name values.
     *
     * @param jArray      JsonArray fetched from the SMEAR API
     * @param givenTitle_ string to match in the title field of the variable data
     * @return TreeMap with LocalDate of the periodEnd as the key and the class Values os the value.
     */
    private static TreeMap<LocalDate, Values> gasVariableNameAndTime(JsonArray jArray, String givenTitle_) {
        //TreeMap to store Date LocalDate of periodEnd, gas table variable name and periodStart, periodEnd values
        TreeMap<LocalDate, Values> gasVariable = new TreeMap<>();
        //For Hyytiälä "CO₂ concentration" keyword does not give current values
        if(givenTitle_.equals("CO₂ concentration") &&
                jArray.get(0).getAsJsonObject().get("tableName").getAsString().equals("HYY_META")){
            givenTitle_ = "CO₂";
        }

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject jObject = jArray.get(i).getAsJsonObject();

            if (jObject.get("title").isJsonNull() || jObject.get("periodStart").isJsonNull()) continue;
            //Restricting some values from SMEAR
            if (jObject.get("title").getAsString().contains("Std of")) continue;
            if(jObject.get("name").getAsString().contains("NOtower")) continue;

            if (jObject.get("title").getAsString().contains(givenTitle_)) {

                try {

                    StringBuilder variableName = new StringBuilder(jObject.get("tableName").getAsString())
                            .append(".").append(jObject.get("name").getAsString());

                    if (jObject.get("periodEnd").isJsonNull()) {

                        LocalDate periodStart = Instant
                                .parse(jObject.get("periodStart").getAsString() + "Z")
                                .atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate periodEnd = LocalDate.now();
                        Values values = new Values(variableName.toString(), periodStart, periodEnd);

                        gasVariable.put(periodEnd, values);

                    } else {

                        LocalDate periodStart = Instant
                                .parse(jObject.get("periodStart").getAsString() + "Z")
                                .atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate periodEnd = Instant
                                .parse(jObject.get("periodEnd").getAsString() + "Z")
                                .atZone(ZoneId.systemDefault()).toLocalDate();

                        Values values = new Values(variableName.toString(), periodStart, periodEnd);

                        gasVariable.put(periodEnd, values);

                    }
                } catch (DateTimeParseException e) {
                }

            }
        }

        return gasVariable;
    }

    /**
     * This method fetches the Json values using the given SMEAR API. It returns JsonArray of the given API.
     *
     * @param url url to fetch the Json Array from
     * @return return JsonArray fetched from the API
     */
    private static JsonArray getSmearJsonArray(String url) {
        //data from SMEAR API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)).build();
        String st = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        //parsing the url string to Json
        JsonElement urlElement = JsonParser.parseString(st);
        JsonArray urlArray = urlElement.getAsJsonArray();
        return urlArray;
    }

    /*public static void main(String[] args) {
        var sinfo =  SmearTimeAndVariableData.getSmearTimeData();
        for(var i : sinfo.keySet()){
            System.out.println(i);
            for(var j : sinfo.get(i).getStationMap().keySet()){
            System.out.print(j + ": VariableName " + sinfo.get(i).getStationMap().get(j).getGasValues().getVariableName());
            System.out.print(", Start Date: "+sinfo.get(i).getStationMap().get(j).getGasValues().getStartDate());
            System.out.print(", End Date: "+ sinfo.get(i).getStationMap().get(j).getGasValues().getEndDate());
            System.out.println("");

            }
        }
    }*/

}
