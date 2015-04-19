package com.trevor.wemowidgets;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
  //  String[] widgets;

    public Device() {

    }

    public Device(String udn, String type, String friendlyName, String serialNumber, String state, String network) {
        this.udn = udn;
        this.type = type;
        this.friendlyName = friendlyName;
        this.serialNumber = serialNumber;
        this.state = state;
        this.network=network;
       // this.widgets = widgets;
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
 /*   public  List<Widget> getWidgets(){
        Widget.find(Widget.class,"", Widget.)
     //   return Book.find(Book.class, "author = ?", new String{author.getId()})
    }*/
   /* public void addWidget(String widgetID){
        List<String> list = new ArrayList<String>(Arrays.asList(widgets));
        if(!list.contains(widgetID))
        {
            list.add(widgetID);
        }
        widgets = list.toArray(widgets);
    }*/
   /* public void removeWidget(String widgetID) {
        List<String> list = new ArrayList<String>(Arrays.asList(widgets));
        list.removeAll(Arrays.asList(widgetID));
        widgets = list.toArray(widgets);
    }*/
}
