package com.example.teslaremontchargecontrol.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChargeResponseList {

    @SerializedName("response")
    @Expose
    private ChargeResponse response;

    public ChargeResponse getResponse() {
        return response;
    }

    public void setResponse(ChargeResponse response) {
        this.response = response;
    }
}
