package com.trevor.wemowidgets;

/**
 * Created by Trevor on 2/17/2015.
 */
public class WemoDeviceSetMessageEvent {
    String state;
    String udn;
    public WemoDeviceSetMessageEvent(String state, String udn){
        this.state=state;
        this.udn = udn;
    }
    public String getUDN()
    {
        return udn;
    }
    public String getState()
    {
        return state;
    }
}
