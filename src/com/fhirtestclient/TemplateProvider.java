package com.fhirtestclient;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TemplateProvider {
    public static String getTemplate(String type) {
        return getTemplate(type, null);
    }

    public static String getTemplate(String type, String data) {
        String result = "{}";

        switch(type) {
            case "testbody":
                result = "{TestData}";
            case "jsontest1":
                result = getFileContent("example_test1.json");
            case "jsontest2":
                LocalDateTime timestamp = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss:SSS");
                String firstLine = "{\"checksum\":\"0123456789\", \"timestamp\":\"" + timestamp.format(dtf) + "\"}";
                System.out.println("Firstline: " + firstLine);
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
}
