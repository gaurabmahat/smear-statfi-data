package fi.tuni.csgr.SMEAR;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dataFromSmear implements IGetData{
    private String startTime;   //Date
    private String endTime;     //Date
    private String station;     //station name
    private String value;       //MIN, MAX, ARITHMETIC

    public dataFromSmear(String startTime_, String endTime_, String value_, String station_){
        startTime = startTime_;
        endTime = endTime_;
        value = value_;
        station = station_;
    }

    @Override
    public String getDataInJson() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://smear-backend.rahtiapp.fi/search/timeseries?aggregation=" + value +
                "&interval=60&from="+ startTime
                + "&to="+ endTime
                + station)).build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
    }

    //To test the class
    public static void main(String[] args){
        String startTime = "2022-01-20T01:00:00.000";
        String endTime = "2022-01-20T02:00:00.000";
        String value = "ARITHMETIC";

        //List of stations for CO2
        List<String> a = Arrays.asList(new String[]{"CO2", "Kumpula"});
        List<String> b = Arrays.asList(new String[]{"CO2", "Hyytiälä"});
        List<String> c = Arrays.asList(new String[]{"CO2", "Värriö"});

        List<List<String>> listStr = new ArrayList<>();
        listStr.add(a);
        listStr.add(b);
        listStr.add(c);
        //List of stations for CO2

        String station = new stationNames(listStr).getStation();

        String json = new dataFromSmear(startTime, endTime, value, station).getDataInJson();
        System.out.println(json);
    }
}
