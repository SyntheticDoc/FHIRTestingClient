package com.fhirtestclient;

public class User {
    public String name;
    public String address;
    public Long phone;
    public boolean emergency;
    public String password;

    public User(String name, String address, Long phone, boolean emergency, String password) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.emergency = emergency;
        this.password = password;
    }

    public User() {}

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
