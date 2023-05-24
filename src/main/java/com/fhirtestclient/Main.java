package com.fhirtestclient;

import com.fhirtestclient.devices.ECGDevice;
import com.fhirtestclient.devices.ECGDeviceComponent;
import com.fhirtestclient.devices.FrontendDevice;
import com.fhirtestclient.helper.Console;
import com.fhirtestclient.helper.HttpNode;
import com.fhirtestclient.helper.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Main {
    private static Console c = Console.getInstance();
    private static HttpNode httpNode = HttpNode.getInstance();

    public static void main(String[] args) {
        httpNode.setShowServerResponse(true);
        httpNode.setShowServerResponseBody(true);

        FrontendDevice frontendDevice = new FrontendDevice("smartphoneDevice01", "Generic android smartphone");
        User user = new User("Test User2", "Teststreet 2", 12345678L, false, "superSecurePwd2");
        frontendDevice.setFrontendUser(user);

        frontendDevice.registerUser();
        frontendDevice.getUser();

        User userESP32 = new User("User Userman3", "ESPStraße 32/Stiege 32/Top 32, 1404 Wien", 3223L, true, "pwd3");
        frontendDevice.setFrontendUser(userESP32);

        ECGDevice ecgDevice = new ECGDevice("device01", "ESP32 custom ecg device", "1a2b:3c4d:5e6f");
        ecgDevice.addComponent(new ECGDeviceComponent("component01", "MDC_ECG_ELEC_POTL_I"));

        ecgDevice.registerECGDevice();

        frontendDevice.connectECGDeviceToUser(ecgDevice);

        ecgDevice.loadDataToComponent(0, "1.0 2.0 3.0 4.0");

        ecgDevice.sendCurrentData();

        wait(1000);

        frontendDevice.getLastHealthStatus(0);
    }

    private static void wait(int milliseconds) {
        Instant start = Instant.now();
        Instant now = Instant.now();

        System.out.println("Waiting " + milliseconds + "ms...");

        while(ChronoUnit.MILLIS.between(start, now) < milliseconds) {
            now = Instant.now();
        }
    }
}
