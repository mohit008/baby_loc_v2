/*
package baby.watching.broad;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import baby.watching.loc.Home;
import baby.watching.util.AppConstants;

public class BabyPhoneStateListener extends PhoneStateListener {

    public static Boolean phoneRinging = false;
    Context context;
    public static Boolean isActivityRunning= false;

    public BabyPhoneStateListener(Context context){
        this.context = context;
    }

    public void onCallStateChanged(int state, String incomingNumber) {

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d("DEBUG", "HOOKUP");
                phoneRinging = false;
                if(isActivityRunning){
                    if (!AppConstants.isActivityRunning(context)) {
                        callLocking();
                    }
                }
                AppConstants.onCall = false;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d("DEBUG", "OFFHOOK");
                phoneRinging = false;
                AppConstants.onCall = true;
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d("DEBUG", "RINGING");
                phoneRinging = true;
                isActivityRunning = AppConstants.isActivityRunning(context);
                break;
        }
    }
    */
/**
     * call locker activity
     *//*

    public void callLocking() {
        Intent in = new Intent(context, Home.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);
    }

}
*/
