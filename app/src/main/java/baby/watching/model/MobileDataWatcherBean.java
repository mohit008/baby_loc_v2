package baby.watching.model;

import java.io.Serializable;

public class MobileDataWatcherBean implements Serializable {

    String type;
    boolean state;
    String reason;
    String extra;
    String sub_type_name;
    int sub_type;
    boolean fail_over;
    boolean available;
    boolean roaming;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getSub_type_name() {
        return sub_type_name;
    }

    public void setSub_type_name(String sub_type_name) {
        this.sub_type_name = sub_type_name;
    }

    public int getSub_type() {
        return sub_type;
    }

    public void setSub_type(int sub_type) {
        this.sub_type = sub_type;
    }

    public boolean getFail_over() {
        return fail_over;
    }

    public void setFail_over(boolean fail_over) {
        this.fail_over = fail_over;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean getRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }


    @Override
    public String toString() {
        return "MobileDataWatcherBean{" +
                "type='" + type + '\'' +
                ", state='" + state + '\'' +
                ", reason='" + reason + '\'' +
                ", extra='" + extra + '\'' +
                ", sub_type_name='" + sub_type_name + '\'' +
                ", sub_type=" + sub_type +
                ", fail_over='" + fail_over + '\'' +
                ", available='" + available + '\'' +
                ", roaming='" + roaming + '\'' +
                '}';
    }

    public String showDetail() {
        String detail = "<b> type  : </b><br/>" + type + " <br/>";
        detail = detail + "<b> Temp : </b><br/>" + state + " <br/>";
        if (reason != null) {
            if (!reason.equals("null")) {
                detail = detail + "<b> Reason : </b><br/>" + reason + " <br/>";
            }
        }
        if (extra != null) {
            if (!extra.equals("null")) {
                detail = detail + "<b> Extra : </b><br/>" + extra + " <br/>";
            }
        }
        if (sub_type_name != null) {
            if (!sub_type_name.equals("null")) {
                detail = detail + "<b> Sub Type Name : </b><br/>" + sub_type_name + " <br/>";
            }
        }
        if (sub_type != 0) {
            detail = detail + "<b> Sub Type  : </b><br/>" + sub_type + "<br/>";
        }
        detail = detail + "<b> Fail Over : </b><br/>" + fail_over + "<br/>";
        detail = detail + "<b> Available : </b><br/>" + available + "<br/>";
        detail = detail + "<b> Roaming : </b><br/>" + roaming + "<br/>";
        return detail;
    }

    public String showAllDetail() {
        return
                "Type : " + type +
                        "\n state : " + state +
                        "\n reason : " + reason +
                        "\n extra : " + extra +
                        "\n sub_type_name : " + sub_type_name +
                        "\n sub_type=" + sub_type +
                        "\n fail_over : " + fail_over +
                        "\n available : " + available +
                        "\n roaming : " + roaming;
    }
}
