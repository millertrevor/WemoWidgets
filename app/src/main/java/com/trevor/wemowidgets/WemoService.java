package com.trevor.wemowidgets;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.belkin.wemo.localsdk.WeMoDevice;
import com.belkin.wemo.localsdk.WeMoSDKContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

import de.greenrobot.event.EventBus;

/**
 * Created by Trevor on 2/13/2015.
 */
public class WemoService extends Service implements WeMoSDKContext.NotificationListener {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    private WeMoSDKContext mWeMoSDKContext = null;
    private ArrayList<String> _udns = new ArrayList<String>();
    Handler mHandler;
    private final Semaphore semaphore = new Semaphore(1);

    @Override
    public void onCreate() {
        // The service is being created
        mHandler = new Handler();
        mWeMoSDKContext = new WeMoSDKContext(this);
        mWeMoSDKContext.addNotificationListener(this);

        mWeMoSDKContext.refreshListOfWeMoDevicesOnLAN();

        EventBus.getDefault().register(this);
    }


    @Override
    public void onNotify(final String event, final String udn) {
      //  lockingObject.lock();


        try {
            semaphore.acquire();
            //this.getApplicationContext().runOnUiThread(new Runnable() {

            //  @Override
            // public void run() {
            WeMoDevice wemoDevice = mWeMoSDKContext.getWeMoDeviceByUDN(udn);

            if (event.equals(WeMoSDKContext.REFRESH_LIST)) {
                OnRefreshListNotify();
            } else if (wemoDevice == null) {
                //do nothing because of incorrect notification
            } else if (event.equals(WeMoSDKContext.ADD_DEVICE)) {
                OnAddDeviceNotify(wemoDevice);
            } else if (event.equals(WeMoSDKContext.REMOVE_DEVICE)) {
                OnRemoveDeviceNotify(wemoDevice);

            } else if (event.equals(WeMoSDKContext.CHANGE_STATE) || event.equals(WeMoSDKContext.SET_STATE)) {
                OnChangeOrSetStateNotify(wemoDevice,udn);
            }
            //  }
            // });
        }
        catch(Exception e)
        {
int blocker = 1;
        }
        finally{
           // lockingObject.unlock();
            semaphore.release();

        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()

        return mStartMode;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WemoService.this, "Destroy WemoService", Toast.LENGTH_SHORT).show();
            }
        });
        int stopper = 1;
        EventBus.getDefault().unregister(this);
        mWeMoSDKContext.removeNotificationListener(this);
    }

    private void OnRefreshListNotify() {
        ArrayList<String> udns = mWeMoSDKContext.getListOfWeMoDevicesOnLAN();
     //   ArrayList<WeMoDevice> wemoDevices = new ArrayList<WeMoDevice>();

        for (String udn : udns) {
            WeMoDevice listDevice = mWeMoSDKContext.getWeMoDeviceByUDN(udn);
            List<Device> devices = Device.find(Device.class,"udn = ?",udn);
            Log.i("WemoService.activity","       --         ");
            Log.i("WemoService.activity","UDN passed in: "+udn);
            Log.i("WemoService.activity","Found this many devices: "+devices.size());
            for (Device device : devices) {
                Log.i("WemoService.activity","UDN: "+device.getUdn());
            }
            Log.i("WemoService.activity","        ----           ");
            List<Device> allForTest = Device.listAll(Device.class);
            if(devices.size()>1){
                int whoops = -1;
            }
            else {
                if (devices.size() == 1) {
                    int okay = 1;
                    Device d = devices.get(0);
                    d.state = listDevice.getState();
                    d.serialNumber = listDevice.getSerialNumber();
                    d.friendlyName = listDevice.getFriendlyName();
                    d.type = listDevice.getType();
                    d.udn = listDevice.getUDN();
                    d.save();
                    d.notify();
                } else {
                    int noresult = 0;
                    Device d = new Device();
                    d.state = listDevice.getState();
                    d.serialNumber = listDevice.getSerialNumber();
                    d.friendlyName = listDevice.getFriendlyName();
                    d.type = listDevice.getType();
                    d.udn = listDevice.getUDN();
                    d.save();
                    d.notify();
                }
            }
           // if (listDevice != null && listDevice.isAvailable()) {
           //     wemoDevices.add(listDevice);
           // }
        }

       /* mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WemoService.this, "Hello Toast REFERSH!", Toast.LENGTH_SHORT).show();
            }
        });*/
        EventBus.getDefault().post(new WemoDeviceRefreshMessageEvent());
    }

    private void OnChangeOrSetStateNotify(final WeMoDevice listDevice, final String udn) {
       // WeMoDevice listDevice = mWeMoSDKContext.getWeMoDeviceByUDN(udn);
        List<Device> devices = Device.find(Device.class, "udn = ?", udn);
        Log.i("WemoService.activity","       -notify-                          ");
        Log.i("WemoService.activity","UDN: "+udn);
        Log.i("WemoService.activity","Found this many devices: "+devices.size());
        for (Device device : devices) {
            Log.i("WemoService.activity","UDN: "+device.getUdn());
        }
        Log.i("WemoService.activity","         -change-              ");
       // List<Device> allForTest = Device.listAll(Device.class);
        if(devices.size()>1){
            int whoops = -1;
        }
        else if (devices.size()==1)
        {
            int okay=1;
            Device d = devices.get(0);
            d.state = listDevice.getState();
            d.serialNumber = listDevice.getSerialNumber();
            d.friendlyName = listDevice.getFriendlyName();
            d.type = listDevice.getType();
            d.udn = listDevice.getUDN();
            d.save();
            d.notify();
        }
        else
        {
            int noresult = 0;
            Device d = new Device();
            d.state = listDevice.getState();
            d.serialNumber = listDevice.getSerialNumber();
            d.friendlyName = listDevice.getFriendlyName();
            d.type = listDevice.getType();
            d.udn = listDevice.getUDN();
            d.save();
            d.notify();
        }
        /*
        if (mListView != null) {
            for (int i = 0; i <= mListView.getLastVisiblePosition() - mListView.getFirstVisiblePosition(); i++) {
                 WeMoDevice wemoDevice = (WeMoDevice) mListView.getItemAtPosition(i);
                // //ListItem listItem = (ListItem)mListView.getChildAt(i);
                 if (wemoDevice.getUDN().equals(udn)) {
                     wemoDevice.setState(wemoDevice.getState().split("\\|")[0]);

                     break;
                 }
            }
        }*/
        EventBus.getDefault().post(new WemoDeviceChangeSetMessageEvent());
    }

    private void OnRemoveDeviceNotify(WeMoDevice wemoDevice) {
      /*  Toast.makeText(getActivity().getApplicationContext(),
                getString(R.string.notification_remove) + wemoDevice.getFriendlyName(),
                Toast.LENGTH_SHORT).show();*/
    }

    private void OnAddDeviceNotify(WeMoDevice wemoDevice) {
        //WeMoDevice listDevice = mWeMoSDKContext.getWeMoDeviceByUDN(udn);

        Device d = new Device();
        d.state = wemoDevice.getState();
        d.serialNumber = wemoDevice.getSerialNumber();
        d.friendlyName = wemoDevice.getFriendlyName();
        d.type = wemoDevice.getType();
        d.udn = wemoDevice.getUDN();
        d.save();
        d.notify();

        /*Adapter adapter = (Adapter) mListView.getAdapter();
        if (adapter != null) {
            adapter.add(listDevice);
        }
        Toast.makeText(getActivity().getApplicationContext(),
                getString(R.string.notification_add) + wemoDevice.getFriendlyName(),
                Toast.LENGTH_SHORT).show();*/
        EventBus.getDefault().post(new WemoDeviceAddedMessageEvent(d));
    }


    public void onEvent(NetworkRefreshMessageEvent event){
        List<Device> allDevices =  Device.listAll(Device.class );
        mWeMoSDKContext.refreshListOfWeMoDevicesOnLAN();
    }

    public void onEvent(WemoDeviceSetMessageEvent event){
        mWeMoSDKContext.setDeviceState(event.getState(),event.getUDN());
    }

}