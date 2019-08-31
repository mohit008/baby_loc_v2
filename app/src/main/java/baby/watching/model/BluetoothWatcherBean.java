package baby.watching.model;

import java.io.Serializable;

public class BluetoothWatcherBean implements Serializable {
    String name;
    String mac;
    boolean state;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "BluetoothWatcherBean{" +
                "name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", state=" + state +
                '}';
    }
    public String showDetail() {
        String detail = "<b> Name  : </b><br/>" + name + " <br/>";
        if (mac != null) {
            detail = detail + "<b> Mac : </b><br/>" + mac + "<br/>";
        }
        detail = detail + "<b> State : </b><br/>" + state + "<br/>";

        return detail;
    }

    public String showAllDetail() {
        return "Name : " + name +
                "\n Mac : " + mac +
                "\n state : " + state
                ;
    }
}
