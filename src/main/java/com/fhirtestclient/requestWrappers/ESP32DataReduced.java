package com.fhirtestclient.requestWrappers;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ESP32DataReduced {
    private String identifier;
    private String timestamp;
    private String data;
    private boolean leadsOff;

    public ESP32DataReduced(String identifier, String data) {
        this.identifier = identifier;
        this.data = data;
        leadsOff = false;
        timestamp = LocalDateTime.now().toString();
    }
}
