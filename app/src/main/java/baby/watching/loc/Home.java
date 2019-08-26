package baby.watching.loc;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

import baby.watching.R;
import baby.watching.adapter.RecycleAdapter;
import baby.watching.broad.MonitorService;
import baby.watching.broad.NotificationWatcher;
import baby.watching.model.NotificationBean;
import baby.watching.util.AppConstants;
import baby.watching.util.LockscreenUtils;
import baby.watching.weather.WeatherMain;

import static baby.watching.util.AppConstants.PERMISSION_REQUESTCODE;

/**
 * Created by mohit.soni on 11/20/2017.
 */

public class Home extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback,
        LockscreenUtils.OnLockStatusChangedListener {

    private static final String TAG = "baby.watching.loc.Home";

    private boolean granted = true;
    AppConstants constantsLocking = new AppConstants();
    NotificationListenerReceiver notificationListenerReceiver;
//    TOP_CPU_LISTENER top_cpu_listener;

    TextClock clock;
    TextView date;
    RelativeLayout rl;
    Button btnEnableAdmin;
    WebView wvTempIcon;
    TextView tvTemp, tvTempText;
    LinearLayout llWeather;

    public static final int RESULT_ENABLE = 11;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecycleAdapter recycleAdapter;

    private LockscreenUtils LockscreenUtils;
    NotificationManager mNotificationManager;

    ArrayList<NotificationBean> statusBar;
    NotificationWatcher notificationWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
        this.getWindow().setType(
                WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);

        super.onCreate(savedInstanceState);
        Log.i(TAG, "Activity Created");

        request();
        register();
        setInit();
    }

    /**
     * register broadcast receiver
     */
    private void register() {
        Set<String> listenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
        boolean haveAccess = false;
        for (String sd : listenerSet) {
            if (sd.equals(AppConstants.ROOT_PACKAGE)) {
                haveAccess = true;
            }
        }
        if (!haveAccess) {
            startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS),PERMISSION_REQUESTCODE);
        }

        notificationListenerReceiver = new NotificationListenerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.NOTIFICATION_RECEIVER);
        registerReceiver(notificationListenerReceiver, filter);

//        top_cpu_listener = new TOP_CPU_LISTENER();
//        IntentFilter top_filter = new IntentFilter();
//        top_filter.addAction(AppConstants.TOP_CPU_DATA);
//        registerReceiver(top_cpu_listener, top_filter);

    }

    /**
     * perform service tasks
     */
    public void serviceTask() {
//        AppConstants.isServiceRunning(this.getApplicationContext(), AppConstants.CLASSES);
//        for (int i = 0; i < AppConstants.SERVICE_CLASS.size(); i++) {
//            boolean status = AppConstants.SERVICE_CLASS.get(AppConstants.CLASSES[i]);
//            if (!status) {
//                startService(new Intent(this, AppConstants.CLASSES[i]));
//                startService(new Intent(this, NotificationCollectorMonitorService.class));
//            }
//        }
        startService(new Intent(this, NotificationWatcher.class));
    }

    /** asked for request if API > 22
     *
     */
    private void request() {
        if (Build.VERSION.SDK_INT > 22) {
            boolean canChange = Settings.System.canWrite(this);
            if (!canChange) {
                Intent openSetting = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                openSetting.setData(Uri.parse("package:" + this.getPackageName()));
                openSetting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(openSetting,PERMISSION_REQUESTCODE);

                //-- auto start app setting
                String manufacturer = "xiaomi";
                if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                    //this will open auto start screen where user can enable permission for your app
                    Intent intent1 = new Intent();
                    intent1.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                    startActivityForResult(intent1,PERMISSION_REQUESTCODE);
                }
            }

            // -- add window on top
            boolean canOverLay = Settings.canDrawOverlays(this);
            if (!canOverLay) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent,PERMISSION_REQUESTCODE);
            }

            // -- permission
            requestPermissions(new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.DISABLE_KEYGUARD,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.GET_TASKS,
                    Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE}, PERMISSION_REQUESTCODE);
        }
    }

    /**
     * do nothing
     */
    @Override
    public void onBackPressed() {
        return;
    }


    /** set initial variables
     *
     */
    public void setInit() {
        setContentView(R.layout.locker);

        clock = (TextClock) findViewById(R.id.clock);
        date = (TextView) findViewById(R.id.date);
        rl = (RelativeLayout) findViewById(R.id.rl);

        btnEnableAdmin = (Button) findViewById(R.id.btnEnableAdmin);

        wvTempIcon = (WebView) findViewById(R.id.wvTempIcon);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvTempText = (TextView) findViewById(R.id.tvTempText);
        llWeather = (LinearLayout) findViewById(R.id.llWeather);

        wvTempIcon.setBackgroundColor(Color.TRANSPARENT);
        date.setTypeface(constantsLocking.getFont(this));
        clock.setTypeface(constantsLocking.getFont(this));
        clock.setFormat12Hour("KK mm");

        clock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (granted) {
                    notificationWatcher.onDestroy();
                    unlockButton();
                }else{
                    request();
                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rvNotification);
        mLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, true);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        date.setText(constantsLocking.getNumDate(date));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) date.getTag();
                if (tag.equals("num")) {
                    date.setText(constantsLocking.getTextDate(date));
                } else {
                    date.setText(constantsLocking.getNumDate(date));
                }
                int id = (int) System.currentTimeMillis();
                generateNotification(id);

            }
        });
        btnEnableAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, AppConstants.compName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
                startActivityForResult(intent, RESULT_ENABLE);

                btnEnableAdmin.setVisibility(View.GONE);

                //-- remove admin right
                /*devicePolicyManager.removeActiveAdmin(compName);*/
            }
        });

        LockscreenUtils = new LockscreenUtils();

        //-- check for admin right
        if (AppConstants.IsAdmin(this)) {
            btnEnableAdmin.setVisibility(View.GONE);
        } else {
            btnEnableAdmin.setVisibility(View.VISIBLE);
            Toast.makeText(Home.this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
        }

        //-- unlock screen in case of app get killed by system
        if (getIntent() != null && getIntent().hasExtra("kill")
                && getIntent().getExtras().getInt("kill") == 1) {
            enableKeyguard();
            unlockButton();
        } else {
            try {
                disableKeyguard();
                lock();
                serviceTask();
            } catch (Exception e) {
            }
        }
        new WeatherMain(this, wvTempIcon, tvTemp, llWeather, tvTempText);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        statusBar = new ArrayList<>();
        notificationWatcher = new NotificationWatcher();
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log("onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        unlockButton();
    }
    /**
     * unregister displayReceiver
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationListenerReceiver);
//        unregisterReceiver(top_cpu_listener);
        stopService(new Intent(this, NotificationWatcher.class));
        startService(new Intent(this, MonitorService.class));
        log("onDestroy");
    }

/*    private void callingWatchingMonitor() {
//        try {
//            // adb shell am stopservice com.baby.watching.behind/.MonitorService
//            // adb shell am start-foreground-service com.baby.watching.behind/.MonitorService
//            Process process = Runtime.getRuntime().exec("adb shell am start-foreground-service com.baby.watching.behind/.MonitorService");
//            BufferedReader bufferedReader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.baby.watching.behind");
        startActivity( launchIntent );
    }*/

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUESTCODE:
                if (permissions.length > 1 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    granted = false;
                }
        }
    }

    /**
     * get event from notification listener class
     */
    class NotificationListenerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                if(((String)intent.getExtras().get("status")).equals("all")){
                    statusBar = new ArrayList<>();
                    statusBar = (ArrayList<NotificationBean>) intent.getExtras().get("notificationBeans");
                    recycleAdapter = new RecycleAdapter(Home.this.getApplicationContext(), statusBar);
                    mRecyclerView.setAdapter(recycleAdapter);
                }
                if(((String)intent.getExtras().get("status")).equals("new")){
                    NotificationBean bean = (NotificationBean) intent.getExtras().get("Bean");
                    recycleAdapter.addBean(bean);
                }
                mRecyclerView.scrollToPosition(statusBar.size() - 1);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * get event from top command listener class
     */
//    class TOP_CPU_LISTENER extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(AppConstants.TOP_CPU_DATA)) {
////                Toast.makeText(Home.this,(String)intent
////                        .getExtras().getString("value"),Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    /** init notification with dummy one
     *
     * @param id
     */
    public void generateNotification(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("default", "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
                mNotificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Baby Notification")
                    .setContentText("Baby showing notification")
                    .setAutoCancel(true);
            mNotificationManager.notify(id, mBuilder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this);
            builder.setContentTitle("Baby Notification");
            builder.setContentText("Baby showing notification");
            builder.setTicker("Notification Listener Service Example");
            builder.setSmallIcon(R.drawable.app_icon);
            builder.setAutoCancel(true);
            mNotificationManager.notify(id, builder.build());
        }
    }

    public void cancelNotification(int id) {
        mNotificationManager.cancel(id);
    }


    @SuppressWarnings("deprecation")
    private void disableKeyguard() {
        KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
        mKL.disableKeyguard();
    }

    @SuppressWarnings("deprecation")
    private void enableKeyguard() {
        KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
        mKL.reenableKeyguard();
    }

    /** Handle button clicks
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                || (keyCode == KeyEvent.KEYCODE_POWER)
                || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                || (keyCode == KeyEvent.KEYCODE_CAMERA)) {
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {

            return true;
        }

        return false;

    }

    /** handle the key press events here itself
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP
                || (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
                || (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
            return false;
        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {

            return true;
        }
        return false;
    }

    public void lock() {
        LockscreenUtils.lock(Home.this);
    }

    public void unlockButton() {
        LockscreenUtils.unlock();
    }

    /** unlock device when home button is successfully unlocked
     *
     * @param isLocked
     */
    @Override
    public void onLockStatusChanged(boolean isLocked) {
        if (!isLocked) {
            unlockDevice();
        }
    }

    private void unlockDevice() {
        finish();
    }
}
