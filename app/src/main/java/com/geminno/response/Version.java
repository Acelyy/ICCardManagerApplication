package com.geminno.response;

/**
 * Created by xu on 2017/7/21.
 */

public class Version {

    /**
     * version_num : 2.2
     * version_url : http://192.168.200.154/appversion/20170721/7e794d55b02a2c9d2a88d71e0bb0548d.apk
     */

    private double version_num;
    private String version_url;

    public double getVersion_num() {
        return version_num;
    }

    public void setVersion_num(double version_num) {
        this.version_num = version_num;
    }

    public String getVersion_url() {
        return version_url;
    }

    public void setVersion_url(String version_url) {
        this.version_url = version_url;
    }
}
