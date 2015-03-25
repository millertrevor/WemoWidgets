package com.trevor.wemowidgets;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.belkin.wemo.localsdk.WeMoDevice;
import com.belkin.wemo.localsdk.WeMoSDKContext;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Trevor on 2/12/2015.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener {

   // private WeMoSDKContext mWeMoSDKContext = null;
   // private ArrayList<String> _udns = new ArrayList<String>();
   // Intent _rememberedIntent;

    private ListView mListView;
    private Button mRefreshButton;
    //private View mProgressBar;

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);

        mListView = (ListView)rootView.findViewById(R.id.deviceListView);
        mRefreshButton = (Button)rootView.findViewById(R.id.refresh_button);
//these 3 lines here to reactive functionality
        //don't forget the ondestroy item
     //   mWeMoSDKContext = new WeMoSDKContext(getActivity().getApplicationContext());
      //  mWeMoSDKContext.addNotificationListener(this);

     //   mWeMoSDKContext.refreshListOfWeMoDevicesOnLAN();

      //  _rememberedIntent = new Intent(getActivity(), WemoService.class);
       // getActivity().startService(_rememberedIntent);

mListView.setOnItemClickListener(this);
        mRefreshButton.setOnClickListener(this);
        EventBus.getDefault().register(this);


        ArrayList<Device> devices =new ArrayList<Device>(Device.listAll(Device.class));
        mListView.setAdapter(new Adapter(getActivity().getApplicationContext(), 0, devices));

        return rootView;
    }

    /**
     * Handle the click on refresh button
     * and update the device list
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.refresh_button) {
            refresh();
           // Intent intent = new Intent(getActivity(), WemoService.class);
           // getActivity().startService(_rememberedIntent);
           // getActivity().stopService(intent);
        }
    }
    /**
     * Updates the list of WeMoDevices in WeMoSDKContext
     */
    private void refresh() {
        EventBus.getDefault().post(new NetworkRefreshMessageEvent());
        //mProgressBar.setVisibility(View.VISIBLE);
        mRefreshButton.setEnabled(false);
       // mWeMoSDKContext.refreshListOfWeMoDevicesOnLAN();
    }

    /**
     * Change the state of a WeMoDevice (if this can be done)
     *
     * @param list ListView
     * @param view ListItem
     * @param position the position of current ListItem in ListView
     * @param id the id of the current view
     */
    @Override
    public void onItemClick(AdapterView<?> list, View view, int position, long id) {
        Device device = ((DeviceListItem)view).getDevice();

        //we can change the state of switches and insight devices only
        String type = device.getType();
        String state = device.getState().split("\\|")[0];

        if(type.equals(WeMoDevice.SWITCH)
                || type.equals(WeMoDevice.LIGHT_SWITCH)
                || type.equals(WeMoDevice.INSIGHT)) {
            String newState = WeMoDevice.WEMO_DEVICE_ON;

            if (state.equals(WeMoDevice.WEMO_DEVICE_ON) || state.equals(WeMoDevice.WEMO_DEVICE_STAND_BY)) {
                newState = WeMoDevice.WEMO_DEVICE_OFF;
            }

            //mWeMoSDKContext.setDeviceState(newState, device.getUDN());
            EventBus.getDefault().post(new WemoDeviceSetMessageEvent(newState, device.getUdn()));
            ((DeviceListItem)view).setState(WeMoDevice.WEMO_DEVICE_UNDEFINED);
        }
    }

    @Override
    public void onDestroy(){
//        mWeMoSDKContext.stop();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(WemoDeviceAddedMessageEvent event){
        //final String udnFromEvent = event.getUdn();
        final Device deviceFromEvent = event.getDevice();
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
        Toast.makeText(getActivity().getApplicationContext(),"From the MessageBUS ADD",Toast.LENGTH_SHORT).show();
                //WeMoDevice listDevice = mWeMoSDKContext.getWeMoDeviceByUDN(udnFromEvent);

                Adapter adapter = (Adapter) mListView.getAdapter();
                if (adapter != null) {
                    adapter.add(deviceFromEvent);
                }
            }
        });

    }
    public void onEvent(WemoDeviceChangeSetMessageEvent event){

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
              //  Toast.makeText(getActivity().getApplicationContext(),"From the MessageBUS Refresh",Toast.LENGTH_SHORT).show();


                // ArrayList<String> udns = mWeMoSDKContext.getListOfWeMoDevicesOnLAN();
               // ArrayList<WeMoDevice> wemoDevices = new ArrayList<WeMoDevice>();
                ArrayList<Device> devices =new ArrayList<Device>(Device.listAll(Device.class));
               // for (Device d : devices) {
                 //   String udn = d.getUdn();
                  //  WeMoDevice listDevice = mWeMoSDKContext.getWeMoDeviceByUDN(udn);
                   //// if (listDevice != null && listDevice.isAvailable()) {
                   //     wemoDevices.add(listDevice);
                   // }
               // }
                mListView.setAdapter(new Adapter(getActivity().getApplicationContext(), 0, devices));//not wemoDevices
                //  mProgressBar.setVisibility(View.GONE);
                mRefreshButton.setEnabled(true);





                Toast.makeText(getActivity().getApplicationContext(),"From the MessageBUS Change!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onEvent(WemoDeviceRefreshMessageEvent event){


        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(),"From the MessageBUS Refresh",Toast.LENGTH_SHORT).show();


       // ArrayList<String> udns = mWeMoSDKContext.getListOfWeMoDevicesOnLAN();
       // ArrayList<WeMoDevice> wemoDevices = new ArrayList<WeMoDevice>();
                ArrayList<Device> devices =new ArrayList<Device>(Device.listAll(Device.class));
      //  for (Device d : devices) {
      //      String udn = d.getUdn();
      //      WeMoDevice listDevice = mWeMoSDKContext.getWeMoDeviceByUDN(udn);
      //      if (listDevice != null && listDevice.isAvailable()) {
      //          wemoDevices.add(listDevice);
      //      }
      //  }
        mListView.setAdapter(new Adapter(getActivity().getApplicationContext(), 0, devices));//not wemoDevices
        //  mProgressBar.setVisibility(View.GONE);
        mRefreshButton.setEnabled(true);





              //  Toast.makeText(getActivity().getApplicationContext(),"From the MessageBUS Change!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * The adapter to ListView
     *
     * @author Anastasia Artemyeva
     */
    private class Adapter extends ArrayAdapter<Device> {
        private ArrayList<Device> mDevices;

        /**
         * Constructor
         *
         * @param context application context
         * @param resource (not used)
         * @param devices the array of WeMoDevices to display
         */
        public Adapter(Context context, int resource, ArrayList<Device> devices) {
            super(context, resource, devices);
            mDevices = devices;
        }

        /**
         * Creates ListItem corresponding to WeMoDevice
         *
         * @param position the position of WeMoDevice in entry array
         * @param convertView the created view, corresponded to the WeMoDevice
         * @param parent (not used)
         *
         * @return created ListItem view
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new DeviceListItem(getActivity().getApplicationContext());
            }
            ((DeviceListItem)convertView).setDevice(mDevices.get(position));

            return convertView;
        }
    }


}

