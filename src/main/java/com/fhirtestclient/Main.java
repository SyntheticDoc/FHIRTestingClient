package com.fhirtestclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        mode2();

        System.out.println("Exiting program");
    }

    private static void mode2() {
        wait(1000);
    }

    private static void mode1() {
        if(true) {
            //sendCustomSingleRequest("http://localhost:8080/connect/registerECGDevice", TemplateProvider.getTemplate("ecgdeviceinfo1"));
            sendCustomSingleRequest("http://localhost:8080/data/receive/esp32_custom", TemplateProvider.getTemplate("example_reduced"));
            //sendCustomSingleRequest("http://localhost:8080/connect/connectECGDeviceToUser", TemplateProvider.getTemplate("connectdevicedata1"));
            //TemplateProvider.saveToFile("CustomObservationTemplate.json", TemplateProvider.getTemplate("fhirtest2"));
            return;
        }

        boolean exit = false;
        int userinput;
        Scanner sc = new Scanner(System.in);

        while(!exit) {
            System.out.println("Choose:");
            System.out.println("\t1. Send normal ecg");
            System.out.println("\t2. Send asystole ecg");
            System.out.println("");
            System.out.println("\t0. End program");
            System.out.println();
            System.out.print(":> ");

            userinput = Integer.parseInt(sc.nextLine());

            switch(userinput) {
                case 1:
                    System.out.println("Sending normal ecg data...");
                    sendSingleRequest(1);
                    break;
                case 2:
                    System.out.println("Sending asystole ecg data...");
                    sendSingleRequest(2);
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown option: " + userinput);
            }

            System.out.println();
        }
    }

    private static void sendSingleRequest(int testFile) {
        String serviceUrl_endpoint = "http://localhost:8080/data";
        //String serviceUrl_method = "/test";
        String serviceUrl_method = "/receive";
        String serviceUrl = serviceUrl_endpoint + serviceUrl_method;
        String body;

        if(testFile == 1) {
            body = TemplateProvider.getTemplate("fhirtest1");
        } else if(testFile == 2) {
            body = TemplateProvider.getTemplate("fhirtest2");
        } else {
            throw new IllegalArgumentException("Unknown testfile: " + testFile);
        }

        //body = "Content-Type: application/json\n\n" + body;

        if(false) {
            System.out.println("\n\n" + body);
            System.exit(0);
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Server response: " + response.statusCode() + " " + response.body());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendCustomSingleRequest(String serviceUrl, String body) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Server response: " + response.statusCode() + ":\n" + response.body());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void testEntityConversion() {
        String serviceUrl_endpoint = "http://localhost:8080/data";
        String serviceUrl_method = "/entityTest";
        String serviceUrl_method2 = "/receive";
        String serviceUrl = serviceUrl_endpoint + serviceUrl_method;
        String serviceUrl2 = serviceUrl_endpoint + serviceUrl_method2;
        String body = TemplateProvider.getTemplate("jsontest2");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        String json = "";

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            json = "{\"checksum\":\"0123456789\", " + response.body().substring(1);
        } catch(Exception e) {
            e.printStackTrace();
        }

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl2))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
            System.out.println("Server response: " + response.statusCode() + " " + response.body());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static String newString(String s) {
        String[] parts = s.split(" ");
        StringBuilder result = new StringBuilder();

        for(String str : parts) {
            if(!str.isBlank()) {
                result.append("0 ");
            }
        }

        return result.toString();
    }

    private static void wait(int milliseconds) {
        Instant start = Instant.now();
        Instant now = Instant.now();

        System.out.println("Waiting " + milliseconds + "ms...");

        while(ChronoUnit.MILLIS.between(start, now) < milliseconds) {
            now = Instant.now();
        }
    }
}
