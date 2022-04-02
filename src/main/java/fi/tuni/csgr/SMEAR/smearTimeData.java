package fi.tuni.csgr.SMEAR;

import com.google.gson.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Class to fetch the gases and their periodStart and periodEnd times from the SMEAR station. It fetches the gases
 * that was measured for the longest of time.
 */
public class smearTimeData {

    private final Map<String, Map<String, Map<String, List<String>>>> mapOfStationNameGasesAndTimeStamp;

    /**
     * Initializes the map
     */
    public smearTimeData() {
        mapOfStationNameGasesAndTimeStamp = new HashMap<>();
    }

    /**
     * Calculates the data from the API to get the gas variables with the largest time difference. Returns a
     * map with all the required information.
     * @return map that contains name of the station, name of the gases, variable name of the gases and the time the
     * calculation started and ended
     */
    public Map<String, Map<String, Map<String, List<String>>>> getSmearTimeData(){
        getMapOfStationNameAndId();
        return mapOfStationNameGasesAndTimeStamp;
    }

    /**
     * This method can be used to get all the station names from SMEAR and get its station id.
     * Ex: {Kumpula=3, Värriö=1, Hyytiälä=2}.
     * It calls one other class stationNamesAndTheirTablesNames
     * It calls two other methods getSmearJsonArray and getMapOfStationIdBasedOnStationVariable
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
            ){
                mapOfStationNameAndStationId.put(stationName, urlObject.get("id").getAsInt());
            }
        }
        //call getMapOfStationIdBasedOnStationVariable method
        if (!mapOfStationNameAndStationId.isEmpty()) getTableVariableIdOfAStation(mapOfStationNameAndStationId);
    }

    /**
     *This method can be used to get a list that contains the variable table id of the given stations.
     * Ex: For Kumpula it gets a list of its variable tables [2, 8, 24].
     *It calls two other methods getSmearJsonArray and getTimeStamp and one class stationNamesAndTheirTablesNames
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
                if(stationNamesAndTheirTablesNames
                        .stationNameTableVariableName
                        .get(nameOfTheStation)
                        .contains(urlObject.get("name").getAsString())
                ){
                    tableIdList.add(urlObject.get("id").getAsInt());
                }
            }

            mapOfStationNameGasesAndTimeStamp.put(nameOfTheStation, getMapOfTimeStamp(url.toString(), tableIdList));
        }
    }

    /**
     * This method is used to find the gases whose data has been collected for the longest time. It returns
     * a Map containing the name of the gas, its variable id and its start and end time. It calls two other methods
     * getSmearJsonArray and gasVariableNameAndTime.
     * @param url_  url of the SMEAR website of variable tables of s station
     * @param tableIdList a list that contains the table ids of variable tables of a station
     * @return
     */
    private Map<String, Map<String, List<String>>> getMapOfTimeStamp(String url_, List<Integer> tableIdList) {
        //Map to store the name of the gas, its variable name and time stamps
        Map<String, Map<String, List<String>>> variableNameAndTimeStamp = new HashMap<>();
        //Map to store the longest days of the gas with time stamps
        TreeMap<Long, Map<String, List<String>>> co2TimeStamp = new TreeMap<>();
        TreeMap<Long, Map<String, List<String>>> so2TimeStamp = new TreeMap<>();
        TreeMap<Long, Map<String, List<String>>> noTimeStamp = new TreeMap<>();

        for (int tableId = 0; tableId < tableIdList.size(); tableId++) {
            //data from SMEAR
            StringBuilder url = new StringBuilder(url_)
                    .append("/")
                    .append(tableIdList.get(tableId))
                    .append("/variable");
            JsonArray urlArray = getSmearJsonArray(url.toString());

            //For CO2 time stamp
            //string to look for in the title of CO2
            String co2Title = "CO₂ concentration";
            co2TimeStamp.putAll(gasVariableNameAndTime(urlArray, co2Title));
            //For SO2 time stamp
            //string to look for in the title of SO2
            String so2Title = "SO₂ concentration";
            so2TimeStamp.putAll(gasVariableNameAndTime(urlArray, so2Title));
            //For NO time stamp
            //string to look for in the title of NO
            String noTitle = "NO concentration";
            noTimeStamp.putAll(gasVariableNameAndTime(urlArray, noTitle));
        }

        if (!co2TimeStamp.isEmpty()) variableNameAndTimeStamp.put("CO2", co2TimeStamp.get(co2TimeStamp.lastKey()));
        if (!so2TimeStamp.isEmpty()) variableNameAndTimeStamp.put("SO2", so2TimeStamp.get(so2TimeStamp.lastKey()));
        if (!noTimeStamp.isEmpty()) variableNameAndTimeStamp.put("NO", noTimeStamp.get(noTimeStamp.lastKey()));

        return variableNameAndTimeStamp;
    }

    /**
     * This method checks for CO₂ concentration is the given JsonArray. It calculates the time difference between the
     * start time and end time. Finally, it returns a map containing the time difference as key and start time and
     * end time as values.
     * @param jArray JsonArray fetched from the SMEAR API
     * @param giventitle_ string to match in the title of the variable data
     * @return map with time difference as key and a list containing start time and end time
     */
    private TreeMap<Long, Map<String, List<String>>> gasVariableNameAndTime(JsonArray jArray, String giventitle_) {
        //TreeMap to store time difference and its start and end times
        TreeMap<Long, Map<String, List<String>>> co2Variable = new TreeMap<>();
        long timeDifference = 0;

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject jObject = jArray.get(i).getAsJsonObject();

            if (jObject.get("title").isJsonNull() || jObject.get("periodStart").isJsonNull()) continue;

            String title = jObject.get("title").getAsString();
            if (title.contains(giventitle_)) {

                try {
                    StringBuilder startTime = new StringBuilder(jObject.get("periodStart").getAsString()).append("Z");
                    Instant startTimeConvert = Instant.parse(startTime);
                    Instant endTimeConvert = Instant.now();

                    if (!jObject.get("periodEnd").isJsonNull()) {
                        StringBuilder endTime = new StringBuilder(jObject.get("periodEnd").getAsString()).append("Z");
                        endTimeConvert = Instant.parse(endTime);
                    }

                    Duration res = Duration.between(startTimeConvert, endTimeConvert);
                    if (timeDifference <= res.toDays()) {
                        timeDifference = res.toDays();
                    } else continue;

                } catch (DateTimeParseException e) { }

                String periodEnd = !jObject.get("periodEnd").isJsonNull()
                        ? jObject.get("periodEnd").getAsString() : null;
                StringBuilder variableName = new StringBuilder(jObject.get("tableName").getAsString())
                        .append(".").append(jObject.get("name").getAsString());
                if (periodEnd == null) {
                    Instant nullToCurrentDate = Instant.now();
                    List<String> lst = Arrays.asList(jObject.get("periodStart").getAsString(),
                            nullToCurrentDate.toString());
                    Map<String, List<String>> co2 = Map.of(variableName.toString(), lst);
                    co2Variable.put(timeDifference, co2);
                    return co2Variable;
                } else {
                    List<String> lst = Arrays.asList(jObject.get("periodStart").getAsString(),
                            jObject.get("periodEnd").getAsString());
                    Map<String, List<String>> co2 = Map.of(variableName.toString(), lst);
                    co2Variable.put(timeDifference, co2);
                }
            }
        }
        return co2Variable;

    }

    /**
     * This method fetches the Json values using the given SMEAR API. It returns JsonArray of the given API.
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
        System.out.println(new smearTimeData().getSmearTimeData().toString());
    }

}
