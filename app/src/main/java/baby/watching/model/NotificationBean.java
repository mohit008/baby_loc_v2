package baby.watching.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by mohit.soni on 29-Dec-17.
 */

public class NotificationBean implements Serializable {

    String packageName;
    int Id;
    String tag;
    long postTime;
    String appName;
    String key;
    int icon;
    int count;
    String tickerText;
    String settingText;

    String text;
    String title;
    boolean reduced_images;
    String subText;
    String template;
    boolean showChronometer;
    int icon_;
    int progress;
    int progressMax;
    boolean showWhen;
    String infoText;
    boolean progressIndeterminate;
    char[] remoteInputHistory;
    String summaryText;


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
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTickerText() {
        return tickerText;
    }

    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    public String getSettingText() {
        return settingText;
    }

    public void setSettingText(String settingText) {
        this.settingText = settingText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isReduced_images() {
        return reduced_images;
    }

    public void setReduced_images(boolean reduced_images) {
        this.reduced_images = reduced_images;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String isTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isShowChronometer() {
        return showChronometer;
    }

    public void setShowChronometer(boolean showChronometer) {
        this.showChronometer = showChronometer;
    }

    public int getIcon_() {
        return icon_;
    }

    public void setIcon_(int icon_) {
        this.icon_ = icon_;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgressMax() {
        return progressMax;
    }

    public void setProgressMax(int progressMax) {
        this.progressMax = progressMax;
    }

    public boolean isShowWhen() {
        return showWhen;
    }

    public void setShowWhen(boolean showWhen) {
        this.showWhen = showWhen;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public boolean isProgressIndeterminate() {
        return progressIndeterminate;
    }

    public void setProgressIndeterminate(boolean progressIndeterminate) {
        this.progressIndeterminate = progressIndeterminate;
    }

    public char[] getRemoteInputHistory() {
        return remoteInputHistory;
    }

    public void setRemoteInputHistory(char[] remoteInputHistory) {
        this.remoteInputHistory = remoteInputHistory;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    @Override
    public String toString() {
        return "NotificationBean{" +
                "packageName='" + packageName + '\'' +
                ", Id=" + Id +
                ", tag='" + tag + '\'' +
                ", postTime=" + postTime +
                ", appName='" + appName + '\'' +
                ", key='" + key + '\'' +
                ", icon=" + icon +
                ", count=" + count +
                ", tickerText='" + tickerText + '\'' +
                ", settingText='" + settingText + '\'' +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                ", reduced_images=" + reduced_images +
                ", subText='" + subText + '\'' +
                ", template=" + template +
                ", showChronometer=" + showChronometer +
                ", icon_=" + icon_ +
                ", progress=" + progress +
                ", progressMax=" + progressMax +
                ", showWhen=" + showWhen +
                ", infoText='" + infoText + '\'' +
                ", progressIndeterminate=" + progressIndeterminate +
                ", remoteInputHistory=" + Arrays.toString(remoteInputHistory) +
                ", summaryText='" + summaryText + '\'' +
                '}';
    }

    public String showDetail(){
        String detail = "<b> Package Name  : </b><br/>" + packageName + " <br/>";
        if(tag !=null){
            if(!tag.equals("null")){
                detail = detail + "<b> Tag : </b><br/>" + tag + " <br/>";
            }
        }
        if(appName !=null){
            if(!appName.equals("null")){
                detail = detail + "<b> App Name : </b><br/>" + appName + " <br/>";
            }
        }
        if(tickerText !=null){
            if(!tickerText.equals("null")){
                detail = detail + "<b> Ticker Text : </b><br/>" + tickerText + " <br/>";
            }
        }
        if(settingText !=null){
            if(!settingText.equals("null")){
                detail = detail + "<b> Setting Text : </b><br/>" + settingText + " <br/>";
            }
        }
        if(text !=null ){
            if(!text.equals("null")){
                detail = detail + "<b> Text : </b><br/>" + text + "<br/>";
            }
        }
        if(title !=null){
            if(!title.equals("null")){
                detail = detail + "<b> Title : </b><br/>" + title + "<br/>";
            }
        }
        if(subText !=null){
            if(!subText.equals("null")){
                detail = detail + "<b> Sub Text : </b><br/>" + subText + "<br/>";
            }
        }
        if(infoText !=null){
            if(!infoText.equals("null")){
                detail = detail + "<b> Info Text : </b><br/>" + infoText + "<br/>";
            }
        }
        if(summaryText !=null){
            if(!summaryText.equals("null")){
                detail = detail + "<b> Summary Text : </b><br/>" + summaryText + "<br/>";
            }
        }
        return  detail ;
    }

    public String showAllDetail(){
        return  "Package Name  : " + packageName +
                "\n Id : " + Id +
                "\n tag : " + tag +
                "\n postTime : " + postTime +
                "\n appName : " + appName +
                "\n key : " + key +
                "\n icon : " + icon +
                "\n count : " + count +
                "\n tickerText : " + tickerText +
                "\n settingText : " + settingText +
                "\n text : " + text +
                "\n title : " + title +
                "\n reduced_images : " + reduced_images +
                "\n subText : " + subText +
                "\n template : " + template +
                "\n showChronometer : " + showChronometer +
                "\n icon_ : " + icon_ +
                "\n progress : " + progress +
                "\n progressMax : " + progressMax +
                "\n showWhen : " + showWhen +
                "\n infoText : " + infoText +
                "\n progressIndeterminate : " + progressIndeterminate +
                "\n remoteInputHistory : " + Arrays.toString(remoteInputHistory) +
                "\n summaryText : " + summaryText ;
    }
}
