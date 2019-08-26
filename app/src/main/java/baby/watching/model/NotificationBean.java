package baby.watching.model;

import android.app.Notification;

import java.io.Serializable;

/**
 * Created by mohit.soni on 29-Dec-17.
 */

public class NotificationBean implements Serializable {

    String packageName;
    int Id;
    String Tag;
    long postTime;
    String appName;
    String key;
    int icon;
    int count;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "NotificationBean{" +
                "packageName='" + packageName + '\'' +
                ", Id='" + Id + '\'' +
                ", Tag='" + Tag + '\'' +
                ", postTime='" + postTime + '\'' +
                ", appName='" + appName + '\'' +
                ", count=" + count +
                '}';
    }


}
