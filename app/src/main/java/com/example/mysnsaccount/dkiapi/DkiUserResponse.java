package com.example.mysnsaccount.dkiapi;

public class DkiUserResponse {
    private boolean result;
    private String ErrorCode;
    private String ErrorMsg;
    private String id;
    private String userName;
    private String phoneNum;

    public boolean isResult() {
        return result;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
