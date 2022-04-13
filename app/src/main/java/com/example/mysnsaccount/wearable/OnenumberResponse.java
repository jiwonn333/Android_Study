package com.example.mysnsaccount.wearable;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "onenumber")
public class OnenumberResponse {
    @PropertyElement(name = "error")
    String error;
    @PropertyElement(name = "cfu_available")
    String cfu_available;
    @PropertyElement(name = "one_available")
    String one_available;


    public String getError() {
        return error;
    }

    public String getCfu_available() {
        return cfu_available;
    }

    public String getOne_available() {
        return one_available;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setCfu_available(String cfu_available) {
        this.cfu_available = cfu_available;
    }

    public void setOne_available(String one_available) {
        this.one_available = one_available;
    }
}
