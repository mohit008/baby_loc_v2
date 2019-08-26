package baby.watching.broad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

import baby.watching.loc.Home;
import baby.watching.util.AppConstants;

public class DisplayReceiver extends BroadcastReceiver {
    Context context;

/*
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
*/
    WindowManager windowManager;
    Display display;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context.getApplicationContext();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)
                || intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            doWakingTask();
        }
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
    public void doWakingTask() {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        if (display.getState() == Display.STATE_ON) {
            if (!AppConstants.isActivityRunning(context)) {
                Intent in = new Intent(context, Home.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }
            /*wake();*/
            /*broadcastIntent(Integer.toString(delta), AppConstants.PROXIMITY);*/
        }
        if (display.getState() == Display.STATE_OFF) {
            //it is locked
        }
    }

/*
    public void wake() {
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager
                .SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "babyLoc:tag");
        wakeLock.acquire();
        wakeLock.release();
    }
*/


    /*public void onDestroy() {
        telephony.listen(null, PhoneStateListener.LISTEN_NONE);
    }*/
}
