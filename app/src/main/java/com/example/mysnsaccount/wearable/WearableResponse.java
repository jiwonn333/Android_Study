package com.example.mysnsaccount.wearable;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "wearabledevice")
public class WearableResponse {
    @PropertyElement(name = "error")
    private String error;
    @PropertyElement(name = "registered")
    private String registered;
    @PropertyElement(name = "other_push_status")
    private String other_push_status;


    @PropertyElement(name = "phone")
    private String phone;
    @PropertyElement(name = "wearable")
    private String wearable;
    @PropertyElement(name = "mode")
    private String mode;
    @PropertyElement(name = "announce")
    private String announce;


    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public void setOther_push_status(String other_push_status) {
        this.other_push_status = other_push_status;
    }

    public String getRegistered() {
        return registered;
    }

    public String getOther_push_status() {
        return other_push_status;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

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
