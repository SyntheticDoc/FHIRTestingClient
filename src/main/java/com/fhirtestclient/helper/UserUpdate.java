package com.fhirtestclient.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

@ToString
public class UserUpdate extends User {
    public String oldName;
    public String oldPassword;

    public UserUpdate(String name, String address, Long phone, boolean emergency, String password, String oldName, String oldPassword) {
        super(name, address, phone, emergency, password);
        this.oldName = oldName;
        this.oldPassword = oldPassword;
    }

    public UserUpdate(User u, String oldName, String oldPassword) {
        super(u.name, u.address, u.phone, u.emergency, u.password);
        this.oldName = oldName;
        this.oldPassword = oldPassword;
    }

    @JsonIgnore
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
