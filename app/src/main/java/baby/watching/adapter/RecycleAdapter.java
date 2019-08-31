package baby.watching.adapter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import baby.watching.R;
import baby.watching.interfacing.AppInfoDialog;
import baby.watching.model.NotificationBean;
import baby.watching.util.AppConstants;
import baby.watching.view.MyProgressView;

import static baby.watching.util.AppConstants.TOGGLE;

/**
 * Created by mohit.soni on 27-Dec-17.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private static String TAG = "RecycleAdapter";
    List<NotificationBean> data;
    Context context;
//    HashMap<Integer, Boolean> checkAnimation = new HashMap<>();
    NotificationManager mNotificationManager;
    boolean islongPress = false;
    AppInfoDialog appInfoDialog;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView noti_icon;
        MyProgressView myProgressView1;
        RelativeLayout rlBadge, rlNotificationMain;
        TextView tvBadge;

        public ViewHolder(View v) {
            super(v);
            noti_icon = (ImageView) v.findViewById(R.id.noti_icon);
            myProgressView1 = (MyProgressView) v.findViewById(R.id.arc_one);
            rlBadge = (RelativeLayout) v.findViewById(R.id.rlBadge);
            rlNotificationMain = (RelativeLayout) v.findViewById(R.id.rlNotificationMain);
            tvBadge = (TextView) v.findViewById(R.id.tvBadge);
        }
    }

    public RecycleAdapter(Context context, List<NotificationBean> data,AppInfoDialog  appInfoDialog) {
        this.data = data;
        this.context = context;
        this.appInfoDialog = appInfoDialog;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        checkCount(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_icon, parent, false);
        return new ViewHolder(v);
    }

    public void addBean(NotificationBean  bean){
        data.add(data.size()-1,bean);
        checkCount(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NotificationBean statusBarNotification = data.get(position);

        try {
           if(statusBarNotification != null){
               final Drawable icon = context.getPackageManager().getApplicationIcon(statusBarNotification.getPackageName());
               holder.noti_icon.setBackground(icon);

               if (statusBarNotification.getCount() > 1) {
                   holder.rlBadge.setVisibility(View.VISIBLE);
                   holder.tvBadge.setText(statusBarNotification.getCount() + "");
               }

               holder.myProgressView1.setStartingDegree(270);
               holder.myProgressView1.setStrokeWidth(2);

//            animateProgress(holder.myProgressView1, 100);

               // animate icon
               Animation ani = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.fade_in);
               ani.setDuration(AppConstants.ANIMATION_SPEED + 1000);
               ani.setInterpolator(new AccelerateInterpolator());
               holder.noti_icon.setAnimation(ani);

               holder.rlNotificationMain.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
//                    if (pre_time != 0 && post_time != 0) {
//                        Toast.makeText(context, statusBarNotification.getPackageName(), Toast.LENGTH_SHORT).show();
//                        if (post_time - pre_time < 175) {
//                            try {
//                                String cmd = "adb shell am kill " + statusBarNotification.getPackageName();
//                                Process process = Runtime.getRuntime().exec(cmd);
//                                process.waitFor();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            } catch (IOException r) {
//                                r.printStackTrace();
//                            }
//                        }
//                        log(post_time - pre_time + "");
//                        pre_time = post_time = 0;
//
//                    }
                       if (!islongPress) {
                           Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(statusBarNotification.getPackageName());
                           context.startActivity(launchIntent);
                           mNotificationManager.cancel(statusBarNotification.getTag(), statusBarNotification.getId());
                       }
                   }
               });
               holder.rlNotificationMain.setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View v) {
                       islongPress = true;
                       appInfoDialog.onItemCheck(statusBarNotification,TOGGLE,"");
                       return false;
                   }
               });
           }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public void log(String msg) {
        Log.i(TAG, msg);
    }

//    public void s() {
//        List<String> nameList = new ArrayList<>();
//        List<Integer> idList = new ArrayList<>();
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> pidsTask = activityManager.getRunningAppProcesses();
//
//        for (int i = 0; i < pidsTask.size(); i++) {
//            nameList.add(pidsTask.get(i).processName);
//            idList.add(pidsTask.get(i).uid);
//        }
//    }

    public void checkCount(List<NotificationBean> notificationBeans) {
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
}
