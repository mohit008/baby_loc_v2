package baby.watching.broad;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;

import baby.watching.model.NotificationBean;
import baby.watching.util.AppConstants;

public class NotificationWatcher extends NotificationListenerService {
    private static final String TAG = "[NotificationWatcher]";
    ArrayList<NotificationBean> notificationBeans = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service Create");
        notificationBeans = new ArrayList<>();

//        requestRebind(new ComponentName("baby.watching", "baby.watching.broad.NotificationWatcher"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestUnbind();
        Log.i(TAG, "Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        watchNotification(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        watchNotification(sbn);
    }

    public void watchNotification(StatusBarNotification sbn) {
        try {
            Log.i(TAG, "========== NotificationRemoved :: ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
            getNewNotificationDetail(sbn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.i(TAG, "Listener Connected");
        sendNotificationDetail();
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.i(TAG, "Listener DisConnected");
    }

    @Override
    public void onListenerHintsChanged(int hints) {
        super.onListenerHintsChanged(hints);
        Log.i(TAG, "Listener HintsChanged : " + "" + hints);
    }

    public void getNewNotificationDetail(StatusBarNotification sbn) {
        try {
            NotificationBean bean = new NotificationBean();
            bean.setPackageName(sbn.getPackageName());
            bean.setId(sbn.getId());
            bean.setTag(sbn.getTag());
            bean.setPostTime(sbn.getPostTime());
            bean.setAppName(getAppName(sbn.getPackageName()));
            bean.setIcon(sbn.getNotification().icon);
            bean.setCount(1);
            bean.setKey(sbn.getKey());

            String state = "";
            if (AppConstants.isActivityRunning(this)) {
                state = "Running \n";
                //-- sent notification detail to activity
                log(bean.toString());
                Intent intent = new Intent(AppConstants.NOTIFICATION_RECEIVER);
                intent.putExtra("status", "new");
                intent.putExtra("Bean", bean);
                sendBroadcast(intent);
            } else {
                state = "Stop \n";
            }
        } catch (Exception e) {
        }
    }

    /** send notification detail to activity class
     *
     */

    public void sendNotificationDetail() {
        try {
            notificationBeans = new ArrayList<>();
            StatusBarNotification[] s = this.getActiveNotifications();
            for (StatusBarNotification sbn : s) {
                if (!notificationBeans.contains(sbn.getPackageName())) {
                    NotificationBean bean = new NotificationBean();
                    bean.setPackageName(sbn.getPackageName());
                    bean.setId(sbn.getId());
                    bean.setTag(sbn.getTag());
                    bean.setPostTime(sbn.getPostTime());
                    bean.setAppName(getAppName(sbn.getPackageName()));
                    bean.setIcon(sbn.getNotification().icon);
                    bean.setCount(1);
                    bean.setKey(sbn.getKey());
                    notificationBeans.add(bean);
                }
            }
            String state = "";
            if (AppConstants.isActivityRunning(this)) {
                state = "Running \n";
                //-- sent notification detail to activity
                log(notificationBeans.toString());
                Intent intent = new Intent(AppConstants.NOTIFICATION_RECEIVER);
                intent.putExtra("status", "all");
                intent.putExtra("notificationBeans", notificationBeans);
                sendBroadcast(intent);
            } else {
                state = "Stop \n";
            }
//            state = state + "\n" + notificationBeans.get(0).getAppName();
//            Toast.makeText(this, state, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }

    /** get application name from package
     *
     * @param pkg
     * @return
     */


    public String getAppName(String pkg) {
        try {
            PackageManager manager = getPackageManager();
            ApplicationInfo applicationInfo = manager
                    .getApplicationInfo(pkg, PackageManager.GET_META_DATA);
            return (String) manager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


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


    private void log(String msg) {
        Log.i(TAG, msg);
    }

}
