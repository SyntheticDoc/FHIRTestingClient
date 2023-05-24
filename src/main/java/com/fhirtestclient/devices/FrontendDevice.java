package com.fhirtestclient.devices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fhirtestclient.helper.*;
import com.fhirtestclient.requestWrappers.ConnectDeviceData;
import com.fhirtestclient.requestWrappers.RequestDatasets;
import com.fhirtestclient.requestWrappers.RequestDeviceAccess;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("DuplicatedCode")
@NoArgsConstructor
@Getter @Setter
@ToString
public class FrontendDevice {
    private String selfID;
    private String identifier;
    private String name;

    private User frontendUser;
    private UserUpdate frontendUserUpdate;
    private ECGDevice device;

    private String lastHealthStatus;

    private boolean debug = false;

    @JsonIgnore
    private Console c = Console.getInstance();
    @JsonIgnore
    private JacksonMapper mapper = JacksonMapper.getInstance();
    @JsonIgnore
    private HttpNode httpNode = HttpNode.getInstance();

    public FrontendDevice(String selfID, String name) {
        this.selfID = selfID;
        this.name = name;
    }

    public void registerUser() {
        c.out(this, "Registering user at " + EndpointProvider.getPostUser() + "...");

        String userJson = httpNode.sendPostRequest(EndpointProvider.getPostUser(), mapper.getJson(frontendUser));

        if (userJson == null || userJson.isBlank()) {
            c.err(this, "Did not receive an user from server, possible error while registering, unable to proceed.");
        } else if(userJson.contains("\"error\"")) {
            c.err(this, "Server error: " + userJson);
        } else {
            User resultUser = (User) mapper.readToObject(userJson, User.class);

            if(debug) {
                c.out(this, "Got user from server: " + resultUser);
            }
        }
    }

    public void updateUser() {
        c.out(this, "Updating user at " + EndpointProvider.getUpdateUser() + "...");

        String userJson = httpNode.sendPostRequest(EndpointProvider.getUpdateUser(), mapper.getJson(frontendUserUpdate));

        if (userJson == null || userJson.isBlank()) {
            c.err(this, "Did not receive an user from server, possible error while updating, unable to proceed.");
        } else if(userJson.contains("\"error\"")) {
            c.err(this, "Server error: " + userJson);
        } else {
            User resultUser = (User) mapper.readToObject(userJson, User.class);

            if(debug) {
                c.out(this, "Got updated user from server: " + resultUser);
            }

            frontendUser.name = resultUser.name;
            frontendUser.address = resultUser.address;
            frontendUser.phone = resultUser.phone;
            frontendUser.emergency = resultUser.emergency;
            frontendUser.password = frontendUserUpdate.password;
            frontendUserUpdate.oldName = frontendUser.name;
            frontendUserUpdate.oldPassword = frontendUser.password;
        }
    }

    public void getUser() {
        String endpoint = EndpointProvider.getGetUser() + "?name=" + frontendUser.name.replace(" ", "+") + "&password=" + frontendUser.password;

        c.out(this, "Getting user at " + endpoint + "...");

        String userJson = httpNode.sendGetRequest(endpoint);

        if (userJson == null || userJson.isBlank()) {
            c.err(this, "Did not receive an user from server, user seems to not exist.");
        } else if(userJson.contains("\"error\"")) {
            c.err(this, "Server error: " + userJson);
        } else {
            User resultUser = (User) mapper.readToObject(userJson, User.class);

            if(debug) {
                c.out(this, "Got requested user from server: " + resultUser);
            }
        }
    }

    public void connectECGDeviceToUser(ECGDevice ecgDevice) {
        c.out(this, "Connecting ecgDevice to user at " + EndpointProvider.getConnectECGDeviceToUser() + "...");

        ConnectDeviceData connectDeviceData = new ConnectDeviceData();
        connectDeviceData.setUserName(frontendUser.name);
        connectDeviceData.setPassword(frontendUser.password);
        connectDeviceData.setRegDeviceName(ecgDevice.getName());
        connectDeviceData.setRegDevicePin(ecgDevice.getPin());

        String ident = httpNode.sendPostRequest(EndpointProvider.getConnectECGDeviceToUser(), mapper.getJson(connectDeviceData));

        if (ident == null || ident.isBlank()) {
            c.err(this, "Did not receive a device identifier from server, possible error while connecting " +
                    "device to user, unable to proceed.");
        } else if(ident.contains("\"error\"")) {
            c.err(this, "Server error: " + ident);
        } else {
            JsonNode jsonNode = mapper.readTree(ident);
            String ecgDeviceIdentifier = jsonNode.get("identifier").asText();

            if(debug) {
                c.out(this, "Got device identifier from server: " + ecgDeviceIdentifier);
            }

            if (!ecgDeviceIdentifier.equals(ecgDevice.getIdentifier())) {
                c.err(this, "Device identifier received does not match ecgDevice identifier (" +
                        ecgDevice.getIdentifier() + "! Unable to proceed.");
            } else {
                frontendUser.devices.add(ecgDevice);
            }
        }
    }

    public void getLastHealthStatus(int deviceNum) {
        c.out(this, "Requesting last health status at " + EndpointProvider.getReportLastHealthStatus() + "...");

        RequestDeviceAccess request = requestDeviceAccess(deviceNum);

        if (request == null) {
            return;
        }

        String result = httpNode.sendPostRequest(EndpointProvider.getReportLastHealthStatus(), mapper.getJson(request));

        if (result == null || result.isBlank()) {
            c.err(this, "Did not receive a health status from the server!");
        } else if(result.contains("\"error\"")) {
            c.err(this, "Server error: " + result);
        } else {
            JsonNode jsonNode = mapper.readTree(result);
            String status = jsonNode.get("lastAnalysisResult").get("ecgstate").asText();

            if(debug) {
                c.out(this, "Got health status from server: " + status);
            }

            lastHealthStatus = status;
        }
    }

    public void getDatasets(String end, int seconds, int deviceNum) {
        c.out(this, "Requesting datasets at " + EndpointProvider.getGetDatasets() + "...");

        RequestDeviceAccess tempRequest = requestDeviceAccess(deviceNum);

        if (tempRequest == null) {
            return;
        }

        RequestDatasets request = new RequestDatasets();
        request.setUserName(tempRequest.getUserName());
        request.setPassword(tempRequest.getPassword());
        request.setDeviceIdentifier(tempRequest.getDeviceIdentifier());
        request.setEnd(end);
        request.setSeconds(seconds);

        String datasets = httpNode.sendPostRequest(EndpointProvider.getGetDatasets(), mapper.getJson(request));

        if (datasets == null || datasets.isBlank()) {
            c.err(this, "Did not receive datasets from the server!");
        } else if(datasets.contains("\"error\"")) {
            c.err(this, "Server error: " + datasets);
        } else {
            if(debug) {
                c.out(this, "Got datasets from server: " + datasets);
            }
        }
    }

    public void sendSMS() {
        c.out(this, "Sending sms at " + EndpointProvider.getSendSMS() + "...");

        httpNode.sendPostRequest(EndpointProvider.getSendSMS(), "This is a test sms by FHIRTestingClient!");
    }

    public void setFrontendUser(User u) {
        frontendUser = u;
    }

    public void setFrontendUserUpdate(User u) {
        frontendUserUpdate = new UserUpdate(u, frontendUser.name, frontendUser.password);
    }

    private RequestDeviceAccess requestDeviceAccess(int deviceNum) {
        if (frontendUser.devices == null) {
            c.err(this, "requestDeviceAccess: Can't get device number " + deviceNum + ", the user has no devices yet.");
            return null;
        } else if (frontendUser.devices.size() <= deviceNum) {
            c.err(this, "requestDeviceAccess: Can't get device number " + deviceNum + ", the user only has " +
                    frontendUser.devices.size() + " devices!");
            return null;
        }

        RequestDeviceAccess request = new RequestDeviceAccess();
        request.setUserName(frontendUser.name);
        request.setPassword(frontendUser.password);
        request.setDeviceIdentifier(frontendUser.devices.get(deviceNum).getIdentifier());

        if (request.getDeviceIdentifier() == null || request.getDeviceIdentifier().isBlank()) {
            c.err(this, "requestDeviceAccess: The device requested has no identifier associated with it!");
            return null;
        }

        return request;
    }
}
