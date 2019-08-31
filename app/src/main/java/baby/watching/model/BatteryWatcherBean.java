package baby.watching.model;

import android.os.BatteryManager;

import java.io.Serializable;
import java.util.Arrays;

public class BatteryWatcherBean implements Serializable {
    int level;
    int temp;
    int voltage;
    int status;
    int charge_plug;
    int battery_health_int;
    boolean power_lugged;
    String battery_status = "no_data";
    String battery_power_source = "no_data";
    String battery_health = "no_data";

    public boolean isPower_lugged() {
        return power_lugged;
    }

    public void setPower_lugged(boolean power_lugged) {
        this.power_lugged = power_lugged;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCharge_plug() {
        return charge_plug;
    }

    public void setCharge_plug(int charge_plug) {
        this.charge_plug = charge_plug;
    }

    public int getBattery_health_int() {
        return battery_health_int;
    }

    public void setBattery_health_int(int battery_health_int) {
        this.battery_health_int = battery_health_int;
    }

    public String getBattery_status() {
        return battery_status;
    }

    public void setBattery_status(String battery_status) {
        this.battery_status = battery_status;
    }

    public String getBattery_power_source() {
        return battery_power_source;
    }

    public void setBattery_power_source(String battery_power_source) {
        this.battery_power_source = battery_power_source;
    }

    public String getBattery_health() {
        return battery_health;
    }

    public void setBattery_health(String battery_health) {
        this.battery_health = battery_health;
    }

    @Override
    public String toString() {
        return "BatteryWatcherBean{" +
                "level=" + level +
                ", temp=" + temp +
                ", voltage=" + voltage +
                ", status=" + status +
                ", charge_plug=" + charge_plug +
                ", battery_health_int=" + battery_health_int +
                ", power_lugged=" + power_lugged +
                ", battery_status='" + battery_status + '\'' +
                ", battery_power_source='" + battery_power_source + '\'' +
                ", battery_health='" + battery_health + '\'' +
                '}';
    }

    public String showDetail() {
        String detail = "<b> Level  : </b><br/>" + level + " <br/>";
        if (temp != 0) {
            detail = detail + "<b> Temp : </b><br/>" + temp + " <br/>";
        }
        if (voltage != 0) {
            detail = detail + "<b> Voltage : </b><br/>" + voltage + " <br/>";
        }
        if (status != 0) {
            detail = detail + "<b> Status : </b><br/>" + status + " <br/>";
        }
        if (charge_plug != 0) {
            detail = detail + "<b>Charge Plug : </b><br/>" + charge_plug + " <br/>";
        }
        if (battery_health_int != 0) {
            detail = detail + "<b> Battery Health  : </b><br/>" + battery_health_int + "<br/>";
        }
        detail = detail + "<b> Power Lugged : </b><br/>" + power_lugged + "<br/>";
        if (battery_status != null) {
            detail = detail + "<b> Battery Status : </b><br/>" + battery_status + "<br/>";
        }
        if (battery_power_source != null) {
            if (!battery_power_source.equals("null")) {
                detail = detail + "<b> Battery Power Source : </b><br/>" + battery_power_source + "<br/>";
            }
        }
        if (battery_health != null) {
            if (!battery_health.equals("null")) {
                detail = detail + "<b> Battery Health : </b><br/>" + battery_health + "<br/>";
            }
        }
        return detail;
    }

    public String showAllDetail() {
        return "Level : " + level +
                "\n temp : " + temp +
                "\n voltage : " + voltage +
                "\n status : " + status +
                "\n charge_plug : " + charge_plug +
                "\n battery_health_int : " + battery_health_int +
                "\n power_lugged : " + power_lugged +
                "\n battery_status : '" + battery_status + '\'' +
                "\n battery_power_source : '" + battery_power_source + '\'' +
                "\n battery_health : '" + battery_health;
    }
}
