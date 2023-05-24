package com.fhirtestclient.helper;

public class EndpointProvider {
    private static final String server = "http://localhost:8080";
    private static final String endpointConnect = "/connect";
    private static final String endpointData = "/data";
    private static final String endpointUser = "/user";

    private static final String registerECGDevice = server + endpointConnect + "/registerECGDevice";
    private static final String registerFrontendDevice = server + endpointConnect + "/registerFrontendDevice";
    private static final String registerUser = server + endpointConnect + "/registerUser";
    private static final String connectECGDeviceToUser = server + endpointConnect + "/connectECGDeviceToUser";

    private static final String receiveObservation = server + endpointData + "/receive";
    private static final String receiveJson = server + endpointData + "/receive";
    private static final String receiveJson_fromCustomEsp32 = server + endpointData + "/receive/esp32_custom";
    private static final String reflect = server + endpointData + "/reflect";
    private static final String reportLastHealthStatus = server + endpointData + "/lastHealthStatus";
    private static final String getDatasets = server + endpointData + "/getDataset";

    private static final String testConnection = server + endpointUser + "/test";
    private static final String returnHealthStatus = server + endpointUser + "/";
    private static final String getRecentEcgData = server + endpointUser + "/getRecentEcg";
    private static final String sendSMS = server + endpointUser + "/sendsms";
    private static final String getUser = server + endpointUser + "/get-user";
    private static final String postUser = server + endpointUser + "/post-user";
    private static final String updateUser = server + endpointUser + "/update-user";

    public static String getRegisterECGDevice() {
        return registerECGDevice;
    }

    public static String getRegisterFrontendDevice() {
        return registerFrontendDevice;
    }

//    public static String getRegisterUser() {
//        return registerUser;
//    }

    public static String getConnectECGDeviceToUser() {
        return connectECGDeviceToUser;
    }

    public static String getReceiveObservation() {
        return receiveObservation;
    }

    public static String getReceiveJson() {
        return receiveJson;
    }

    public static String getReceiveJson_fromCustomEsp32() {
        return receiveJson_fromCustomEsp32;
    }

    public static String getReflect() {
        return reflect;
    }

    public static String getReportLastHealthStatus() {
        return reportLastHealthStatus;
    }

    public static String getGetDatasets() {
        return getDatasets;
    }

    public static String getTestConnection() {
        return testConnection;
    }

    public static String getReturnHealthStatus() {
        return returnHealthStatus;
    }

    public static String getGetRecentEcgData() {
        return getRecentEcgData;
    }

    public static String getSendSMS() {
        return sendSMS;
    }

    public static String getGetUser() {
        return getUser;
    }

    public static String getPostUser() {
        return postUser;
    }

    public static String getUpdateUser() {
        return updateUser;
    }
}
