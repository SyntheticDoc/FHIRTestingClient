package com.fhirtestclient.requestWrappers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RequestDeviceAccess {
    private String userName;
    private String password;
    private String deviceIdentifier;
}
