package com.example.mysnsaccount.dkiapi;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("resultInfo")
    private ResultInfo resultInfo;

    @SerializedName("userInfo")
    private UserInfo userInfo;

    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public class ResultInfo {
        private boolean result;

        private String errorCode;

        private String errorMsg;

        public void setResult(boolean result) {
            this.result = result;
        }

        public boolean getResult() {
            return this.result;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return this.errorCode;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getErrorMsg() {
            return this.errorMsg;
        }
    }

    public class UserInfo {
        private int num;
        private String id;
        private String pw;
        private String name;
        private String phone;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPw() {
            return pw;
        }

        public void setPw(String pw) {
            this.pw = pw;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }


}
