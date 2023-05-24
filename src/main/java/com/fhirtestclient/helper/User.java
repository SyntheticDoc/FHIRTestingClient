package com.fhirtestclient.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fhirtestclient.devices.ECGDevice;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class User {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Long id;
    public String name;
    public String address;
    public Long phone;
    public boolean emergency;
    public String password;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<ECGDevice> devices = new ArrayList<>();

    public User(String name, String address, Long phone, boolean emergency, String password) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.emergency = emergency;
        this.password = password;
    }

    public User() {}

    @JsonIgnore
    public String getJSON() {
        StringBuilder result = new StringBuilder();

        result.append("{\n");
        result.append("\t\"name\":\"").append(name).append("\",\n");
        result.append("\t\"address\":\"").append(address).append("\",\n");
        result.append("\t\"phone\":").append(phone).append(",\n");
        result.append("\t\"emergency\":").append(emergency).append(",\n");
        result.append("\t\"password\":\"").append(password).append("\"\n");
        result.append("}");

        return result.toString();
    }

    @JsonIgnore
    public String getGetParams() {
        StringBuilder result = new StringBuilder();

        result.append("?");
        result.append("name=").append(name);
        result.append("&address=").append(address);
        result.append("&phone=").append(phone);
        result.append("&emergency=").append(emergency);
        result.append("&password=").append(password);

        return result.toString();
    }
}
