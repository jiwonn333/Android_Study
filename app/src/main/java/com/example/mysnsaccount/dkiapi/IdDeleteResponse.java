package com.example.mysnsaccount.dkiapi;

import com.google.gson.annotations.SerializedName;

public class IdDeleteResponse {
    @SerializedName("resultInfo")
    private ResultInfo resultInfo;

    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public class ResultInfo {
        private boolean result;
        private String errorCode;
        private String errorMsg;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}
