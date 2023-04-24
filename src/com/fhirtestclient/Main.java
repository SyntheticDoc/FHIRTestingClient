package com.fhirtestclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if(false) {
            //System.out.println(newString("2200 2170 2007 1915 1913 1938 1939 1935 1933 1933 1931 1932 1934 1936 1936 1935 1934 1936 1937 1940 1940 1942 1945 1947 1949 1952 1956 1962 1969 1977 1988 1997 2008 2019 2028 2034 2035 2025 2011 1993 1975 1961 1950 1944 1939 1937 1935 1935 1934 1935 1934 1934 1935 1935 1936 1936 1935 1936 1937 1939 1942 1941 1943 1943 1942 1942 1941 1941 1939 1938 1938 1938 1937 1936 1935 1933 1933 1938 1949 1953 1948 1952 1954 1952 1945 1944 1940 1936 1932 1929 1928 1926 1926 1926 1927 1925 1925 1926 1925 1917 1934 1992 2150 2189 2068 1924 1909 1933 1945 1940 1937 1937 1937 1934 1934 1935 1937 1938 1939 1938 1938 1940 2200 2138 2012 1935 1951 1985 1988 1997 2003 2008 2008 2009 2010 2012 2013 2014 2015 2019 2020 2025 2033 2037 2046 2056 2067 2081 2090 2096 2101 2103 2098 2086 2066 2044 2031 2013 2005 1995 1992 1988 1987 1987 1987 1983 1983 1984 1984 1985 1982 1982 1982 1985 1982 1982 1983 1983 1984 1983 1982 1980 1980 1980 1977 1975 1975 1977 1974 1975 1975 1974 1972 1972 1973 1973 1971 1972 1971 1972 1972 1969 1969 1968 1971 1972 1973 1972 1980 1979 1987 1982 1983 1975 1976 1975 1973 1974 1971 1966 1969 1970 1973 1973 1975 1977 1976 2004 2025 2055 2160 2195 2066 1972 1909 1913 1968 1980 1984 1994 1997 1999 2001 2004 2004 2003 2005 2007 2010 2012 2015 2019 2025 2172 2137 2026 1956 1902 1918 1923 1922 1923 1923 1926 1927 1928 1928 1931 1932 1934 1934 1935 1937 1939 1943 1945 1949 1952 1956 1961 1968 1975 1982 1990 1996 2000 2003 2004 2004 2000 1991 1982 1971 1961 1955 1948 1943 1940 1937 1935 1934 1933 1933 1933 1933 1932 1933 1933 1934 1935 1936 1936 1935 1936 1937 1937 1937 1937 1938 1936 1938 1937 1938 1937 1937 1937 1939 1938 1938 1937 1937 1936 1936 1936 1936 1937 1936 1937 1936 1937 1938 1939 1939 1940 1940 1940 1940 1940 1940 1940 1940 1942 1941 1942 1942 1943 1943 1943 1943 1943 1944 1944 1945 1944 1944 1944 1944 1945 1948 1956 1962 1964 1976 1977 1978 1978 1969 1965 1963 1950 1942 1938 1938 1937 1937 1938 1933 1940 1968 2077 2197 2200 2102 1999 1939 1923 1940 1950 1949 1950 1950 1951 1952 1953 1955 1955 1957 1958 1960 1962 1963 1965 1968 1971 1974 1976 1981 "));
            testEntityConversion();
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
                    sendSingleRequest(2);
                    break;
                case 2:
                    System.out.println("Sending asystole ecg data...");
                    sendSingleRequest(3);
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown option: " + userinput);
            }

            System.out.println();
        }

        System.out.println("Exiting program");
    }

    private static void sendSingleRequest(int testFile) {
        String serviceUrl_endpoint = "http://localhost:8080/data";
        //String serviceUrl_method = "/test";
        String serviceUrl_method = "/receive";
        String serviceUrl = serviceUrl_endpoint + serviceUrl_method;
        String body;

        if(testFile == 2) {
            body = TemplateProvider.getTemplate("jsontest2");
        } else if(testFile == 3) {
            body = TemplateProvider.getTemplate("jsontest3");
        } else {
            throw new IllegalArgumentException("Unknown testfile: " + testFile);
        }

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
}
