package com.fhirtestclient;

import com.fhirtestclient.devices.ECGDevice;
import com.fhirtestclient.devices.ECGDeviceComponent;
import com.fhirtestclient.devices.FrontendDevice;
import com.fhirtestclient.helper.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Main {
    private static Console c = Console.getInstance();
    private static HttpNode httpNode = HttpNode.getInstance();

    public static void main(String[] args) {
        httpNode.setShowServerResponse(true);
        httpNode.setShowServerResponseBody(true);

        // test2();
        test1();
        wait(2000);
        test3();
    }

    public static void test3() {
        ECGDevice ecgDevice = new ECGDevice("device01", "ESP32 custom ecg device", "1a2b:3c4d:5e6f");
        ecgDevice.addComponent(new ECGDeviceComponent("component01", "MDC_ECG_ELEC_POTL_I"));

        ecgDevice.registerECGDevice();

        ecgDevice.loadDataToComponent(0, "0.0 0.0 0.0 0.0");

        ecgDevice.sendCurrentData();

        wait(1000);

        ecgDevice.sendCurrentData();

        wait(1000);

        ecgDevice.sendCurrentData();

        wait(1000);
    }

    public static void test2() {
        BackendDataContainer dataContainer = new BackendDataContainer();
        FrontendDevice frontendDevice = new FrontendDevice("smartphoneDevice01", "Generic android smartphone");
        frontendDevice.setFrontendUser(dataContainer.userman2);

        for(int i = 0; i < 30; i++) {
            frontendDevice.getLastHealthStatus(0);
            wait(500);
        }
    }

    public static void test1() {
        BackendDataContainer dataContainer = new BackendDataContainer();
        FrontendDevice frontendDevice = new FrontendDevice("smartphoneDevice01", "Generic android smartphone");
        User user = new User("Test User2", "Teststreet 2", 12345678L, false, "superSecurePwd2");
        frontendDevice.setFrontendUser(user);

        frontendDevice.registerUser();
        frontendDevice.getUser();

        User user2 = new User("Test User2", "Teststreet 2", 12345678L, false, "superSecurePwd3");
        User userUpdate = new UserUpdate(user2, user.name, user.password);
        frontendDevice.setFrontendUserUpdate(userUpdate);
        frontendDevice.updateUser();

        User userESP32 = dataContainer.userman3;
        frontendDevice.setFrontendUser(userESP32);

        ECGDevice ecgDevice = new ECGDevice("device01", "ESP32 custom ecg device", "1a2b:3c4d:5e6f");
        ecgDevice.addComponent(new ECGDeviceComponent("component01", "MDC_ECG_ELEC_POTL_I"));

        ecgDevice.registerECGDevice();

        frontendDevice.connectECGDeviceToUser(ecgDevice);

        ecgDevice.loadDataToComponent(0, "0.0 0.0 0.0 0.0");

        ecgDevice.sendCurrentData();

        wait(1000);

        ecgDevice.sendCurrentData();

        wait(1000);

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
