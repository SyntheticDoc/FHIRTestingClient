package com.fhirtestclient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Observation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TemplateProvider {
    private static FhirContext ctx = FhirContext.forR5Cached();
    private static int checksum = 123456789;
    private static String deviceID = "b6555a1f924af0056f478cd9476213f0713617dc6e12494a88b12f5f707280b1";
    private static String[] componentIDs = new String[] {"3cfa4e0f7c6b5f2ceba2708a274f35b1b3107dd13c249bd3abc73defea6e8e2e",
                                                        "2034cb6376b62db4fa9c5b7d0ebb6c6608f1fd4496d51959946cead2052b48c3",
                                                        "5522557370588f56d92655cef590e8b630ba68b074167d99bbae2067422edc0a",
                                                        "bb204c866742e7484577da0f97ad5d3e910b24cd233b02ed39660f95129702d7",
                                                        "cb04b1688dd44e7bd11e4c95c9c59d289c71f88d117bfa8b6234bfcc67686d64"};

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss:SSS");

    public static String getTemplate(String type) {
        return getTemplate(type, null);
    }

    public static String getTemplate(String type, String data) {
        String result = "{}";

        switch(type) {
            case "testbody":
                result = "{TestData}";
                break;
            case "jsontest1":
                result = getFileContent("example_test1.json");
                break;
            case "jsontest2":
                result = getExampleJSON(1);
                break;
            case "jsontest3":
                result = getExampleJSON(2);
                break;
            case "fhirtest1":
                result = getCustomObservationAsString(1);
                break;
            case "fhirtest2":
                result = getCustomObservationAsString(2);
                break;
            case "ecgdeviceinfo1":
                result = getFileContent("ECGDeviceInfo1.json");
                break;
            case "connectdevicedata1":
                result = getFileContent("ConnectDeviceData.json");
                break;
            case "example_reduced":
                result = getFileContent("example_reduced.json");
                break;
            case "dataset_reduced":
                result = getReducedDataset(data);
                break;
        }

        return result;
    }

    private static String getReducedDataset(String data) {
        String[] temp = data.split(":");
        String deviceIdentifier = temp[0];
        String ecgData = temp[1].trim();
        StringBuilder result = new StringBuilder();
        LocalDateTime timestamp = LocalDateTime.now();

        if(ecgData.contains(",")) {
            temp = ecgData.split(",");
        } else {
            temp = ecgData.split(" ");
        }

        ecgData = "";

        for(int i = 0; i < temp.length; i++) {
            String val = String.valueOf(Double.parseDouble(temp[i]));

            if(val.equals("0.0")) {
                if(i < (temp.length - 1) && String.valueOf(Double.parseDouble(temp[i+1])).equals("0.0") || i == (temp.length - 1)) {
                    continue;
                } else {
                    ecgData += String.valueOf(Double.parseDouble(temp[i]));

                    if(i < (temp.length) - 1) {
                        ecgData += " ";
                    }
                }
            } else {
                ecgData += String.valueOf(Double.parseDouble(temp[i]));

                if(i < (temp.length) - 1) {
                    ecgData += " ";
                }
            }
        }

        ecgData = ecgData.trim();

        String filename = "resources/templates/example_reduced.json";

        try {
            Scanner sc = new Scanner(new File(filename));

            while(sc.hasNextLine()) {
                String s = sc.nextLine();

                if(!s.isBlank()) {
                    if(s.contains("someIdentifier")) {
                        s = s.replace("someIdentifier", deviceIdentifier);
                    }

                    if(s.contains("someTimestamp")) {
                        s = s.replace("someTimestamp", new DateTimeType(String.valueOf(timestamp)).getValueAsString());
                    }

                    if(s.contains("someData")) {
                        s = s.replace("someData", ecgData);
                    }

                    result.append(s).append("\n");
                }
            }

            return result.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getCustomObservationAsString(int example) {
        if(example == 2) {
            return getObservationFromFile("example_test2.json");
        } else {
            return getObservationFromFile("example_test1.json");
        }
    }

    private static String getObservationFromFile(String filename) {
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);

        CustomObservation obs = parser.parseResource(CustomObservation.class, getFileContent(filename));

        obs.setChecksumFromInt(checksum);
        obs.setTimestampFromLocalDateTime(LocalDateTime.now());
        obs.setDeviceIDfromString(deviceID);

        for(int i = 0; i < obs.getComponent().size(); i++) {
            Observation.ObservationComponentComponent c = obs.getComponent().get(i);
            c.getCode().getCoding().get(0).setCode(componentIDs[i]);
        }

        return parser.encodeResourceToString(obs);
    }

    private static String getExampleJSON(int example) {
        String result = "{}";
        LocalDateTime timestamp = LocalDateTime.now();
        String firstLine = "{\"checksum\":\"0123456789\", \"timestamp\":\"" + timestamp.format(dtf) + "\"}";
        // System.out.println("Firstline: " + firstLine);

        if(example == 2) {
            result = firstLine + "\n" + getFileContent("example_test2.json");
        } else {
            result = firstLine + "\n" + getFileContent("example_test1.json");
        }

        return result;
    }

    private static String getFileContent(String file) {
        String filename = "resources/templates/" + file;
        StringBuilder result = new StringBuilder();

        try {
            Scanner sc = new Scanner(new File(filename));

            while(sc.hasNextLine()) {
                result.append(sc.nextLine()).append("\n");
            }

            return result.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void saveToFile(String fileName, String data) {
        String path = "resources/templates/" + fileName;
        StringBuilder result = new StringBuilder();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(data);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
