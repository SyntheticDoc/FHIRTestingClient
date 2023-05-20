package com.fhirtestclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //prepFile();
        //mode2();
        //mode3();

        String[] messages1 = new String[] {
                "registerECGDevice",
                "dataset_reduced_1234",
                "dataset_reduced_1234"
        };

        String[] messages2 = new String[] {
                "registerUser"
        };

        String[] messages3 = new String[] {
                "updateUser"
        };

        String[] messages4 = new String[] {
                "getUser"
        };

        User uReg = new User("TestUser1", "TestAddress1", 123L, true, "TestPWD");
        UserUpdate uUpd = new UserUpdate("TestUser2", "TestAddress3", 124L, false, "TestPWD2", uReg.name, uReg.password);

        //multiMessage(1000, messages4, uUpd);
        multiMessage(1000, new String[]{"registerECGDevice", "IliDat1"}, null);

        System.out.println("Exiting program");
    }

    private static void multiMessage(int waitMilliSecondsBetweenRequests, String[] messages, User u) {
        String deviceInfo = "Could not get deviceInfo from backend request";

        for(int i = 0; i < messages.length; i++) {
            if (messages[i].equals("registerECGDevice")) {
                deviceInfo = extractDeviceIdentifier(singleMessage(messages[i], "", null));
                System.out.println("   Received device info: " + deviceInfo);
            } else if (messages[i].contains("dataset")) {
                singleMessage(messages[i], deviceInfo, null);
            } else if(messages[i].contains("User")) {
                singleMessage(messages[i], "", u);
            }  else if(messages[i].contains("IliDat1")) {
                DataProvider dataProvider = new DataProvider();
                singleMessage(messages[i], deviceInfo + ":" + dataProvider.getIliData(2), null);
            } else {
                singleMessage(messages[i], "", null);
            }

            if(i < (messages.length - 1)) {
                wait(waitMilliSecondsBetweenRequests);
            }
        }
    }

    private static String singleMessage(String message, String optionalInfo, User u) {
        String messageTarget = "http://localhost:8080";
        String body = "No body generated";

        switch(message) {
            case "registerECGDevice":
                messageTarget += "/connect/registerECGDevice";
                body = TemplateProvider.getTemplate("ecgdeviceinfo1");
                break;
            case "dataset_reduced_patientData_id_1_1":
                messageTarget += "/data/receive/esp32_custom";

                try(Scanner sc = new Scanner(new File("resources/datasets/id_1_1.txt"))) {
                    StringBuilder data = new StringBuilder();

                    while (sc.hasNextLine()) {
                        data.append(sc.nextLine());
                    }

                    body = TemplateProvider.getTemplate("dataset_reduced", optionalInfo + ":" + data);
                } catch(IOException e) {
                    e.printStackTrace();
                }
                break;
            case "dataset_reduced_1234":
                messageTarget += "/data/receive/esp32_custom";
                body = TemplateProvider.getTemplate("dataset_reduced", optionalInfo + ":1 2 3 4");
                break;
            case "connectECGDeviceToUser":
                messageTarget += "/connect/connectECGDeviceToUser";
                body = TemplateProvider.getTemplate("connectdevicedata1");
                break;
            case "registerUser":
                messageTarget += "/user/post-user";
                body = u.getJSON();
                break;
            case "updateUser":
                messageTarget += "/user/update-user";
                body = u.getJSON();
                break;
            case "getUser":
                messageTarget += "/user/get-user?name=" + u.name + "&password=" + u.password;
                body = "";
                break;
            case "IliDat1":
                messageTarget += "/data/receive/esp32_custom";
                body = TemplateProvider.getTemplate("dataset_reduced", optionalInfo);
                break;
            default:
                throw new IllegalArgumentException("Unknown message type " + message);
        }

        String loggingMessage = "Send message type \"" + message + "\" to target " + messageTarget;

        if(optionalInfo != null && !optionalInfo.isBlank()) {
            loggingMessage += " (Optional info: " + optionalInfo + ")";
        }

        System.out.println(loggingMessage);

        if(!message.equals("getUser")) {
            return sendCustomSingleRequest(messageTarget, body);
        } else {
            return sendCustomSingleRequest(messageTarget, body, "get");
        }
    }

    private static void mode3() {
        int waitMillisecondsBetweenRequests = 1000;
        int numberOfMessages = 2;
        int counter = 0;
        DataProvider dataProvider = new DataProvider();
        String deviceIdentifier;

        // Register ecg device
        String requestResult = sendCustomSingleRequest("http://localhost:8080/connect/registerECGDevice", TemplateProvider.getTemplate("ecgdeviceinfo1"));
        deviceIdentifier = requestResult.replace("{", "").replace("}", "").replace(" ", "")
                .replace("\"", "").split(":")[1];

        while(counter < numberOfMessages) {
            counter++;
            wait(waitMillisecondsBetweenRequests);

            String data = "";

            try {
                Scanner sc = new Scanner(new File("resources/datasets/id_1_1.txt"));

                while(sc.hasNextLine()) {
                    data = sc.nextLine();
                }

                data = "1 2 3 4";

                String json = TemplateProvider.getTemplate("dataset_reduced", deviceIdentifier + ":" + data);
                System.out.println("json:\n" + json);
                sendCustomSingleRequest("http://localhost:8080/data/receive/esp32_custom", json);
            } catch(Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private static void mode2() {
        int waitMillisecondsBetweenRequests = 1000;
        int numberOfMessages = 1;
        int counter = 0;
        DataProvider dataProvider = new DataProvider();
        String deviceIdentifier;

        // Register ecg device
        String requestResult = sendCustomSingleRequest("http://localhost:8080/connect/registerECGDevice", TemplateProvider.getTemplate("ecgdeviceinfo1"));
        deviceIdentifier = requestResult.replace("{", "").replace("}", "").replace(" ", "")
                .replace("\"", "").split(":")[1];

        while(counter < numberOfMessages) {
            counter++;
            wait(waitMillisecondsBetweenRequests);
            String data = dataProvider.getRandomDataset();
            String json = TemplateProvider.getTemplate("dataset_reduced", deviceIdentifier + ":" + data);
            sendCustomSingleRequest("http://localhost:8080/data/receive/esp32_custom", json);
        }
    }

    private static void mode1() {
        if(true) {
            DataProvider dataProvider = new DataProvider();
            String json = TemplateProvider.getTemplate("dataset_reduced",
                    "d6864a0e35be5c109190b96f5d4b395fb083af8dbc8f1b4e9bb01cb5f26eacd7:" + dataProvider.getRandomDataset());

            System.out.print("JSON:\n" + json);

            //sendCustomSingleRequest("http://localhost:8080/connect/registerECGDevice", TemplateProvider.getTemplate("ecgdeviceinfo1"));
            //sendCustomSingleRequest("http://localhost:8080/data/receive/esp32_custom", TemplateProvider.getTemplate("example_reduced"));
            sendCustomSingleRequest("http://localhost:8080/data/receive/esp32_custom", json);
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
            String serverResponse = "Server response: " + response.statusCode();

            if(!response.body().isBlank()) {
                serverResponse = serverResponse + ": " + response.body();
            }

            System.out.println(serverResponse);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static String sendCustomSingleRequest(String serviceUrl, String body) {
        return sendCustomSingleRequest(serviceUrl, body, "post");
    }

    private static String sendCustomSingleRequest(String serviceUrl, String body, String type) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request;

        if(type.equalsIgnoreCase("post")) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(serviceUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .build();
        } else if(type.equalsIgnoreCase("get")) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(serviceUrl))
                    .GET()
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            throw new IllegalArgumentException("Unknown request type: " + type);
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Server response: " + response.statusCode() + ":\n" + response.body());
            return response.body();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
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

    private static void prepFile() {
        String filename = "resources/datasets/id_1.txt";
        ArrayList<String> data = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(filename));

            while(sc.hasNextLine()) {
                String line = sc.nextLine();

                if(line.startsWith("'0")) {
                    data.add(line.split(",")[1]);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

        StringBuilder result = new StringBuilder();

        for(int i = 0; i < data.size(); i++) {
            result.append(data.get(i));

            if(i < (data.size() - 1)) {
                result.append(",");
            }
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("resources/datasets/id_1_1.txt"))) {
            writer.write(result.toString());
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private static String extractDeviceIdentifier(String msg) {
        return msg.replace("{", "").replace("}", "").replace(" ", "")
                .replace("\"", "").split(":")[1];
    }
}
