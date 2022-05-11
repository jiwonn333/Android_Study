package com.example.mysnsaccount.dkiapi;

import com.google.gson.annotations.SerializedName;

public class CommonResponse {
    @SerializedName("resultInfo")
    private ResultInfo resultInfo;

    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public class ResultInfo {
        private boolean result;
        private String errCode;
        private String errorMsg;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }

}
