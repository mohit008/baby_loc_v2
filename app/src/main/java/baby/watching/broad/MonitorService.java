package baby.watching.broad;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import baby.watching.R;
import baby.watching.loc.Home;
import baby.watching.model.NotificationBean;
import baby.watching.util.AppConstants;

/**
 * Created by mohit on 28-Mar-17.
 */
public class MonitorService extends Service {

    private static final String TAG = "[MonitorService]";

    //-- sensor
    Context context;
    DisplayReceiver displayReceiver;
    Timer timer;
    TimerTask timeTask;

    //-- cpu
    HashMap<String, String> master = new HashMap<>();
    String[] titles = {"PID", "PR", "CPU%", "S", "#THR", "VSS", "RSS", "PCY", "UID", "Name",
            "User %", "System %", "IOW %", "IRQ %", "Nice", "Sys", "Idle", "IOW", "IRQ", "SIRQ"};
    JSONArray rootArray = new JSONArray();
    JSONObject root = new JSONObject();


    WindowManager windowManager;
    WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this.getApplicationContext();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //-- call display receiver with filter
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);

        //-- register display receiver
        displayReceiver = new DisplayReceiver();
        registerReceiver(displayReceiver, filter);

//        startTimerforTopCPU();
        getDescriptionContent();
        startForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * sent notification that service is running
     */
    private void startForeground() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("default", "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
                mNotificationManager.createNotificationChannel(channel);
            }
            mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                    .setAutoCancel(true);
        } else {
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setAutoCancel(true);
        }
        Notification notification = mBuilder.setOngoing(true)
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText("Running")
                .setSmallIcon(R.drawable.app_icon)
                .setContentIntent(null)
                .setOngoing(true)
                .build();
        startForeground(9999, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
        unregisterReceiver(displayReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * start timer for top command
     */
    public void startTimerforTopCPU() {
        timer = new Timer();
        timeTask = new TimerTask() {
            @Override
            public void run() {
                collectCPUData();
            }
        };
        try {
            timer.schedule(timeTask, 3000, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getError(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String s = sw.toString();
        return s;
    }


    /*public void createJson() {
        AppConstants.NOTIFICATION_JSON = "";
        AppShared locShared = new AppShared();
        StatusBarNotification[] statusBar = MonitorService.this.getActiveNotifications();
        try{
            JSONArray jsonArray = new JSONArray();
            JSONObject root = new JSONObject();
            for(StatusBarNotification sb: statusBar){
                JSONObject pkgOj = new JSONObject();
                pkgOj.put("pkg",sb.getPackageName());
                pkgOj.put("id",sb.getId());
                pkgOj.put("tag",sb.getTag());
//                pkgOj.put("key",sb.getKey());
//                pkgOj.put("groupKey",sb.getGroupKey());
//                pkgOj.put("overrideGroupKey",sb.getOverrideGroupKey());
//                pkgOj.put("initialPid",sb.getInitialPid());
                pkgOj.put("postTime",sb.getPostTime());
                pkgOj.put("name",getAppName(sb.getPackageName()));
                jsonArray.put(pkgOj);
            }
            root.put("notification",jsonArray);
            locShared.manageNotification(this,root.toString(),"clear");
            locShared.manageNotification(this,root.toString(),"create");
            AppConstants.NOTIFICATION_JSON = root.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }*/

    /*private void log(String msg) {
        Log.i(TAG, msg);
    }*/

    /**
     * get application name from package
     *
     * @param pkg
     * @return
     */
    public String getAppName(String pkg) {
        try {
            PackageManager manager = context.getPackageManager();
            ApplicationInfo applicationInfo = manager
                    .getApplicationInfo(pkg, PackageManager.GET_META_DATA);
            return (String) manager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * check count of notification
     *
     * @param notificationBeans
     */
    public void checkCount(ArrayList<NotificationBean> notificationBeans) {
        for (int i = 0; i < notificationBeans.size(); i++) {
            NotificationBean bean = notificationBeans.get(i);
            for (int j = notificationBeans.size() - 1; j > i; j--) {
                if (bean.getPackageName().equals(notificationBeans.get(j).getPackageName())) {
                    bean.setCount(bean.getCount() + 1);
                    notificationBeans.remove(j);
                }
            }
        }
    }

    /**
     * collectCPUData 'top' data of process
     */
    public void collectCPUData() {
        String output = null;
        try {
            Process process = Runtime.getRuntime().exec("top -m 10 -n 1");
            InputStream in = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            StringBuilder builder = new StringBuilder();
            while (bufferedReader.readLine() != null) {
                builder.append(bufferedReader.readLine() + "\n");
            }
            output = builder.toString();
            Log.i("Exec_Service", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] str = output.split("\n");
        ArrayList<String[]> gts = new ArrayList<>();
        for (String s : str) {
            if (s.isEmpty()) {
                continue;
            } else {
                if (s.contains(",")) {
                    // head
                    String[] head = s.split(",");
                    gts.add(head);
                } else {
                    if (!s.equals("null")) {
                        String[] a = s.split(" ");
                        for (int i = 0; i < a.length; i++) {
                            if (a[i].equals("")) {
                                a[i] = null;
                            }
                        }
                        gts.add(a);
                    }
                }
            }
        }
        createJson(gts);

    }

    /**
     * create json of raw data
     *
     * @param arrayList
     */
    public void createJson(ArrayList<String[]> arrayList) {
        try {

            //--head
            String[] head = arrayList.get(0);
            JSONArray headArray = new JSONArray();
            JSONObject headObject = new JSONObject();
            for (int i = 0; i < head.length; i++) {
                JSONObject headObj = new JSONObject();
                headObj.put("title", head[i]);
                headArray.put(headObj);
            }
            headObject.put("head", headArray);
            rootArray.put(headObject);

            //--content
            JSONArray dataArray = new JSONArray();
            JSONObject ContentObject = new JSONObject();
            arrayList.remove(null);
            for (int i = 1; i < arrayList.size(); i++) {
                String[] ar = getString(arrayList.get(i));
                JSONObject pkgOj = new JSONObject();
                pkgOj.put("PID", ar[0]);
                pkgOj.put("PR", ar[1]);
                pkgOj.put("CPU%", ar[2]);
                pkgOj.put("S", ar[3]);
                pkgOj.put("#THR", ar[4]);
                pkgOj.put("VSS", ar[5]);
                pkgOj.put("RSS", ar[6]);
                pkgOj.put("PCY", ar[7]);
                pkgOj.put("UID", ar[8]);
                pkgOj.put("Name", ar[9]);
                dataArray.put(pkgOj);
            }
            ContentObject.put("content", dataArray);
            rootArray.put(ContentObject);

            //--description
            JSONObject obj_Detail = new JSONObject();
            JSONArray object_DetailArray = new JSONArray();

            for (int i = 0; i <= master.size() - 1; i++) {
                JSONObject objectDetail = new JSONObject();
                objectDetail.put("definition", master.get(titles[i]));
                objectDetail.put("title", titles[i]);
                object_DetailArray.put(objectDetail);
            }
            obj_Detail.put("description", object_DetailArray);
            rootArray.put(obj_Detail);

            root.put("root", rootArray);
            Log.i("JSON", root.toString());

            broadcastCPU(root.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * update array
     *
     * @param strings
     * @return
     */
    public String[] getString(String[] strings) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str : strings) {
            if (str != null) {
                arrayList.add(str);
            }
        }
        if (arrayList.size() == 9) {
            arrayList.add(7, " ");
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }


    /**
     * sent brightness msg to activity
     *
     * @param value
     */
    public void broadcastCPU(String value) {
        sendBroadcast(new Intent()
                .setAction(AppConstants.TOP_CPU_DATA)
                .putExtra("value", value));
    }

    public void getDescriptionContent() {
        master.put("PID", "Process ID");
        master.put("PR", "Priority of the process ");
        master.put("CPU%", "CPU Usage");
        master.put("S", "State (or possibly status) R=Running, S=Sleeping");
        master.put("#THR", "Number of threads");
        master.put("VSS", "Virtual Set Size : Indicates how much virtual memory is associated with the process.");
        master.put("RSS", "Resident Set Size : Indicates how many physical pages are associated with the process.");
        master.put("PCY", "Policy -- Determines how an app should be treated by Android's memory. FG : Foreground, BG : Background");
        master.put("UID", "Name of the user that started the task");
        master.put("Name", "Name of Process");
        master.put("User %", "Percentage of the CPU for user processes");
        master.put("System %", "Percentage of the CPU for system processes");
        master.put("IOW %", "Percentage of the CPU on Input Output Wait");
        master.put("IRQ %", "Percentage of the CPU time spent servicing/handling hardware Interrupt Request");
        master.put("Nice", "Percentage of CPU time spent on low priority processes and been niced.");
        master.put("Sys", "CPU for system processes. The  time  the  CPU  has  spent  running  the  kernel  and  its processes.");
        master.put("Idle", "CPU time spent idle");
        master.put("IOW", "Input Output Wait");
        master.put("IRQ", "CPU time spent servicing/handling hardware Interrupt Requests");
        master.put("SIRQ", "CPU time spent servicing/handling software Interrupt Requests");
    }

    public void addWindow(String msg,String title) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                 WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, for <23
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.x = 0;
        params.y = 0;
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.error_window, null);
        TextView tvErrortext = (TextView) view.findViewById(R.id.tvErrortext);
        Button btDialogOk = (Button) view.findViewById(R.id.btDialogOk);
        tvErrortext.setBackgroundColor(Color.WHITE);
        tvErrortext.setTextColor(Color.BLACK);
        tvErrortext.setText(title +"\n" + msg);

        btDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeViewImmediate(view);
            }
        });
        windowManager.addView(view, params);
    }
}
