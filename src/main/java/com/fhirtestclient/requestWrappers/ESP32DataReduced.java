package com.fhirtestclient.requestWrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ESP32DataReduced {
    private String identifier;
    private String timestamp;
    private String data;
    private int sampling;
    private boolean leadsOff;

    @JsonIgnore
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss:SSS");

    public ESP32DataReduced(String identifier, String data) {
        this.identifier = identifier;
        this.data = data;
        this.sampling = 500;
        leadsOff = false;
        timestamp = LocalDateTime.now().toString();
    }
}
