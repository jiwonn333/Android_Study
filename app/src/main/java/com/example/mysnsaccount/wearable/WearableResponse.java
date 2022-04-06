package com.example.mysnsaccount.wearable;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "wearabledevice")
public class WearableResponse {
    @PropertyElement(name = "error")
    private String error;
    @PropertyElement(name = "phone")
    private String phone;
    @PropertyElement(name = "wearable")
    private String wearable;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWearable() {
        return wearable;
    }

    public void setWearable(String wearable) {
        this.wearable = wearable;
    }
}
