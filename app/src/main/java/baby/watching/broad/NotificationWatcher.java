package baby.watching.broad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
    HomeNotificationStatus homeNotificationStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service Create");
        notificationBeans = new ArrayList<>();

        homeNotificationStatus = new HomeNotificationStatus();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.NOTIFICATION_STATUS_RECEIVER);
        registerReceiver(homeNotificationStatus, filter);

//        requestRebind(new ComponentName("baby.watching", "baby.watching.broad.NotificationWatcher"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homeNotificationStatus!=null)
        unregisterReceiver(homeNotificationStatus);
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
            NotificationBean bean = addNotificationDetail(sbn);
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

    /**
     * send notification detail to activity class
     */

    public void sendNotificationDetail() {
        try {
            notificationBeans = new ArrayList<>();
            StatusBarNotification[] s = this.getActiveNotifications();
            for (StatusBarNotification sbn : s) {
                if (!notificationBeans.contains(sbn.getPackageName())) {
                    notificationBeans.add(addNotificationDetail(sbn));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get application name from package
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


/*    public void checkCount(ArrayList<NotificationBean> notificationBeans) {
        for (int i = 0; i < notificationBeans.size(); i++) {
            NotificationBean bean = notificationBeans.get(i);
            for (int j = notificationBeans.size() - 1; j > i; j--) {
                if (bean.getPackageName().equals(notificationBeans.get(j).getPackageName())) {
                    bean.setCount(bean.getCount() + 1);
                    notificationBeans.remove(j);
                }
            }
        }
    }*/

    class HomeNotificationStatus extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (((String) intent.getExtras().get("status")).equals("null")) {
                    sendNotificationDetail();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    /**
     * collect notification detail
     *
     * @param sbn
     * @return
     */
    public NotificationBean addNotificationDetail(StatusBarNotification sbn) {
        NotificationBean notificationBean = new NotificationBean();
        notificationBean.setPackageName(sbn.getPackageName());
        notificationBean.setId(sbn.getId());
        notificationBean.setTag(sbn.getTag());
        notificationBean.setPostTime(sbn.getPostTime());
        notificationBean.setAppName(getAppName(sbn.getPackageName()));
        notificationBean.setIcon(sbn.getNotification().icon);
        notificationBean.setCount(1);
        notificationBean.setKey(sbn.getKey());
        notificationBean.setTickerText(sbn.getNotification().tickerText + "");
        notificationBean.setSettingText(sbn.getNotification().getSettingsText() + "");

        Bundle extras = sbn.getNotification().extras;

        if (extras.containsKey("android.text")) {
            if (extras.getCharSequence("android.text") != null) {
                notificationBean.setText(extras.getCharSequence("android.text").toString());
            }
        }
        if (extras.containsKey("android.title")) {
            if (extras.getCharSequence("android.title") != null) {
                notificationBean.setTitle(extras.getCharSequence("android.title").toString());
            }
        }
        if (extras.containsKey("android.reduced.images")) {
            notificationBean.setReduced_images(extras.getBoolean("android.reduced.images"));
        }
        if (extras.containsKey("android.subText")) {
            if (extras.getCharSequence("android.subText") != null) {
                notificationBean.setSubText(extras.getCharSequence("android.subText").toString());
            }

        }
        if (extras.containsKey("android.template")) {
            notificationBean.setTemplate(extras.getString("android.template"));
        }
        if (extras.containsKey("android.showChronometer")) {
            notificationBean.setShowChronometer(extras.getBoolean("android.showChronometer"));
        }
        if (extras.containsKey("android.icon")) {
            notificationBean.setIcon_(extras.getInt("android.icon"));
        }
        if (extras.containsKey("android.progress")) {
            notificationBean.setProgress(extras.getInt("android.progress"));
        }
        if (extras.containsKey("android.progressMax")) {
            notificationBean.setProgressMax(extras.getInt("android.progressMax"));
        }
        if (extras.containsKey("android.appInfo")) {
//            if (extras.getParcelable("android.appInfo") != null) {
//                ApplicationInfo b = extras.getParcelable("android.appInfo");
//            }
        }
        if (extras.containsKey("android.picture")) {
//            Bitmap b = BitmapFactory.decodeByteArray(extras.getByteArray("android.picture"), 0, 0);
        }
        if (extras.containsKey("android.showWhen")) {
            notificationBean.setShowWhen(extras.getBoolean("android.showWhen"));
        }
        if (extras.containsKey("android.largeIcon")) {
//            Bitmap b = BitmapFactory.decodeByteArray(extras.getByteArray("android.largeIcon"), 0, 0);
        }
        if (extras.containsKey("android.infoText")) {
            if (extras.getCharSequence("android.infoText") != null) {
                notificationBean.setInfoText(extras.getCharSequence("android.infoText").toString());
            }
        }
        if (extras.containsKey("android.progressIndeterminate")) {
            notificationBean.setProgressIndeterminate(extras.getBoolean("android.progressIndeterminate"));
        }
        if (extras.containsKey("android.remoteInputHistory")) {
            notificationBean.setRemoteInputHistory(extras.getCharArray("android.remoteInputHistory"));
        }
        if (extras.containsKey("android.summaryText")) {
            if (extras.getCharSequence("android.summaryText") != null) {
                notificationBean.setSummaryText(extras.getCharSequence("android.summaryText").toString());
            }
        }
        return notificationBean;
    }

}
