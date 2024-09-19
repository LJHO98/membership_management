package com.membership.Util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecaptchaResponse {
    @JsonProperty("success")
    private boolean success;

    // 기타 필드

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
