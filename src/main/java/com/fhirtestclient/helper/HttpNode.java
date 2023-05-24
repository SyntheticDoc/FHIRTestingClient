package com.fhirtestclient.helper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpNode {
    private static HttpNode instance;
    private boolean showServerResponse = true;
    private boolean showServerResponseBody = true;
    private Console c = Console.getInstance();

    private HttpNode() {

    }

    public static HttpNode getInstance() {
        if(instance == null) {
            instance = new HttpNode();
        }

        return instance;
    }

    public String sendPostRequest(String endpoint, String body) {
        HttpRequest request;

        request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();

        return sendRequest(request);
    }

    public String sendGetRequest(String endpoint) {
        HttpRequest request;

        request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        return sendRequest(request);
    }

    private String sendRequest(HttpRequest request) {
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseString = "Server response: " + response.statusCode();

            if(showServerResponseBody) {
                responseString += ": " + response.body();
            }

            if(showServerResponse) {
                c.out(this, responseString);
            }

            return response.body();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setShowServerResponse(boolean value) {
        showServerResponse = value;
    }

    public void setShowServerResponseBody(boolean value) {
        showServerResponseBody = value;
    }
}
