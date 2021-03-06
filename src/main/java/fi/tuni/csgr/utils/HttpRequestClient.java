package fi.tuni.csgr.utils;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility class to perform Http requests.
 */

public class HttpRequestClient {

    /**
     * Performs a http request and returns the response.
     *
     * @param request
     * @return the response as string
     */
    public static String request(HttpRequest request) {
        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
    }
}
