package com.fhirtestclient.devices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fhirtestclient.helper.Console;
import com.fhirtestclient.helper.EndpointProvider;
import com.fhirtestclient.helper.HttpNode;
import com.fhirtestclient.helper.JacksonMapper;
import com.fhirtestclient.requestWrappers.ESP32DataReduced;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@NoArgsConstructor
@Getter @Setter
@ToString
public class ECGDevice {
    private String selfID;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String identifier;
    private String name;
    private int leads = 0;
    private String pin;

    @JsonIgnore
    private Console c = Console.getInstance();
    @JsonIgnore
    private JacksonMapper mapper = JacksonMapper.getInstance();
    @JsonIgnore
    private HttpNode httpNode = HttpNode.getInstance();

    private ArrayList<ECGDeviceComponent> components = new ArrayList<>();

    public ECGDevice(String selfID, String name, String pin) {
        this.selfID = selfID;
        this.name = name;
        this.pin = pin;
    }

    public void addComponent(ECGDeviceComponent c) {
        components.add(c);
        leads++;
    }

    public void registerECGDevice() {
        c.out(this, "Registering device at " + EndpointProvider.getRegisterECGDevice() + "...");

        String ident = httpNode.sendPostRequest(EndpointProvider.getRegisterECGDevice(), mapper.getJson(this));

        if(ident == null || ident.isBlank()) {
            c.err(this, "Did not receive an identifier from server, unable to proceed.");
        } else {
            JsonNode jsonNode = mapper.readTree(ident);
            identifier = jsonNode.get("identifier").asText();
            c.out(this, "Got identifier from server: " + identifier);
        }
    }

    public void sendCurrentData() {
        c.out(this, "Sending current component data to " + EndpointProvider.getReceiveJson_fromCustomEsp32() + "...");

        if(name.equals("ESP32 custom ecg device")) {
            ESP32DataReduced dat = new ESP32DataReduced(identifier, components.get(0).getData());
            httpNode.sendPostRequest(EndpointProvider.getReceiveJson_fromCustomEsp32(), mapper.getJson(dat));
        } else {
            httpNode.sendPostRequest(EndpointProvider.getReceiveJson_fromCustomEsp32(), mapper.getJson(this));
        }
    }

    @JsonIgnore
    public int getLeadCount() {
        return components.size();
    }

    public void loadDataToComponent(int componentIndex, String data) {
        components.get(componentIndex).setData(data);
    }
}
