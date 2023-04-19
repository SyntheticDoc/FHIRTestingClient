package com.fhirtestclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) {
        String serviceUrl_endpoint = "http://localhost:8080/data";
        //String serviceUrl_method = "/test";
        String serviceUrl_method = "/receive";
        String serviceUrl = serviceUrl_endpoint + serviceUrl_method;
        String body = TemplateProvider.getTemplate("jsontest2");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Server response: " + response.statusCode() + " " + response.body());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
