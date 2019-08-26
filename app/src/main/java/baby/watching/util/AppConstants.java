package baby.watching.util;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import baby.watching.loc.LockAdmin;

import static android.content.Context.DEVICE_POLICY_SERVICE;

/**
 * Created by mohit.soni on 11/20/2017.
 */

public class AppConstants {
    public static final String TAG = "baby.watching.util.AppConstants";
    public static final String CALL_CUSTOM = "call_custom";
    public static final String NOTIFICATION_RECEIVER = "notification_receiver";
    public static final String NOTIFICATION_LISTENER_RECEIVER = "notification_listener_receiver";
    public static final String TOP_CPU_DATA = "top_cpu_data";
    public static String ROOT_PACKAGE = "";

    //permissions
/*    public static final String[] PERMISSION = {
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.DISABLE_KEYGUARD,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.GET_TASKS,
            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
    };*/

    public static final int PERMISSION_REQUESTCODE = 811;

    public static final int MINI_BRIGHTNESS = 6;
    public static final int MAX_BRIGHTNESS = 9;
    public static final int ANIMATION_SPEED = 1500;

    public static ContentResolver resolver;

    public static Map<Class<?>, Boolean> SERVICE_CLASS = new HashMap<>();

    //-- lock
    public static DevicePolicyManager devicePolicyManager;
    public static ComponentName compName;
    public static boolean IsAdminactive = false;

    public static boolean onCall = false;

    /**
     * set system brightness to given value
     */
    static void SET_DIM(Activity activity, int value) {
        resolver = activity.getContentResolver();
        // 0 - 255
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, value);
        Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        ContentObserver contentObserver = new ContentObserver(new Handler()) {
            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
            }
        };
        resolver.notifyChange(uri, contentObserver);

    }

    public static int GET_BRIGHTNESS() {
        try {
            return Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * get calender of date and time
     *
     * @return date format
     */
    @NonNull
    public DateFormat getCal() {
//        DateFormat dateFormat =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss EEEE");
//        Log.i(TAG,dateFormat.format(new Date()).split(" ")[2]);
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss EEEE", Locale.US);
    }

    /**
     * get date
     *
     * @return date format
     */
    @NonNull
    public String[] getDate() {
        return getCal().format(new Date()).split(" ")[0].split("/");
    }

    /**
     * get time
     *
     * @return time format
     */
    @NonNull
    public String[] getTime() {
        return getCal().format(new Date()).split(" ")[1].split(":");
    }

    /**
     * get day
     *
     * @return time format
     */
    @NonNull
    public String[] getDay() {
        return getCal().format(new Date()).split(" ")[2].split(":");
    }

    /**
     * get month name
     *
     * @param value
     * @return
     */
    public static String getMonth(String value) {
        switch (value) {
            case "01":
                return "Jan";

            case "02":
                return "Feb";

            case "03":
                return "March";

            case "04":
                return "April";

            case "05":
                return "May";

            case "06":
                return "June";

            case "07":
                return "July";

            case "08":
                return "Aug";

            case "09":
                return "Sep";

            case "10":
                return "Oct";

            case "11":
                return "Nov";

            case "12":
                return "Dec";
        }
        return value;
    }

    /**
     * get date in nueric value
     *
     * @param view
     * @return array
     */
    public String getNumDate(TextView view) {
        String[] date_ = getDate();
        setTag(view, "num");
        return getDay()[0] + "" + ", " + date_[2] + "/" + date_[1] + "/" + date_[0];
    }

    /**
     * get date in text value
     *
     * @param view
     * @return array
     */
    public String getTextDate(TextView view) {
        String[] date_ = getDate();
        setTag(view, "text");
        return getDay()[0] + "" + ", " + date_[2] + " " + getMonth(date_[1]) + " " + date_[0].substring(2, 4);
    }

    /**
     * set tag to text view
     *
     * @param view
     * @param value
     */
    public void setTag(TextView view, String value) {
        view.setTag(value);
    }


    /**
     * check if perticular permission is granted or not
     *
     * @param activity
     * @param permission
     * @return
     */
    /*public static boolean checkPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED;
    }*/

    /**
     * get brightness level
     *
     * @param value
     * @return brightness
     */
    public float getBrightness(String value) {
        float y = 0.0f;
        int val = Integer.parseInt(value);
        switch (val) {
            case MINI_BRIGHTNESS:
                y = 0.0f;
                break;
            case 7:
                y = 0.25f;
                break;
            case 8:
                y = 0.50f;
                break;
            case MAX_BRIGHTNESS:
                y = 1.0f;
                break;
            default:
        }
        return y;
    }

    /**
     * if our activity is running
     *
     * @return status
     */
    public static boolean isActivityRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = manager.getRunningTasks(Integer.MAX_VALUE);

        // iterate over activity list
        for (ActivityManager.RunningTaskInfo info : runningTaskInfoList) {
            if (info.topActivity.getPackageName().equals(AppConstants.ROOT_PACKAGE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if service is running or not
     *
     * @return state
     */
    public static void isServiceRunning(Context context, Class<?>[] cls) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (Class<?> clas : cls) {
            List<ActivityManager.RunningServiceInfo> serviceInfos = manager.getRunningServices(Integer.MAX_VALUE);
            if(serviceInfos.size() > 0){
                for(int i=0;i<serviceInfos.size();i++){
                    if (clas.getName().equals(serviceInfos.get(i).service.getClassName())) {
                        SERVICE_CLASS.put(clas, true);
                        continue;
                    } else {
                        SERVICE_CLASS.put(clas, false);
                    }
                }
            }else{
                SERVICE_CLASS.put(clas, false);
            }
        }
    }

    /**
     * get font family
     *
     * @param context
     * @return
     */
    public Typeface getFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                String.format(Locale.US, "fonts/%s", "opensans_light.ttf"));
    }

    /**
     * animate progress bar with value
     *
     * @param progressBar
     * @param value
     */
    public static void animateProgress(ProgressBar progressBar, int value) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, value);
        animation.setDuration(ANIMATION_SPEED);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    public static boolean IsAdmin(Activity activity) {
        devicePolicyManager = (DevicePolicyManager) activity.getSystemService(DEVICE_POLICY_SERVICE);
        compName = new ComponentName(activity, LockAdmin.class);

        IsAdminactive = devicePolicyManager.isAdminActive(compName);
        return IsAdminactive;
    }

    /*public void setBrightness(String vx, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = getBrightness(vx);
        activity.getWindow().setAttributes(lp);
    }*/
}
