package fi.tuni.csgr.network;

import fi.tuni.csgr.converters.json.ResultList;
import fi.tuni.csgr.query.Query;
import fi.tuni.csgr.utils.HttpRequestClient;

import java.net.http.HttpRequest;

public class QueryClient {
    public void performQuery(Query query) {
        HttpRequest request = query.getHttpRequest();
        String json = HttpRequestClient.request(request);
        ResultList results = query.JsonToResult(json);
        query.updateGraphs(results);
    }
}
