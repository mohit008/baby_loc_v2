//package baby.watching.broad;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.graphics.PixelFormat;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Build;
//import android.os.IBinder;
//import android.os.PowerManager;
//import android.service.notification.NotificationListenerService;
//import android.service.notification.StatusBarNotification;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//import android.view.Display;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.TextView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import baby.watching.R;
//import baby.watching.loc.Home;
//import baby.watching.model.NotificationBean;
//import baby.watching.util.AppConstants;
//
///**
// * Created by mohit on 20-aug-2019.
// */
//public class PreMonitorService extends NotificationListenerService implements SensorEventListener {
//
//    private static final String TAG = "[MonitorService]";
//
///*    ArrayList<Integer> pro_values = new ArrayList<>();
//    String pattern = "50";
//    public static int delta = 0, x = 0, y = 0, z = 10, unlock_count = 0;
//    private static long sensor_time_to_accept = 3000;*/
//
//    //-- sensor
//    SensorManager sensorManager;
//    Context context;
//    DisplayReceiver displayReceiver;
//    Timer timer;
//    TimerTask timeTask;
//
//    //-- cpu
//    HashMap<String, String> master = new HashMap<>();
//    String[] titles = {"PID", "PR", "CPU%", "S", "#THR", "VSS", "RSS", "PCY", "UID", "Name",
//            "User %", "System %", "IOW %", "IRQ %", "Nice", "Sys", "Idle", "IOW", "IRQ", "SIRQ"};
//    JSONArray rootArray = new JSONArray();
//    JSONObject root = new JSONObject();
//
//    private PowerManager powerManager;
//    private PowerManager.WakeLock wakeLock;
//    WindowManager windowManager;
//    Display display;
//
//    WindowManager.LayoutParams params;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        context = this.getApplicationContext();
//        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//
//        //-- call display receiver with filter
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
//        filter.addAction("android.intent.action.PHONE_STATE");
//        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
//
//        //-- register sensor
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        if (sensorManager != null) {
//            if (sensorManager.getDefaultSensor(Sensor.TYPE_ALL) != null) {
//                sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
//            }
//        }
//
//        //-- register display receiver
//        displayReceiver = new DisplayReceiver();
//        registerReceiver(displayReceiver, filter);
//
////        startAnimationProcess();
//        getDescriptionContent();
//        startForeground();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }
//
//    /**
//     * sent notification that service is running
//     */
//    private void startForeground() {
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mBuilder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel channel = new NotificationChannel("default", "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
//                channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
//                mNotificationManager.createNotificationChannel(channel);
//            }
//            mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
//                    .setAutoCancel(true);
//        } else {
//            mBuilder = new NotificationCompat.Builder(this);
//            mBuilder.setAutoCancel(true);
//        }
//        Notification notification = mBuilder.setOngoing(true)
//                .setTicker(getResources().getString(R.string.app_name))
//                .setContentText("Running")
//                .setSmallIcon(R.mipmap.app_icon)
//                .setContentIntent(null)
//                .setOngoing(true)
//                .build();
//        startForeground(9999, notification);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.i(TAG, "Service destroyed");
//        sensorManager.unregisterListener(this);
//        unregisterReceiver(displayReceiver);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return super.onBind(intent);
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//    }
//
//    /**
//     * check for sensor value if greater then 6
//     *
//     * @param event
//     */
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        Sensor s = event.sensor;
//
//        //-- if proximity
//        /*if (s.getType() == Sensor.TYPE_PROXIMITY) {
//            Log.i("[MonitorServiceXX]", "type : " + AppConstants.PROXIMITY + " : " + delta + "");
//
//            delta = (int) event.values[0];
//            pattern = pattern + Integer.toString(delta);
//            pro_values.add(delta);
//            //-- check if pattern matches
//            if (Pattern.matches("05", pattern)) {
//                doWakingTask();
//                *//*unlock_count = unlock_count + 1;*//*
//                Log.i(TAG, "type : " + AppConstants.PROXIMITY + " : " + delta + "");
//                pro_values.clear();
//                pattern = "";
//            }
//            //-- skip for first deploy
//            if (pattern.length() == 3) {
//                pattern = "";
//            }
//            delta = 0;
//        }*/
//    }
//
//    public void doWakingTask() {
//        //-- do nothing during call
//        display = windowManager.getDefaultDisplay();
//        if (display.getState() == Display.STATE_OFF) {
//            if (!AppConstants.isActivityRunning(context)) {
//                Intent in = new Intent(context, Home.class);
//                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(in);
//            }
//            wake();
//            /*broadcastIntent(Integer.toString(delta), AppConstants.PROXIMITY);*/
//        }
//        if (display.getState() == Display.STATE_ON) {
//            //it is locked
//        }
//    }
//
//    public void wake() {
//        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(PowerManager
//                .SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "babyLoc:tag");
//        wakeLock.acquire();
//        wakeLock.release();
//    }
//
///*    public void broadcastIntent(String val, String type) {
//        if (AppConstants.isActivityRunning(context.getApplicationContext())) {
//            sendBroadcast(new Intent()
//                    .setAction(AppConstants.CALL_CUSTOM)
//                    .putExtra("val", val)
//                    .putExtra("type", type));
//        }
//    }*/
//
//    /**
//     * check if device is in stable position
//     *
//     * @return
//     */
//    /*public boolean isStable() {
//        return x >= -1 && x <= 1 && y >= -1 && y <= 1 && z == 10;
//    }*/
//
//    /**
//     * start sensor_time_to_accept to check for off signal
//     */
///*    public void startTimerProximity() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (unlock_count == 2) {
////                    Intent in = new Intent(context, Home.class);
////                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    in.putExtra("lock", "lock");
////                    context.startActivity(in);
//                    if(AppConstants.isActivityRunning(context)){
//                        if (AppConstants.IsAdminactive) {
//                            devicePolicyManager.lockNow();
//                        }
//                    }
//                }
//                unlock_count = 0;
//            }
//        }, sensor_time_to_accept);
//    }*/
//
//    /**
//     * start timer for top command
//     */
//    public void startAnimationProcess() {
//        timer = new Timer();
//        timeTask = new TimerTask() {
//            @Override
//            public void run() {
//                collectCPUData();
//            }
//        };
//        try {
//            timer.schedule(timeTask, 3000, 5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onNotificationPosted(StatusBarNotification sbn) {
//        watchNotification(sbn);
//    }
//
//    @Override
//    public void onNotificationRemoved(StatusBarNotification sbn) {
//        watchNotification(sbn);
//    }
//
//    public void watchNotification(StatusBarNotification sbn) {
//        try {
//            Log.i(TAG, "========== NotificationRemoved :: ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
//            sendNotificationDetail();
//        } catch (Exception e) {
//            e.printStackTrace();
//            addWindow(getError(e),"watchNotification");
//        }
//    }
//
//    @Override
//    public void onListenerConnected() {
//        super.onListenerConnected();
////        sendNotificationDetail();
//    }
//
//    /**
//     * send notification detail to activity class
//     */
//    public void sendNotificationDetail() {
//        try {
//            doWakingTask();
//            ArrayList<NotificationBean> notificationBeans = new ArrayList<>();
//            for (StatusBarNotification sbn : PreMonitorService.this.getActiveNotifications()) {
//                if (!notificationBeans.contains(sbn.getPackageName())) {
//                    NotificationBean bean = new NotificationBean();
//                    bean.setPackageName(sbn.getPackageName());
//                    bean.setId(sbn.getId());
//                    bean.setTag(sbn.getTag());
//                    bean.setPostTime(sbn.getPostTime());
//                    bean.setAppName(getAppName(sbn.getPackageName()));
//                    bean.setIcon(sbn.getNotification().icon);
//                    bean.setCount(1);
//                    bean.setKey(sbn.getKey());
//                    notificationBeans.add(bean);
//                }
//            }
//            checkCount(notificationBeans);
//            wake();
//
//            //-- sent notification detail to activity
//            Log.i("NASDNCAINEICEsdfVNEWVW", notificationBeans.toString());
//            Intent intent = new Intent(AppConstants.NOTIFICATION_RECEIVER);
//            intent.putExtra("notification", notificationBeans);
//            sendBroadcast(intent);
//        } catch (Exception e) {
//            addWindow(getError(e),"sendNotificationDetail");
//        }
//    }
//
//    public String getError(Exception ex) {
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//        ex.printStackTrace(pw);
//        String s = sw.toString();
//        return s;
//    }
//
////    public String checkNullString(String object) {
////        if (object == null) {
////            return "";
////        }
////        return object;
////    }
////    public String checkNullint(int object) {
////        if (object == null) {
////            return "";
////        }
////        return object;
////    }
////    public String checkNulllong(long object) {
////        if (object == null) {
////            return "";
////        }
////        return object;
////    }
//
//    /*public void createJson() {
//        AppConstants.NOTIFICATION_JSON = "";
//        AppShared locShared = new AppShared();
//        StatusBarNotification[] statusBar = MonitorService.this.getActiveNotifications();
//        try{
//            JSONArray jsonArray = new JSONArray();
//            JSONObject root = new JSONObject();
//            for(StatusBarNotification sb: statusBar){
//                JSONObject pkgOj = new JSONObject();
//                pkgOj.put("pkg",sb.getPackageName());
//                pkgOj.put("id",sb.getId());
//                pkgOj.put("tag",sb.getTag());
////                pkgOj.put("key",sb.getKey());
////                pkgOj.put("groupKey",sb.getGroupKey());
////                pkgOj.put("overrideGroupKey",sb.getOverrideGroupKey());
////                pkgOj.put("initialPid",sb.getInitialPid());
//                pkgOj.put("postTime",sb.getPostTime());
//                pkgOj.put("name",getAppName(sb.getPackageName()));
//                jsonArray.put(pkgOj);
//            }
//            root.put("notification",jsonArray);
//            locShared.manageNotification(this,root.toString(),"clear");
//            locShared.manageNotification(this,root.toString(),"create");
//            AppConstants.NOTIFICATION_JSON = root.toString();
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//    }*/
//
//    /*private void log(String msg) {
//        Log.i(TAG, msg);
//    }*/
//
//    /**
//     * get application name from package
//     *
//     * @param pkg
//     * @return
//     */
//    public String getAppName(String pkg) {
//        try {
//            PackageManager manager = context.getPackageManager();
//            ApplicationInfo applicationInfo = manager
//                    .getApplicationInfo(pkg, PackageManager.GET_META_DATA);
//            return (String) manager.getApplicationLabel(applicationInfo);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    /**
//     * check count of notification
//     *
//     * @param notificationBeans
//     */
//    public void checkCount(ArrayList<NotificationBean> notificationBeans) {
//        for (int i = 0; i < notificationBeans.size(); i++) {
//            NotificationBean bean = notificationBeans.get(i);
//            for (int j = notificationBeans.size() - 1; j > i; j--) {
//                if (bean.getPackageName().equals(notificationBeans.get(j).getPackageName())) {
//                    bean.setCount(bean.getCount() + 1);
//                    notificationBeans.remove(j);
//                }
//            }
//        }
//    }
//
//    /**
//     * collectCPUData 'top' data of process
//     */
//    public void collectCPUData() {
//        String output = null;
//        try {
//            Process process = Runtime.getRuntime().exec("top -m 10 -n 1");
//            InputStream in = process.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//
//            StringBuilder builder = new StringBuilder();
//            while (bufferedReader.readLine() != null) {
//                builder.append(bufferedReader.readLine() + "\n");
//            }
//            output = builder.toString();
//            Log.i("Exec_Service", output);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String[] str = output.split("\n");
//        ArrayList<String[]> gts = new ArrayList<>();
//        for (String s : str) {
//            if (s.isEmpty()) {
//                continue;
//            } else {
//                if (s.contains(",")) {
//                    // head
//                    String[] head = s.split(",");
//                    gts.add(head);
//                } else {
//                    if (!s.equals("null")) {
//                        String[] a = s.split(" ");
//                        for (int i = 0; i < a.length; i++) {
//                            if (a[i].equals("")) {
//                                a[i] = null;
//                            }
//                        }
//                        gts.add(a);
//                    }
//                }
//            }
//        }
//        createJson(gts);
//
//    }
//
//    /**
//     * create json of raw data
//     *
//     * @param arrayList
//     */
//    public void createJson(ArrayList<String[]> arrayList) {
//        try {
//
//            //--head
//            String[] head = arrayList.get(0);
//            JSONArray headArray = new JSONArray();
//            JSONObject headObject = new JSONObject();
//            for (int i = 0; i < head.length; i++) {
//                JSONObject headObj = new JSONObject();
//                headObj.put("title", head[i]);
//                headArray.put(headObj);
//            }
//            headObject.put("head", headArray);
//            rootArray.put(headObject);
//
//            //--content
//            JSONArray dataArray = new JSONArray();
//            JSONObject ContentObject = new JSONObject();
//            arrayList.remove(null);
//            for (int i = 1; i < arrayList.size(); i++) {
//                String[] ar = getString(arrayList.get(i));
//                JSONObject pkgOj = new JSONObject();
//                pkgOj.put("PID", ar[0]);
//                pkgOj.put("PR", ar[1]);
//                pkgOj.put("CPU%", ar[2]);
//                pkgOj.put("S", ar[3]);
//                pkgOj.put("#THR", ar[4]);
//                pkgOj.put("VSS", ar[5]);
//                pkgOj.put("RSS", ar[6]);
//                pkgOj.put("PCY", ar[7]);
//                pkgOj.put("UID", ar[8]);
//                pkgOj.put("Name", ar[9]);
//                dataArray.put(pkgOj);
//            }
//            ContentObject.put("content", dataArray);
//            rootArray.put(ContentObject);
//
//            //--description
//            JSONObject obj_Detail = new JSONObject();
//            JSONArray object_DetailArray = new JSONArray();
//
//            for (int i = 0; i <= master.size() - 1; i++) {
//                JSONObject objectDetail = new JSONObject();
//                objectDetail.put("definition", master.get(titles[i]));
//                objectDetail.put("title", titles[i]);
//                object_DetailArray.put(objectDetail);
//            }
//            obj_Detail.put("description", object_DetailArray);
//            rootArray.put(obj_Detail);
//
//            root.put("root", rootArray);
//            Log.i("JSON", root.toString());
//
//            broadcastCPU(root.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * update array
//     *
//     * @param strings
//     * @return
//     */
//    public String[] getString(String[] strings) {
//        ArrayList<String> arrayList = new ArrayList<>();
//        for (String str : strings) {
//            if (str != null) {
//                arrayList.add(str);
//            }
//        }
//        if (arrayList.size() == 9) {
//            arrayList.add(7, " ");
//        }
//        return arrayList.toArray(new String[arrayList.size()]);
//    }
//
//
//    /**
//     * sent brightness msg to activity
//     *
//     * @param value
//     */
//    public void broadcastCPU(String value) {
//        sendBroadcast(new Intent()
//                .setAction(AppConstants.TOP_CPU_DATA)
//                .putExtra("value", value));
//    }
//
//    public void getDescriptionContent() {
//        master.put("PID", "Process ID");
//        master.put("PR", "Priority of the process ");
//        master.put("CPU%", "CPU Usage");
//        master.put("S", "State (or possibly status) R=Running, S=Sleeping");
//        master.put("#THR", "Number of threads");
//        master.put("VSS", "Virtual Set Size : Indicates how much virtual memory is associated with the process.");
//        master.put("RSS", "Resident Set Size : Indicates how many physical pages are associated with the process.");
//        master.put("PCY", "Policy -- Determines how an app should be treated by Android's memory. FG : Foreground, BG : Background");
//        master.put("UID", "Name of the user that started the task");
//        master.put("Name", "Name of Process");
//        master.put("User %", "Percentage of the CPU for user processes");
//        master.put("System %", "Percentage of the CPU for system processes");
//        master.put("IOW %", "Percentage of the CPU on Input Output Wait");
//        master.put("IRQ %", "Percentage of the CPU time spent servicing/handling hardware Interrupt Request");
//        master.put("Nice", "Percentage of CPU time spent on low priority processes and been niced.");
//        master.put("Sys", "CPU for system processes. The  time  the  CPU  has  spent  running  the  kernel  and  its processes.");
//        master.put("Idle", "CPU time spent idle");
//        master.put("IOW", "Input Output Wait");
//        master.put("IRQ", "CPU time spent servicing/handling hardware Interrupt Requests");
//        master.put("SIRQ", "CPU time spent servicing/handling software Interrupt Requests");
//    }
//
//    public void addWindow(String msg,String title) {
//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
////                 WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, for <23
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//
//        params.x = 0;
//        params.y = 0;
//        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//        final View view = layoutInflater.inflate(R.layout.error_window, null);
//        TextView tvErrortext = (TextView) view.findViewById(R.id.tvErrortext);
//        Button btDialogOk = (Button) view.findViewById(R.id.btDialogOk);
//        tvErrortext.setBackgroundColor(Color.WHITE);
//        tvErrortext.setTextColor(Color.BLACK);
//        tvErrortext.setText(title +"\n" + msg);
//
//        btDialogOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                windowManager.removeViewImmediate(view);
//            }
//        });
//        windowManager.addView(view, params);
//    }
//}
