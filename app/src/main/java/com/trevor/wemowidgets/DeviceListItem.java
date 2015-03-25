package com.trevor.wemowidgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.belkin.wemo.localsdk.WeMoDevice;

import java.io.File;

/**
 * Created by Trevor on 2/12/2015.
 */
public class DeviceListItem extends FrameLayout {
    /**
     * Display a name of the WeMoDevice
     */
    private TextView mName;

    /**
     * Displays a current state of WeMoDevice
     */
    private TextView mState;

    /**
     * Displays a current logo
     */
    private ImageView mLogo;

    /**
     * Corresponding WeMoDevice
     */
    private Device mDevice = null;

    /**
     * Constructor
     */
    public DeviceListItem(Context context) {
        super(context);
        init(context);
    }

    /**
     * Sets the layout
     *
     * @param context application context
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.device_list_item_layout, this);

        mName = (TextView) findViewById(R.id.name);
        mState = (TextView) findViewById(R.id.state);
        mLogo = (ImageView) findViewById(R.id.logo);
    }

    /**
     * Sets the WeMoDevice corresponding to this ListItem and displays its parameters
     *
     * @param device the corresponding WeMoDevice
     */
    public void setDevice(Device device) {
        mDevice = device;
        mName.setText(mDevice.getFriendlyName());
        setState(mDevice.getState().split("\\|")[0]);
        String type = mDevice.getType();
        Resources res = getResources();

        if (type.equals(WeMoDevice.SWITCH)) {
            Drawable insiteswitchdrawable = res.getDrawable(R.drawable.insight);
            mLogo.setImageDrawable(insiteswitchdrawable);
        }
        else if(type.equals(WeMoDevice.LIGHT_SWITCH)){
            Drawable lightswitchdrawable = res.getDrawable(R.drawable.lightswitch);
            mLogo.setImageDrawable(lightswitchdrawable);
        }
       // File logo = new File(mDevice.getLogo());

       // if (logo.exists()) {
            //load the logo from local storage
         //   Bitmap bitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
        //    mLogo.setImageBitmap(bitmap);
       // } else {
            // load the logo from URL
            //new LogoDownloader().execute(mDevice.getLogoURL());
      //  }
    }

    /**
     * @return the corresponding weMoDevice
     */
    public Device getDevice() {
        return mDevice;
    }

    /**
     * Sets the displaying state depending on WeMoDevice type
     *
     * @param state the new state of WeMoDevice
     */
    public void setState(String state) {
        int stateColor = getResources().getColor(R.color.blue_color);
        String stateText = null;

        //SWITCH || LIGHT_SWITCH
        String type = mDevice.getType();
        if (type.equals(WeMoDevice.SWITCH) || type.equals(WeMoDevice.LIGHT_SWITCH)) {
            if (state.equals(WeMoDevice.WEMO_DEVICE_ON)) {
                stateText = getResources().getString(R.string.state_on);
            } else if (state.equals(WeMoDevice.WEMO_DEVICE_OFF)) {
                stateText = getResources().getString(R.string.state_off);
                stateColor = getResources().getColor(R.color.black_color);
            } else if (state.equals(WeMoDevice.WEMO_DEVICE_UNDEFINED)) {
                stateColor = getResources().getColor(R.color.dark_grey_color);
            }
            //SENSOR
        } else if (type.equals(WeMoDevice.SENSOR)) {
            stateText = state.equals(WeMoDevice.WEMO_DEVICE_ON) ? getResources().getString(R.string.state_motion)
                    : getResources().getString(R.string.state_wait);
            //INSIGHT
        } else if (type.equals(WeMoDevice.INSIGHT)) {
            if (state.equals(WeMoDevice.WEMO_DEVICE_ON)) {
                stateText = getResources().getString(R.string.state_load);
            } else if (state.equals(WeMoDevice.WEMO_DEVICE_OFF)) {
                stateColor = getResources().getColor(R.color.black_color);
                stateText = getResources().getString(R.string.state_off);
            } else if (state.equals(WeMoDevice.WEMO_DEVICE_STAND_BY)) {
                stateColor = getResources().getColor(R.color.orange_color);
                stateText = getResources().getString(R.string.state_stand_by);
            } else if (state.equals(WeMoDevice.WEMO_DEVICE_UNDEFINED)) {
                stateColor = getResources().getColor(R.color.dark_grey_color);
            }
        }

        mState.setTextColor(stateColor);
        mState.setText(stateText == null ? "" : stateText);
    }
}