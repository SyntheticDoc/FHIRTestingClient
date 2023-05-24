package com.fhirtestclient.devices;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ECGDeviceComponent {
    private String selfID;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String identifier;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String data;

    public ECGDeviceComponent(String selfID, String name) {
        this.selfID = selfID;
        this.name = name;
    }
}
