package com.trevor.wemowidgets;

/**
 * Created by Trevor on 2/16/2015.
 */
public class WemoDeviceAddedMessageEvent {
    Device mDevice;
    public WemoDeviceAddedMessageEvent(Device device){
        mDevice = device;
    }
    public Device getDevice(){
        return mDevice;
    }
}
