package com.trevor.wemowidgets;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by Trevor on 2/16/2015.
 */
public class Device extends SugarRecord<Device> {
    String udn;
    String type;
    String friendlyName;
    String serialNumber;
    String state;
    String network;

    public Device() {

    }

    public Device(String udn, String type, String friendlyName, String serialNumber, String state, String network) {
        this.udn = udn;
        this.type = type;
        this.friendlyName = friendlyName;
        this.serialNumber = serialNumber;
        this.state = state;
        this.network=network;
    }
    public String getUdn(){
        return udn;
    }
    public String getFriendlyName(){return friendlyName;}
    public String getState(){return state;}
    public String getType(){return type;}
    public String getNetwork(){
        return network;
    }
    public void setNetwork(String network){
        this.network=network;
    }
}
