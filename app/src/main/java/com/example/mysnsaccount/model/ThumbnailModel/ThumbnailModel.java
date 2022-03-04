package com.example.mysnsaccount.model.ThumbnailModel;

import com.example.mysnsaccount.model.ThumbnailModel.Data;

public class ThumbnailModel
{
    private String code;

    private String errMsg;

    private Data data;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setErrMsg(String errMsg){
        this.errMsg = errMsg;
    }
    public String getErrMsg(){
        return this.errMsg;
    }
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }
}
