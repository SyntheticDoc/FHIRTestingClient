package com.fhirtestclient.helper;

import com.fhirtestclient.devices.ECGDevice;
import com.fhirtestclient.devices.ECGDeviceComponent;

import java.util.ArrayList;
import java.util.List;

public class BackendDataContainer {
    public ECGDevice user1Device, user2Device;
    public User userman1, userman2, userman3;
    
    public BackendDataContainer() {
        ECGDeviceComponent c1 = new ECGDeviceComponent(), c2 = new ECGDeviceComponent(), c3 = new ECGDeviceComponent();
        c1.setSelfID("c1");
        c1.setName("Lead c1");
        c1.setIdentifier("Lead c1");
        c2.setSelfID("c2");
        c2.setName("Lead c2");
        c2.setIdentifier("Lead c2");
        c3.setSelfID("c3");
        c3.setName("Lead c3");
        c3.setIdentifier("Lead c3");

        ArrayList<ECGDeviceComponent> d1Comps = new ArrayList<>();
        ArrayList<ECGDeviceComponent> d2Comps = new ArrayList<>();

        d1Comps.add(c1);
        d2Comps.add(c2);
        d2Comps.add(c3);

        user1Device = new ECGDevice();
        user1Device.setSelfID("user1DeviceSelfID");
        user1Device.setIdentifier("user1DeviceSelfID");
        user1Device.setName("user1Device");
        user1Device.setLeads(1);
        user1Device.setComponents(d1Comps);
        user1Device.setPin("123");
        user2Device = new ECGDevice();
        user2Device.setSelfID("user2DeviceSelfID");
        user2Device.setIdentifier("user2DeviceSelfID");
        user2Device.setName("user2Device");
        user2Device.setLeads(2);
        user2Device.setComponents(d2Comps);
        user2Device.setPin("123");

        List<ECGDevice> user1Devices = new ArrayList<>();
        List<ECGDevice> user2Devices = new ArrayList<>();
        user1Devices.add(user1Device);
        user2Devices.add(user2Device);

        userman1 = new User();
        userman1.name = "User Userman1";
        userman1.address = "Userstraße 42/Stiege -5/Top 0, 1404 Wien";
        userman1.password = "pwd";
        userman1.emergency = true;
        userman1.phone = 1234L;
        userman1.devices = user1Devices;

        userman2 = new User();
        userman2.name = "User Userman2";
        userman2.address = "Nutzerstraße 23/Stiege 2001/Top inf, 1404 Wien";
        userman2.password = "pwd2";
        userman2.emergency = true;
        userman2.phone = 6789L;
        userman2.devices = user2Devices;

        userman3 = new User();
        userman3.name = "User Userman3";
        userman3.address = "ESPStraße 32/Stiege 32/Top 32, 1404 Wien";
        userman3.password = "pwd3";
        userman3.emergency = true;
        userman3.phone = 3223L;
    }
}
