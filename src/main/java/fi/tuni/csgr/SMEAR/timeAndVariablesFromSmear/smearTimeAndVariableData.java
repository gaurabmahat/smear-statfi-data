package fi.tuni.csgr.SMEAR.timeAndVariablesFromSmear;

import com.google.gson.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Class to fetch the gases and their periodStart and periodEnd times from the SMEAR station. It fetches the gases
 * that are closer to the current date.
 */
public class smearTimeAndVariableData {

    private final Map<String, Map<String, Map<String, List<Instant>>>> mapOfStationNameGasesAndTimeStamp;

    /**
     * Initializes the map
     */
    public smearTimeAndVariableData() {
        mapOfStationNameGasesAndTimeStamp = new HashMap<>();
    }

    /**
     * Calculates the data from the SMEAR API to get the gas variables. Returns a map with all the required information.
     *
     * @return map that contains name of the station, name of the gases, variable name of the gases and the gases
     * periodStart and periodEnd values.
     */
    public Map<String, Map<String, Map<String, List<Instant>>>> getSmearTimeData() {
        getMapOfStationNameAndId();
        return mapOfStationNameGasesAndTimeStamp;
    }

    /**
     * This method can be used to get the station id from SMEAR based on the given station name.
     * Ex: {Kumpula=3, Värriö=1, Hyytiälä=2}.
     * It calls one other class stationNamesAndTheirTablesNames
     * It also calls two inner methods getSmearJsonArray and getTableVariableIdOfAStation
     */
    private void getMapOfStationNameAndId() {

        //map to store the station and its id
        Map<String, Integer> mapOfStationNameAndStationId = new HashMap();

        //JsonArray data from SMEAR API
        String url = "https://smear-backend.rahtiapp.fi/search/station";
        JsonArray urlArray = getSmearJsonArray(url);

        for (int i = 0; i < urlArray.size(); i++) {
            JsonObject urlObject = urlArray.get(i).getAsJsonObject();
            String stationName = urlObject.get("name").getAsString();

            if (stationNamesAndTheirTablesNames
                    .stationNameTableVariableName
                    .containsKey(stationName)
            ) {
                mapOfStationNameAndStationId.put(stationName, urlObject.get("id").getAsInt());
            }
        }
        //call getTableVariableIdOfAStation method
        if (!mapOfStationNameAndStationId.isEmpty()) getTableVariableIdOfAStation(mapOfStationNameAndStationId);
    }

    /**
     * This method can be used to get a list that contains the variable table id of the stations. It fetches
     * the variable table id of the given variable table name.
     * Ex: In Kumpula, when two of its variable table names are given, in this instance: "KUM_META" and "KUM_EDDY",
     * it gets a list of its variable tables [2, 8].
     * It calls two inner methods getSmearJsonArray and getMapOfTimeStamp and one class stationNamesAndTheirTablesNames
     *
     * @param stationNameAndStationId map containing the name of the stations and its ids
     */
    private void getTableVariableIdOfAStation(Map<String, Integer> stationNameAndStationId) {

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
                if (stationNamesAndTheirTablesNames
                        .stationNameTableVariableName
                        .get(nameOfTheStation)
                        .contains(urlObject.get("name").getAsString())
                ) {
                    tableIdList.add(urlObject.get("id").getAsInt());
                }
            }

            mapOfStationNameGasesAndTimeStamp.put(nameOfTheStation, getMapOfTimeStamp(url.toString(), tableIdList));
        }
    }

    /**
     * This method a Map containing the name of the gas, its variable id and its periodStart and periodEnd values.
     * It calls an inner method gasVariableNameAndTime where it passes a string parameter that will decide which
     * gas variable to search for.
     * It calls two inner methods getSmearJsonArray and gasVariableNameAndTime.
     *
     * @param url_        url of the SMEAR website of variable tables of s station.
     * @param tableIdList a list that contains the table ids of variable tables of a station.
     * @return map with gas name, gas variable name and periodStart, periodEnd as list.
     */
    private Map<String, Map<String, List<Instant>>> getMapOfTimeStamp(String url_, List<Integer> tableIdList) {
        //Map to store the name of the gas, its variable name and periodStart, periodEnd values
        Map<String, Map<String, List<Instant>>> variableNameAndTimeStamp = new HashMap<>();
        //Map to store the gas variable name and periodStart, periodEnd values, with key as Date Instant of the periodEnd
        TreeMap<Instant, Map<String, List<Instant>>> co2TimeStamp = new TreeMap<>();
        TreeMap<Instant, Map<String, List<Instant>>> so2TimeStamp = new TreeMap<>();
        TreeMap<Instant, Map<String, List<Instant>>> noTimeStamp = new TreeMap<>();

        for (int tableId = 0; tableId < tableIdList.size(); tableId++) {
            //data from SMEAR
            StringBuilder url = new StringBuilder(url_)
                    .append("/")
                    .append(tableIdList.get(tableId))
                    .append("/variable");
            JsonArray urlArray = getSmearJsonArray(url.toString());

            //For CO2
            //string to look for in the title field
            String co2Title = "CO₂ concentration";
            co2TimeStamp.putAll(gasVariableNameAndTime(urlArray, co2Title));
            //For SO2
            //string to look for in the title field
            String so2Title = "SO₂ concentration";
            so2TimeStamp.putAll(gasVariableNameAndTime(urlArray, so2Title));
            //For NO
            //string to look for in the title field
            String noTitle = "NO concentration";
            noTimeStamp.putAll(gasVariableNameAndTime(urlArray, noTitle));
        }

        if (!co2TimeStamp.isEmpty()) variableNameAndTimeStamp.put("CO2", co2TimeStamp.get(co2TimeStamp.lastKey()));
        if (!so2TimeStamp.isEmpty()) variableNameAndTimeStamp.put("SO2", so2TimeStamp.get(so2TimeStamp.lastKey()));
        if (!noTimeStamp.isEmpty()) variableNameAndTimeStamp.put("NO", noTimeStamp.get(noTimeStamp.lastKey()));

        return variableNameAndTimeStamp;
    }

    /**
     * This method checks for given string in the title field of the given JsonArray. It fetches the periodStart and
     * periodEnd values from the matching variable.
     *
     * @param jArray      JsonArray fetched from the SMEAR API
     * @param givenTitle_ string to match in the title field of the variable data
     * @return map with Date Instant of the periodEnd value, gas variable name and periodStart, periodEnd values
     */
    private TreeMap<Instant, Map<String, List<Instant>>> gasVariableNameAndTime(JsonArray jArray, String givenTitle_) {
        //TreeMap to store Date Instant of periodEnd, gas table variable name and periodStart, periodEnd values
        TreeMap<Instant, Map<String, List<Instant>>> gasVariable = new TreeMap<>();

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject jObject = jArray.get(i).getAsJsonObject();

            if (jObject.get("title").isJsonNull() || jObject.get("periodStart").isJsonNull()) continue;

            if (jObject.get("title").getAsString().contains(givenTitle_)) {

                try {

                    StringBuilder variableName = new StringBuilder(jObject.get("tableName").getAsString())
                            .append(".").append(jObject.get("name").getAsString());

                    if (jObject.get("periodEnd").isJsonNull()) {
                        List<Instant> lst = Arrays.asList(Instant.parse(jObject.get("periodStart").getAsString() + "Z"),
                                Instant.now());
                        Map<String, List<Instant>> gas = Map.of(variableName.toString(), lst);
                        gasVariable.put(Instant.now(), gas);

                    } else {
                        List<Instant> lst = Arrays.asList(Instant.parse(jObject.get("periodStart").getAsString() + "Z"),
                                Instant.parse(jObject.get("periodEnd").getAsString() + "Z"));
                        Map<String, List<Instant>> gas = Map.of(variableName.toString(), lst);
                        gasVariable.put(Instant.parse(jObject.get("periodEnd").getAsString() + "Z"), gas);
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
    private JsonArray getSmearJsonArray(String url) {
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

    public static void main(String[] args) {
        //System.out.println(new smearTimeAndVariableData().getSmearTimeData().toString());
    }

}
