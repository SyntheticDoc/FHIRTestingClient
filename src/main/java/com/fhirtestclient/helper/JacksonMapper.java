package com.fhirtestclient.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fhirtestclient.devices.ECGDevice;

public class JacksonMapper {
    private final ObjectMapper mapper = new ObjectMapper();
    private static JacksonMapper instance;

    private JacksonMapper() {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static JacksonMapper getInstance() {
        if(instance == null) {
            instance = new JacksonMapper();
        }

        return instance;
    }

    public String getJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode readTree(String json) {
        try {
            return mapper.readTree(json);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object readToObject(String json, Class targetClass) {
        try {
            return mapper.readValue(json, targetClass);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
