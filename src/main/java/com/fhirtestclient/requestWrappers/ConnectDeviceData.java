package com.fhirtestclient.requestWrappers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
public class ConnectDeviceData {
    private String userName;
    private String password;
    private String regDeviceName;
    private String regDevicePin;
}
