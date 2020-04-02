package com.baosight.df.license.entity;

/**
 * Created by xutingting on 2017/6/13.
 */

public class LicenseEntry {
    private String version;
    private String buildDate;
    private String baseLine;
    private String deliveryPkg;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getBaseLine() {
        return baseLine;
    }

    public void setBaseLine(String baseLine) {
        this.baseLine = baseLine;
    }

    public String getDeliveryPkg() {
        return deliveryPkg;
    }

    public void setDeliveryPkg(String deliveryPkg) {
        this.deliveryPkg = deliveryPkg;
    }


}
