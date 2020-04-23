package com.example.hackathon_covid19;

public class Essen_issuePOJO {

    String userName;
    String userEmailId;
    String userMessage;
    //String userDeviceId;
    String time;
    String location;

    public Essen_issuePOJO() {
    }

    public Essen_issuePOJO(String userName, String userEmailId, String userMessage,String time,String location) {
        this.userName = userName;
        this.userEmailId = userEmailId;
        this.userMessage = userMessage;
        //this.userDeviceId = userDeviceId;
        this.time=time;
        this.location=location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

//    public String getUserDeviceId() {
//        return userDeviceId;
//    }
//
//    public void setUserDeviceId(String userDeviceId) {
//        this.userDeviceId = userDeviceId;
//    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
