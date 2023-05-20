package com.fhirtestclient;

public class UserUpdate extends User {
    private String oldName;
    private String oldPassword;

    public UserUpdate(String name, String address, Long phone, boolean emergency, String password, String oldName, String oldPassword) {
        super(name, address, phone, emergency, password);
        this.oldName = oldName;
        this.oldPassword = oldPassword;
    }

    @Override
    public String getJSON() {
        StringBuilder result = new StringBuilder();

        result.append("{\n");
        result.append("\t\"oldName\":\"").append(oldName).append("\",\n");
        result.append("\t\"oldPassword\":\"").append(oldPassword).append("\",\n");
        result.append("\t\"name\":\"").append(name).append("\",\n");
        result.append("\t\"address\":\"").append(address).append("\",\n");
        result.append("\t\"phone\":").append(phone).append(",\n");
        result.append("\t\"emergency\":").append(emergency).append(",\n");
        result.append("\t\"password\":\"").append(password).append("\"\n");
        result.append("}");

        return result.toString();
    }
}
