package fi.tuni.csgr.SMEAR.fetchSeriesDataFromSmear;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class dataFromSmear implements IGetData {
    private String startTime;   //Date
    private String endTime;     //Date
    private String value;       //MIN, MAX, ARITHMETIC
    private List<String> gases; //Name of the Gases
    private List<String> stations; //Name of the Stations

    public dataFromSmear(String startTime_, String endTime_, String value_, List<String> gases_, List<String> stations_){
        this.startTime = startTime_;
        this.endTime = endTime_;
        this.value = value_;
        this.gases = gases_;
        this.stations = stations_;
    }

    @Override
    public String getDataInStringJson() {
        //Get table variables from the class getTableVariable
        String tableVariables = new getTableVariable(gases, stations).getStationsCode();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://smear-backend.rahtiapp.fi/search/timeseries?aggregation=" + value +
                "&interval=60&from="+ startTime
                + "&to="+ endTime
                + tableVariables)).build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
    }

    //To test the class
    public static void main(String[] args){
        /*String startTime = "2022-01-20T01:00:00.000";
        String endTime = "2022-01-20T02:00:00.000";
        String value = "MAX";

        //List of gases and stations
        List<String> gases = Arrays.asList(new String[]{"CO2", "NO", "SO2"});
        List<String> stations = Arrays.asList(new String[]{"Kumpula", "Hyytiälä", "Värriö"});

        System.out.println(new dataFromSmear(startTime, endTime, value, gases, stations).getDataInStringJson());*/
    }
}
