package com.example.teslaremontchargecontrol.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleInfoList {

    @SerializedName("response")
    @Expose
    private List<VehicleInfo> response = null;
    @SerializedName("count")
    @Expose
    private Integer count;

    public List<VehicleInfo> getResponse() {
        return response;
    }

    public void setResponse(List<VehicleInfo> response) {
        this.response = response;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}

