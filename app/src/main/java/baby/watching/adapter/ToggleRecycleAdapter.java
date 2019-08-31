package baby.watching.adapter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import baby.watching.R;
import baby.watching.interfacing.AppInfoDialog;
import baby.watching.model.BatteryWatcherBean;
import baby.watching.model.BluetoothWatcherBean;
import baby.watching.model.MobileDataWatcherBean;
import baby.watching.model.WifiWatcherBean;
import baby.watching.view.MyProgressView;

import static baby.watching.util.AppConstants.BATTERY;
import static baby.watching.util.AppConstants.BLUETOOTH;
import static baby.watching.util.AppConstants.ENABLE;
import static baby.watching.util.AppConstants.MOBILE;
import static baby.watching.util.AppConstants.NETWORK;
import static baby.watching.util.AppConstants.TOGGLE;
import static baby.watching.util.AppConstants.WIFI;
import static baby.watching.util.AppConstants.animateProgress;

/**
 * Created by mohit.soni on 27-Aug-19.
 */

public class ToggleRecycleAdapter extends RecyclerView.Adapter<ToggleRecycleAdapter.ViewHolder> {
    private static String TAG = "ToggleRecycleAdapter";
    Context context;
    ArrayList<ImageView> imageViews = new ArrayList<>();
    boolean layout_done = false;
    HashMap<String, Object> toggleData = new HashMap<>();
    ArrayList<String> toggleKey;
    public static boolean isLongClick = false;
    AppInfoDialog appInfoDialog;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivToggleIcon, ivToggleStatus;

        public ViewHolder(View v) {
            super(v);
            ivToggleIcon = (ImageView) v.findViewById(R.id.ivToggleIcon);
            ivToggleStatus = (ImageView) v.findViewById(R.id.ivToggleStatus);
        }
    }

    public ToggleRecycleAdapter(Context context, HashMap<String, Object> toggleData,AppInfoDialog  appInfoDialog) {
        this.context = context;
        this.toggleData = toggleData;
        this.appInfoDialog = appInfoDialog;
        Set<String> set = toggleData.keySet();
        toggleKey = new ArrayList<>(set);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.toggle_menu,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (!imageViews.contains(holder.ivToggleIcon)) {
            holder.ivToggleIcon.setVisibility(View.INVISIBLE);
            holder.ivToggleIcon.setTag("0");
            imageViews.add(holder.ivToggleIcon);
        } else {
            String s = (String) (imageViews.get(position)).getTag();
            if (s.equals("01")) {
                final Animation ani = AnimationUtils.loadAnimation
                        (context.getApplicationContext(), R.anim.fade_in);
                ani.setDuration(200);
                ani.setInterpolator(new AccelerateInterpolator());
                holder.ivToggleIcon.setVisibility(View.VISIBLE);
                holder.ivToggleIcon.setAnimation(ani);
                holder.ivToggleIcon.setTag(toggleKey.get(position));
                startAnimationProcess(position);
            }
            if (toggleKey.get(position).equals(BATTERY)) {
                BatteryWatcherBean batteryWatcherBean = (BatteryWatcherBean) toggleData.get(BATTERY);
                holder.ivToggleIcon.setBackgroundResource(getBatteryDrawable(batteryWatcherBean.getLevel()));
                holder.ivToggleStatus.setVisibility(batteryWatcherBean.isPower_lugged() ? View.VISIBLE : View.INVISIBLE);
            }
            if (toggleKey.get(position).equals(WIFI)) {
                WifiWatcherBean wifiWatcherBean = (WifiWatcherBean) toggleData.get(WIFI);
                holder.ivToggleIcon.setBackgroundResource(wifiWatcherBean.getStatus().equals(ENABLE) ? R.drawable.wifi_4 : R.drawable.wifi_0);
                holder.ivToggleStatus.setVisibility(wifiWatcherBean.getStatus().equals(ENABLE) ? View.VISIBLE : View.INVISIBLE);
            }
            if (toggleKey.get(position).equals(MOBILE)) {
                MobileDataWatcherBean mobileDataWatcherBean = (MobileDataWatcherBean) toggleData.get(MOBILE);
                holder.ivToggleIcon.setBackgroundResource(mobileDataWatcherBean.getState() ? R.drawable.mobile_data_on : R.drawable.mobile_data_off);
                holder.ivToggleStatus.setVisibility(mobileDataWatcherBean.getState() ? View.VISIBLE : View.INVISIBLE);
            }
            if (toggleKey.get(position).equals(BLUETOOTH)) {
                BluetoothWatcherBean bluetoothWatcherBean = (BluetoothWatcherBean) toggleData.get(BLUETOOTH);
                holder.ivToggleIcon.setBackgroundResource(bluetoothWatcherBean.getState() ? R.drawable.bluetooth_on : R.drawable.bluetooth_disabled);
                holder.ivToggleStatus.setVisibility(bluetoothWatcherBean.getState() ? View.VISIBLE : View.INVISIBLE);
            }

        }
        if (layout_done) {
            holder.ivToggleIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLongClick) {
                        if (v.getTag().equals(BATTERY)) {
                            log(BATTERY);
                        }
                        if (v.getTag().equals(WIFI)) {
                            log(WIFI);
                            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                                wifiManager.setWifiEnabled(false);
                            } else {
                                wifiManager.setWifiEnabled(true);
                            }
                        }
                        if (v.getTag().equals(MOBILE)) {
                            log(MOBILE);
//                            try {
//                                TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
////                                Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
////                                if (null != setMobileDataEnabledMethod) {
////                                    setMobileDataEnabledMethod.invoke(telephonyService, true);
////                                }
//                                telephonyService.setDataEnabled(true);
//                            } catch (Exception ex) {
//                                context.enforceCallingOrSelfPermission(android.Manifest.permission.MODIFY_PHONE_STATE, null);
//                                Log.e(TAG, "Error setting mobile data state", ex);
//                            }
                        }
                        if (v.getTag().equals(BLUETOOTH)) {
                            log(BLUETOOTH);
                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (mBluetoothAdapter.isEnabled()) {
                                mBluetoothAdapter.disable();
                            } else {
                                mBluetoothAdapter.enable();
                            }
                        }
                    }
                }
            });
            holder.ivToggleIcon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isLongClick = true;
                    if (v.getTag().equals(BATTERY)) {
                        log(BATTERY);
                        BatteryWatcherBean batteryWatcherBean = (BatteryWatcherBean) toggleData.get(BATTERY);
                        appInfoDialog.onItemCheck(batteryWatcherBean,TOGGLE,BATTERY);
                    }
                    if (v.getTag().equals(WIFI)) {
                        log(WIFI);
                        WifiWatcherBean wifiWatcherBean = (WifiWatcherBean) toggleData.get(WIFI);
                        appInfoDialog.onItemCheck(wifiWatcherBean,TOGGLE,WIFI);
                    }
                    if (v.getTag().equals(MOBILE)) {
                        log(MOBILE);
                        if(toggleData.containsKey(MOBILE)){
                            MobileDataWatcherBean mobileDataWatcherBean = (MobileDataWatcherBean) toggleData.get(MOBILE);
                            appInfoDialog.onItemCheck(mobileDataWatcherBean,TOGGLE,MOBILE);
                        }
                    }
                    if (v.getTag().equals(BLUETOOTH)) {
                        log(BLUETOOTH);
                        BluetoothWatcherBean bluetoothWatcherBean = (BluetoothWatcherBean) toggleData.get(BLUETOOTH);
                        appInfoDialog.onItemCheck(bluetoothWatcherBean,TOGGLE,BLUETOOTH);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return toggleData.size();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public void log(String msg) {
        Log.i(TAG, msg);
    }

    public void updateTag(int position) {
        imageViews.get(position).setTag("01");
        notifyDataSetChanged();
    }

    /**
     * notify when all animation is done
     */
    public void notifyDone() {
        layout_done = true;
        notifyDataSetChanged();
    }

    /**
     * start animation for all icon
     *
     * @param position
     */
    public void startAnimationProcess(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int x = position + 1;
                if (x < toggleData.size()) {
                    updateTag(x);
                } else {
                    notifyDone();
                }
            }
        }, 300);
    }

    /**
     * get drawable according to level
     *
     * @param level
     * @return
     */
    public int getBatteryDrawable(int level) {
        if (level > 0 && level <= 20) {
            return R.drawable.battery_charging_20;
        }

        if (level > 20 && level <= 50) {
            return R.drawable.battery_charging_50;
        }

        if (level > 50 && level <= 80) {
            return R.drawable.battery_charging_80;
        }

        if (level > 81 && level <= 99) {
            return R.drawable.battery_full;
        }
        return 0;
    }


//    synchronized void waitForAnimationToEnd(final int position) {


    //                Thread t = new Thread() {
//                    public void run() {
//                        waitForAnimationToEnd(position);
//                    }
//                };
//                t.start();
//                new Thread() {
//                    public void run() {
//                        startAnimationForView(holder.ivToggleIcon);
//                    }
//                }.start();
//        try {
//            wait();
//        } catch (Exception e) {
//        }
//        int x = position - 1;
//        if (x >= 0) {
//            updateTag(x);
//        } else {
//            notifyDone();
//        }
//    }
//
//    synchronized void startAnimationForView(ImageView imageView) {
//        final Animation ani = AnimationUtils.loadAnimation
//                (context.getApplicationContext(), R.anim.fade_in);
//        ani.setDuration(400);
//        ani.setInterpolator(new AccelerateInterpolator());
//        imageView.setAnimation(ani);
//        while (true) {
//            boolean ended = ani.hasEnded();
//            if (ended) {
//                log("not ended");
//                break;
//            }
//        }
//        notify();
//    }
}
