package baby.watching.model;

import java.io.Serializable;

public class WifiWatcherBean implements Serializable {
    String ssid;
    String bssid;
    String mac;
    int ipAddress;
    String supplicant_state;
    int rssi;
    int link_speed;
    int frequency;
    int net_id;
    String metered_hint;
    String score;
    String status;

    MobileDataWatcherBean mobileDataWatcherBean;

    public MobileDataWatcherBean getMobileDataWatcherBean() {
        return mobileDataWatcherBean;
    }

    public void setMobileDataWatcherBean(MobileDataWatcherBean mobileDataWatcherBean) {
        this.mobileDataWatcherBean = mobileDataWatcherBean;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(int ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSupplicant_state() {
        return supplicant_state;
    }

    public void setSupplicant_state(String supplicant_state) {
        this.supplicant_state = supplicant_state;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getLink_speed() {
        return link_speed;
    }

    public void setLink_speed(int link_speed) {
        this.link_speed = link_speed;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getNet_id() {
        return net_id;
    }

    public void setNet_id(int net_id) {
        this.net_id = net_id;
    }

    public String getMetered_hint() {
        return metered_hint;
    }

    public void setMetered_hint(String metered_hint) {
        this.metered_hint = metered_hint;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String showDetail() {
        String detail = "<b> SSID  : </b><br/>" + ssid + " <br/>";
        if (bssid != null) {
            if (!bssid.equals("null")) {
                detail = detail + "<b> BSSID : </b><br/>" + bssid + " <br/>";
            }
        }
        if (mac != null) {
            if (!mac.equals("null")) {
                detail = detail + "<b> MAC : </b><br/>" + mac + " <br/>";
            }
        }
        if (supplicant_state != null) {
            if (!supplicant_state.equals("null")) {
                detail = detail + "<b> Supplicant State : </b><br/>" + supplicant_state + " <br/>";
            }
        }
        if (ipAddress != 0) {
            detail = detail + "<b> IP Address  : </b><br/>" + ipAddress + "<br/>";
        }
        if (rssi != 0) {
            detail = detail + "<b> RSSI  : </b><br/>" + rssi + "<br/>";
        }
        if (link_speed != 0) {
            detail = detail + "<b> Link Speed  : </b><br/>" + link_speed + "<br/>";
        }
        if (frequency != 0) {
            detail = detail + "<b> Frequency  : </b><br/>" + frequency + "<br/>";
        }
        if (net_id != 0) {
            detail = detail + "<b> Net Id  : </b><br/>" + net_id + "<br/>";
        }
        if (metered_hint != null) {
            if (!metered_hint.equals("null")) {
                detail = detail + "<b> Metered Hint : </b><br/>" + metered_hint + " <br/>";
            }
        }
        if (score != null) {
            if (!score.equals("null")) {
                detail = detail + "<b> Score : </b><br/>" + score + " <br/>";
            }
        }
        if (status != null) {
            if (!status.equals("null")) {
                detail = detail + "<b> Status : </b><br/>" + status + " <br/>";
            }
        }
        return detail;
    }


    @Override
    public String toString() {
        return "WifiWatcherBean{" +
                "ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", mac='" + mac + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", supplicant_state='" + supplicant_state + '\'' +
                ", rssi=" + rssi +
                ", link_speed=" + link_speed +
                ", frequency=" + frequency +
                ", net_id=" + net_id +
                ", metered_hint='" + metered_hint + '\'' +
                ", score='" + score + '\'' +
                ", status=" + status +
                ", mobileDataWatcherBean=" + mobileDataWatcherBean.toString() +
                '}';
    }


    public String showAllDetail() {
        return
                "ssid : " + ssid +
                        "\n bssid : " + bssid +
                        "\n mac : " + mac +
                        "\n ipAddress : " + ipAddress +
                        "\n supplicant_state : " + supplicant_state +
                        "\n rssi : " + rssi +
                        "\n link_speed : " + link_speed +
                        "\n frequency : " + frequency +
                        "\n net_id : " + net_id +
                        "\n metered_hint : " + metered_hint +
                        "\n score : " + score +
                        "\n status : " + status +
                        "\n mobileDataWatcherBean : " + mobileDataWatcherBean.toString();
    }
}
