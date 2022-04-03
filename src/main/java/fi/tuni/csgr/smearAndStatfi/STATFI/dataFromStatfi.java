package fi.tuni.csgr.smearAndStatfi.STATFI;

import fi.tuni.csgr.smearAndStatfi.IGetData;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Retrieve data from STATFI
 */
public class dataFromStatfi implements IGetData {
    private List<String> tiedot;
    private List<String> vuosi;

    /**
     * Takes a list of Tiedot and a list of Vuosi values to retrieve data from STATFI
     * @param tiedot_ list of Tiedot values
     * @param vuosi_ list of Vuosi values
     */
    public dataFromStatfi(List<String> tiedot_, List<String> vuosi_){
        if(tiedot_.isEmpty()){
            throw new IllegalArgumentException("Tiedot list cannot be empty.");
        }
        if(vuosi_.isEmpty()){
            throw new IllegalArgumentException("Vuosi list cannot be empty.");
        }
        this.tiedot = tiedot_;
        this.vuosi = vuosi_;
    }

    /**
     * This method returns the response retrieved from responseFromStatfi. It also converts the lists of Tiedot
     * values and Vuosi values into a string and passes it to an inner method responseFromStatfi.
     * @return returns json data from STATFI as a string
     * @throws URISyntaxException checks if the uri string cannot be parsed as URI reference
     */
    @Override
    public String getDataInStringJson() throws URISyntaxException {
        //Convert List to string
        String tiedotValues = "";
        String vuosiValues = "";
        //For Tiedot values
        for(int i = 0; i < this.tiedot.size()-1; i++){
            tiedotValues += "\"" + this.tiedot.get(i) + "\", ";
        }
        tiedotValues += "\"" + this.tiedot.get(this.tiedot.size()-1) + "\"";
        //For Vuosi values
        for(int i = 0; i < this.vuosi.size()-1; i++){
            vuosiValues += "\"" + this.vuosi.get(i) + "\", ";
        }
        vuosiValues += "\"" + this.vuosi.get(this.vuosi.size()-1) + "\"";

        return responseFromStatfi(tiedotValues, vuosiValues);
    }

    /**
     * This method calls the STATFI API using POST request with the required string.
     * @param tiedot_ string containing the Tiedot values
     * @param vuosi_ string containing Vuosi values
     * @return returns json data from STATFI as a string
     * @throws URISyntaxException checks if the uri string cannot be parsed as URI reference
     */
    private String responseFromStatfi(String tiedot_, String vuosi_) throws URISyntaxException {

        URI uri = new URI("https://pxnet2.stat.fi/PXWeb/api/v1/en/ymp/taulukot/Kokodata.px");
        var sendJson = "{\n" +
                "        \"query\": [\n" +
                "            {\n" +
                "                \"code\": \"Tiedot\",\n" +
                "                \"selection\": {\n" +
                "                    \"filter\": \"item\",\n" +
                "                    \"values\": ["+ tiedot_ + "]\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"code\": \"Vuosi\",\n" +
                "                \"selection\": {\n" +
                "                    \"filter\": \"item\",\n" +
                "                    \"values\": [" + vuosi_ + "]\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .POST(HttpRequest.BodyPublishers.ofString(sendJson))
                .header("Content-type", "application/json")
                .build();

        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        return response;
    }

    public static void main(String[] args) throws URISyntaxException {
        /*List<String> ls = Arrays.asList("Khk_yht_index", "Khk_yht_las_index");
        List<String> date = Arrays.asList("2010", "2011");

        String data = new dataFromStatfi(ls, date).getDataInStringJson();
        System.out.println(data);*/
    }
}
