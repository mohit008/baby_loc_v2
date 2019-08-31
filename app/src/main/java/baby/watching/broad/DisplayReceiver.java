package baby.watching.broad;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import baby.watching.loc.Home;
import baby.watching.model.BatteryWatcherBean;
import baby.watching.model.BluetoothWatcherBean;
import baby.watching.model.MobileDataWatcherBean;
import baby.watching.model.WifiWatcherBean;
import baby.watching.util.AppConstants;

import static baby.watching.util.AppConstants.BATTERY;
import static baby.watching.util.AppConstants.BLUETOOTH;
import static baby.watching.util.AppConstants.DISABLE;
import static baby.watching.util.AppConstants.ENABLE;
import static baby.watching.util.AppConstants.NETWORK;
import static baby.watching.util.AppConstants.STATUS;
import static baby.watching.util.AppConstants.TYPE;

public class DisplayReceiver extends BroadcastReceiver {
    private static final String TAG = "[DisplayReceiver]";
    Context context;

    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    WindowManager windowManager;
    Display display;
    private WifiManager wifiManager;
    private  WifiWatcherBean wifiWatcherBean;
    public static Boolean PLUGGED = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context.getApplicationContext();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)
                || intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent in = new Intent(context, Home.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
        if (checkActivity()) {
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                PLUGGED = true;
                doWakingTaskForPower();
                Launch();
            }
            if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                PLUGGED = false;
                doWakingTaskForPower();
                Launch();
            }
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                getBatteryManaged(intent);
            }
            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                wifiWatcherBean = new WifiWatcherBean();
                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                    wifiWatcherBean.setStatus(DISABLE);
                }
                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    wifiWatcherBean.setSsid(wifiInfo.getSSID());
                    wifiWatcherBean.setBssid(wifiInfo.getBSSID());
                    wifiWatcherBean.setMac(wifiInfo.getMacAddress());
                    wifiWatcherBean.setSupplicant_state("");
                    wifiWatcherBean.setRssi(wifiInfo.getRssi());
                    wifiWatcherBean.setLink_speed(wifiInfo.getLinkSpeed());
                    wifiWatcherBean.setFrequency(wifiInfo.getFrequency());
                    wifiWatcherBean.setNet_id(wifiInfo.getNetworkId());
                    wifiWatcherBean.setMetered_hint("");
                    wifiWatcherBean.setScore("");
                    wifiWatcherBean.setIpAddress(wifiInfo.getIpAddress());
                    wifiWatcherBean.setStatus(ENABLE);
                }

            }
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                MobileDataWatcherBean mobileDataWatcherBean = new MobileDataWatcherBean();
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean isMobileConn = false;
                NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn |= networkInfo.isConnected();
                }
                if (isMobileConn) {
                    log(networkInfo.toString());
                    mobileDataWatcherBean.setType(networkInfo.getTypeName());
                    mobileDataWatcherBean.setState(networkInfo.isConnected());
                    mobileDataWatcherBean.setReason(networkInfo.getReason());
                    mobileDataWatcherBean.setExtra(networkInfo.getExtraInfo());
                    mobileDataWatcherBean.setSub_type_name(networkInfo.getSubtypeName());
                    mobileDataWatcherBean.setSub_type(networkInfo.getSubtype());
                    mobileDataWatcherBean.setFail_over(networkInfo.isFailover());
                    mobileDataWatcherBean.setAvailable(networkInfo.isAvailable());
                    mobileDataWatcherBean.setRoaming(networkInfo.isRoaming());
                }
                if (!isMobileConn) {
                    mobileDataWatcherBean.setState(false);
                }
                wifiWatcherBean.setMobileDataWatcherBean(mobileDataWatcherBean);
                BraoadCast(NETWORK,wifiWatcherBean);
            }
            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                BluetoothAdapter mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
                BluetoothWatcherBean bluetoothWatcherBean = new BluetoothWatcherBean();
                bluetoothWatcherBean.setName("");
                bluetoothWatcherBean.setMac("");
                if (mBluetoothAdapter.isEnabled()) {
                    bluetoothWatcherBean.setState(true);
                }else{
                    bluetoothWatcherBean.setState(false);
                }
                BraoadCast(BLUETOOTH,bluetoothWatcherBean);
            }
        }
    }

    public void getBatteryManaged(Intent intent) {
        BatteryWatcherBean batteryWatcherBean = new BatteryWatcherBean();
        try {
            int level = intent.getIntExtra("level", 0);
            batteryWatcherBean.setLevel(level);
            batteryWatcherBean.setTemp(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1));
            batteryWatcherBean.setVoltage(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1));

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            String BStatus = "No Data";
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                BStatus = "Charging";
            }
            if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                BStatus = "Discharging";
            }
            if (status == BatteryManager.BATTERY_STATUS_FULL) {
                BStatus = "Full";
            }
            if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                BStatus = "Not Charging";
            }
            if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
                BStatus = "Unknown";
            }
            batteryWatcherBean.setStatus(status);
            batteryWatcherBean.setBattery_status(BStatus);
            batteryWatcherBean.setPower_lugged(PLUGGED);

            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            String batteryPowerSource = "No Data";
            if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
                batteryPowerSource = "AC";
            }
            if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
                batteryPowerSource = "USB";
            }
            batteryWatcherBean.setCharge_plug(chargePlug);
            batteryWatcherBean.setBattery_power_source(batteryPowerSource);

            int BHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            String BatteryHealth = "No Data";
            if (BHealth == BatteryManager.BATTERY_HEALTH_COLD) {
                BatteryHealth = "Cold";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_DEAD) {
                BatteryHealth = "Dead";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_GOOD) {
                BatteryHealth = "Good";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                BatteryHealth = "Over-Voltage";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                BatteryHealth = "Overheat";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
                BatteryHealth = "Unknown";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
                BatteryHealth = "Unspecified Failure";
            }
            batteryWatcherBean.setBattery_health_int(BHealth);
            batteryWatcherBean.setBattery_health(BatteryHealth);

            //Do whatever with the data here
        } catch (Exception e) {
            log("Battery Info Error");
        }
        BraoadCast(BATTERY,batteryWatcherBean);
    }

/*
    /**
     * Handle events of calls and unlock screen if necessary
     * add filter to receiver in manifest
     *     <intent-filter>
     *         <action android:name="android.intent.action.PHONE_STATE" />
     *         </intent-filter>
     *     <intent-filter>
     *             <action android:name="android.intent.action.NEW_OUTGOING_CALL" />
     *     </intent-filter>
     **/

  /*  public void registerTelephonic(Intent intent){
        BabyPhoneStateListener phoneListener = new BabyPhoneStateListener(context);
        telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        if(intent.getAction().equals("android.intent.action.PHONE_STATE")){
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number = TelephonyManager.EXTRA_INCOMING_NUMBER;
            // ringing
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
//                callState(number);
            }
            // picked up
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
            }
            // call ended
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            }
        }
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            Toast.makeText(context,"Call",Toast.LENGTH_SHORT).show();
        }
    }*/

    /**
     * call locker activity
     */
    public void doWakingTaskForPower() {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        if (display.getState() == Display.STATE_ON) {
        }
        if (display.getState() == Display.STATE_OFF) {
            wake();
        }
    }

    public void Launch() {
        if(!checkActivity()){
            Intent in = new Intent(context, Home.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
    }

    public void wake() {
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager
                .SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "babyLoc:tag");
        wakeLock.acquire();
        wakeLock.release();
    }

    public boolean checkActivity() {
        return AppConstants.isActivityRunning(context);
    }

    /*public void onDestroy() {
        telephony.listen(null, PhoneStateListener.LISTEN_NONE);
    }*/
    private void log(String msg) {
        Log.i(TAG, msg);
    }

    public void BraoadCast(String type_, Object object) {
        Intent intent_battery = new Intent(AppConstants.STATUS_RECEIVER);
        switch (type_) {
            case BATTERY:
                intent_battery.putExtra(TYPE,type_);
                intent_battery.putExtra(STATUS, (BatteryWatcherBean) object);
                break;
            case NETWORK:
                intent_battery.putExtra(TYPE, type_);
                intent_battery.putExtra(STATUS,(WifiWatcherBean) object);
                break;
            case BLUETOOTH:
                intent_battery.putExtra(TYPE, type_);
                intent_battery.putExtra(STATUS,(BluetoothWatcherBean) object);
                break;
        }
        context.sendBroadcast(intent_battery);
    }
}
